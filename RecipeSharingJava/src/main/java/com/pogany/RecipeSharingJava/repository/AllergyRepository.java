package com.pogany.RecipeSharingJava.repository;

import com.pogany.RecipeSharingJava.entities.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllergyRepository extends JpaRepository<Allergy, Integer> {
}
