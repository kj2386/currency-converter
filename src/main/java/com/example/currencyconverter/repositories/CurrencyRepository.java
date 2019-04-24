package com.example.currencyconverter.repositories;


import com.example.currencyconverter.models.Currency;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CurrencyRepository extends CrudRepository<Currency, String> {

    @Override
    List<Currency> findAll();
}
