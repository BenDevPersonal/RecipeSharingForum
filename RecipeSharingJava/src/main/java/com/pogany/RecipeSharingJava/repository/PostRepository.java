package com.pogany.RecipeSharingJava.repository;

import com.pogany.RecipeSharingJava.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
