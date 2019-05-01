package com.example.currencyconverter.controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.currencyconverter.models.Currency;
import com.example.currencyconverter.services.CurrencyService;

@RestController
public class ListAllCurrenciesController {

    @Autowired
    CurrencyService currencyService;

    // List all currencies
    @RequestMapping(value = "/currencies", produces = {"application/json"}, method = RequestMethod.GET)
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        return new ResponseEntity<>(this.currencyService.getAllCurrencies(), HttpStatus.OK);
    }
}
