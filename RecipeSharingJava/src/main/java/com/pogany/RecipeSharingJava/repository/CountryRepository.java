package com.pogany.RecipeSharingJava.repository;

import com.pogany.RecipeSharingJava.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, String> {
}

