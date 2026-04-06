package com.pogany.RecipeSharingJava.controller;

import com.pogany.RecipeSharingJava.dto.CreateFeedbackDto;
import com.pogany.RecipeSharingJava.dto.FeedbackDto;
import com.pogany.RecipeSharingJava.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {
    private FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public List<FeedbackDto> getAllFeedbacks() {
        return feedbackService.findAll();
    }

    @GetMapping("/{id}")
    public FeedbackDto getFeedbackById(@PathVariable Integer id) {
        return feedbackService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FeedbackDto createFeedback(@RequestBody CreateFeedbackDto request) {
        return feedbackService.createFeedback(request);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FeedbackDto updateFeedback(@PathVariable Integer id, @RequestBody CreateFeedbackDto request) { return feedbackService.updateFeedback(id, request); }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFeedback(@PathVariable Integer id) { feedbackService.delete(id); }
}
