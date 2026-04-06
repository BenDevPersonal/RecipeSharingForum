package com.pogany.RecipeSharingJava.controller;

import com.pogany.RecipeSharingJava.dto.CountryDto;
import com.pogany.RecipeSharingJava.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {
    private CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public List<CountryDto> getAllCountries() {
        return countryService.findAll();
    }

    @GetMapping("/{code}")
    public CountryDto getCountryById(@PathVariable String code) {
        return countryService.findById(code);
    }
}
