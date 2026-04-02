package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.entity.Allergy;
import com.pogany.RecipeSharingJava.exception.ResourceNotFoundException;
import com.pogany.RecipeSharingJava.repository.AllergyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllergyService {

    private AllergyRepository allergyRepository;

    public AllergyService(AllergyRepository allergyRepository) {
        this.allergyRepository = allergyRepository;
    }

    public List<Allergy> findAll() {
        return allergyRepository.findAll();
    }

    public Allergy findById(Integer id) {
        return allergyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allergy not found with ID: " + id));
    }

    public Allergy save(Allergy allergy) {
        return allergyRepository.save(allergy);
    }

    public void delete(Integer id) {
        allergyRepository.deleteById(id);
    }
}