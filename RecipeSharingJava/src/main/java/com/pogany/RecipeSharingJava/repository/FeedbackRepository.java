package com.pogany.RecipeSharingJava.repository;

import com.pogany.RecipeSharingJava.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
}
