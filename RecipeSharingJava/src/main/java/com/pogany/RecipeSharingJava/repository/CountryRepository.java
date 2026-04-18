package com.pogany.RecipeSharingJava.repository;

import com.pogany.RecipeSharingJava.entity.Country;
import com.pogany.RecipeSharingJava.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, String> {
    Optional<Country> findByName(String name);
}

