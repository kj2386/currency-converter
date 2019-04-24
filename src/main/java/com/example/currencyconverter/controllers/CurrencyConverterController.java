package com.example.currencyconverter.controllers;

import com.example.currencyconverter.models.ConversionCurrency;
import com.example.currencyconverter.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class CurrencyConverterController {

    @Autowired
    CurrencyService currencyService;

    //converts between two currencies

    @RequestMapping(value = "/currency-converter", method = RequestMethod.POST)
    public ResponseEntity<Double> convertCurrencies(@RequestBody ConversionCurrency conversionCurrency) {
        Optional<Double> resultOptional = this.currencyService.convert(conversionCurrency);
        if (resultOptional.isPresent()) {
            return new ResponseEntity<>(resultOptional.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
