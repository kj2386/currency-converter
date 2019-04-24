package com.example.currencyconverter.services;

import com.example.currencyconverter.models.ConversionCurrency;
import com.example.currencyconverter.models.Currency;
import com.example.currencyconverter.repositories.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;



    public CurrencyService(CurrencyRepository currencyRepository) {

        this.currencyRepository = currencyRepository;
    }

    public List<Currency> getAllCurrencies() {
        List<Currency> currencyList = this.currencyRepository.findAll();
        currencyList.sort(Comparator.comparing(Currency::getName));
        return currencyList;
    }

    public Optional<Double> convert(ConversionCurrency conversionCurrency) {
       Optional<Currency> toOptional = this.currencyRepository.findById(conversionCurrency.getTo());
       Optional<Currency> fromOptional = this.currencyRepository.findById(conversionCurrency.getFrom());

       if (toOptional.isPresent() && fromOptional.isPresent()) {
           Currency to = toOptional.get();
           Currency from = fromOptional.get();
           Double toValue = to.getValueInEuros();
           Double fromValue = from.getValueInEuros();

           Double answer = toValue * conversionCurrency.getValue() / fromValue;
           Double result = Math.round(answer * 100.0) / 100.0;

           return Optional.of(result);
       }


       return Optional.empty();

    }






}
