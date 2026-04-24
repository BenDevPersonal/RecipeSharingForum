package com.pogany.RecipeSharingJava.repository;

import com.pogany.RecipeSharingJava.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Integer> {
}