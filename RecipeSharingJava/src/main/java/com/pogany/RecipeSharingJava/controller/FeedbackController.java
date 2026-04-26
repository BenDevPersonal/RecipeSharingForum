package com.pogany.RecipeSharingJava.controller;

import com.pogany.RecipeSharingJava.dto.CreateFeedbackRequest;
import com.pogany.RecipeSharingJava.dto.FeedbackDto;
import com.pogany.RecipeSharingJava.service.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

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
    public FeedbackDto createFeedback(@RequestBody CreateFeedbackRequest request) {
        return feedbackService.createFeedback(request);
    }

    @PutMapping("/update/{id}")
    public FeedbackDto updateFeedback(@PathVariable Integer id,
                                      @RequestBody CreateFeedbackRequest request) {
        return feedbackService.updateFeedback(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteFeedback(@PathVariable Integer id) {
        feedbackService.delete(id);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgument(IllegalArgumentException ex) {
        return ex.getMessage();
    }
}