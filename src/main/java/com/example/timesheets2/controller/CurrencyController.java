package com.example.timesheets2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.NumberFormat;
import java.util.Locale;

@RestController()
@RequestMapping("/v1/currency")
public class CurrencyController {

    private final String currencySymbol;

    public CurrencyController(@Value("${com.example.country}") String country) {
        var locale = new Locale.Builder().setRegion(country).build();
        var formatter = NumberFormat.getCurrencyInstance(locale);
        currencySymbol = formatter.getCurrency().getSymbol();
    }

    @GetMapping()
    String getCurrency() {
        return currencySymbol;
    }

}
