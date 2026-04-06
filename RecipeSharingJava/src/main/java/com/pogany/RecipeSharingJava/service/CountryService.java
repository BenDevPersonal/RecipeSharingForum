package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.dto.CountryDto;
import com.pogany.RecipeSharingJava.entity.Country;
import com.pogany.RecipeSharingJava.exception.ResourceNotFoundException;
import com.pogany.RecipeSharingJava.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {
    private CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<CountryDto> findAll() {
        return countryRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public CountryDto findById(String code) {
        return toDto(countryRepository.findById(code)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with Code: " + code)));
    }

    private CountryDto toDto(Country country) {
        return new CountryDto(
                country.getCode(),
                country.getName()
        );
    }
}
