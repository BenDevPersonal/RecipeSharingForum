package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.dto.AllergyDto;
import com.pogany.RecipeSharingJava.dto.CreateAllergyRequest;
import com.pogany.RecipeSharingJava.entity.Allergy;
import com.pogany.RecipeSharingJava.exception.ResourceNotFoundException;
import com.pogany.RecipeSharingJava.repository.AllergyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AllergyService {

    private AllergyRepository allergyRepository;

    public AllergyService(AllergyRepository allergyRepository) {
        this.allergyRepository = allergyRepository;
    }

    public List<AllergyDto> findAll() {
        return allergyRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public AllergyDto findById(Integer id) {
        return toDto(allergyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allergy not found with ID: " + id)));
    }

    public AllergyDto updateAllergy(Integer id, CreateAllergyRequest request) {
        Allergy allergy = allergyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allergy not found with ID: " + id));

        allergy.setName(request.getName());

        return toDto(allergyRepository.save(allergy));
    }

    public AllergyDto createAllergy(CreateAllergyRequest request) {
        Allergy allergy = new Allergy();
        allergy.setName(request.getName());

        return toDto(allergyRepository.save(allergy));
    }

    public void delete(Integer id) {
        allergyRepository.deleteById(id);
    }

    private AllergyDto toDto(Allergy allergy) {
        return new AllergyDto(
                allergy.getId(),
                allergy.getName()
        );
    }
}