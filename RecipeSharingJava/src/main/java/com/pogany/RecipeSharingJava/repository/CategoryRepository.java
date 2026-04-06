package com.pogany.RecipeSharingJava.repository;

import com.pogany.RecipeSharingJava.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
