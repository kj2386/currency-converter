package com.example.currencyconverter.tasks;

import com.example.currencyconverter.models.Currency;
import com.example.currencyconverter.models.CurrencyDTO;
import com.example.currencyconverter.repositories.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class CurrencyTask {

    @Autowired
    private CurrencyRepository currencyRepository;

    //runs every 5 hours
    @Scheduled(fixedRate = 5 * 1000 * 60 * 60)
    private void getRatesTask() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            CurrencyDTO forObject = restTemplate.getForObject("http://data.fixer.io/api/latest?access_key=85b152dd1a19ee1bb3fb8bfef246416c", CurrencyDTO.class);
            forObject.getRates().forEach((key, value) -> {
                Currency currency = new Currency(key, value);
                this.currencyRepository.save(currency);
            });
        } catch (RestClientException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
