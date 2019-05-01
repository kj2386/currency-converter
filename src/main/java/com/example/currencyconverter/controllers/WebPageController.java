package com.example.currencyconverter.controllers;

import com.example.currencyconverter.models.ConversionCurrency;
import com.example.currencyconverter.models.Currency;
import com.example.currencyconverter.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class WebPageController {

    @Autowired
    CurrencyService currencyService;

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @GetMapping("/home")
    public String showWebPage(Model theModel) {


        ConversionCurrency conversionCurrency = new ConversionCurrency();
        theModel.addAttribute("conversion", conversionCurrency);

        List<Currency> from = currencyService.getAllCurrencies();
        theModel.addAttribute("fromCurrency", from);

        List<Currency> to = currencyService.getAllCurrencies();
        theModel.addAttribute("toCurrency", to);


        return "currency-converter";
    }

    @PostMapping("/convert")
    public String showResult(@ModelAttribute("conversion") ConversionCurrency conversionCurrency, Model theModel) {

        List<Currency> from = currencyService.getAllCurrencies();
        theModel.addAttribute("fromCurrency", from);

        List<Currency> to = currencyService.getAllCurrencies();
        theModel.addAttribute("toCurrency", to);


        Optional<Double> answer = currencyService.convert(conversionCurrency);

        if (answer.isPresent()) {

           conversionCurrency.setResult(answer.get());
            return "currency-converter";
        }

        return "currency-converter";
    }

}

