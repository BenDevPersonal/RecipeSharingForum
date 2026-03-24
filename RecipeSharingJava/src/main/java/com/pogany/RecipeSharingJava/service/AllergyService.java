package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.entities.Allergy;
import com.pogany.RecipeSharingJava.repository.AllergyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AllergyService {

    @Autowired
    private AllergyRepository allergyRepository;

    @Transactional(readOnly = true)
    public List<Allergy> findAll() {
        return allergyRepository.findAll();
    }

    @Transactional(readOnly = true)
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