package com.pogany.RecipeSharingJava.controller;

import com.pogany.RecipeSharingJava.dto.AllergyDto;
import com.pogany.RecipeSharingJava.dto.CreateAllergyRequest;
import com.pogany.RecipeSharingJava.service.AllergyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/allergies")
public class AllergyController {
    private AllergyService allergyService;

    @Autowired
    public AllergyController(AllergyService allergyService) {
        this.allergyService = allergyService;
    }

    @GetMapping
    public List<AllergyDto> getAllAllergies() {
        return allergyService.findAll();
    }

    @GetMapping("/{id}")
    public AllergyDto getAllergyById(@PathVariable Integer id) {
        return allergyService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AllergyDto createAllergy(@RequestBody CreateAllergyRequest request) {
        return  allergyService.createAllergy(request);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AllergyDto updateAllergy(@PathVariable Integer id, @RequestBody CreateAllergyRequest request) {
        return  allergyService.updateAllergy(id, request);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllergy(@PathVariable Integer id) {
        allergyService.delete(id);
    }
}
