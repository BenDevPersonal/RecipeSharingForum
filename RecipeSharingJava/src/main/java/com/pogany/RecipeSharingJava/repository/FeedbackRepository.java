package com.pogany.RecipeSharingJava.repository;

import com.pogany.RecipeSharingJava.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    List<Feedback> findByPostId(Integer postId);
}
