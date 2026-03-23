package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.entities.Allergy;
import com.pogany.RecipeSharingJava.repository.AllergyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllergyService {

    @Autowired
    private AllergyRepository allergyRepository;

    public List<Allergy> findAll() {
        return allergyRepository.findAll();
    }

    public Allergy findById(Integer id) {
        return allergyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Allergy not found"));
    }

    public Allergy save(Allergy allergy) {
        return allergyRepository.save(allergy);
    }

    public void delete(Integer id) {
        allergyRepository.deleteById(id);
    }
}