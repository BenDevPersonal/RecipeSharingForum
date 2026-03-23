package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.entities.Feedback;
import com.pogany.RecipeSharingJava.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    public Feedback findById(Integer id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
    }

    public Feedback save(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public void delete(Integer id) {
        feedbackRepository.deleteById(id);
    }
}