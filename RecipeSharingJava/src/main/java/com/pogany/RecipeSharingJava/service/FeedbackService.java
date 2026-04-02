package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.dto.CreateFeedbackDto;
import com.pogany.RecipeSharingJava.dto.FeedbackDto;
import com.pogany.RecipeSharingJava.entity.Feedback;
import com.pogany.RecipeSharingJava.entity.Post;
import com.pogany.RecipeSharingJava.entity.User;
import com.pogany.RecipeSharingJava.exception.ResourceNotFoundException;
import com.pogany.RecipeSharingJava.repository.FeedbackRepository;
import com.pogany.RecipeSharingJava.repository.PostRepository;
import com.pogany.RecipeSharingJava.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private FeedbackRepository feedbackRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, PostRepository postRepository, UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<FeedbackDto> findAll() {
        return feedbackRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public FeedbackDto findById(Integer id) {
        return toDto(feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with ID: " + id)));
    }

    public FeedbackDto createFeedback(CreateFeedbackDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + request.getPostId()));

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setPost(post);
        feedback.setRating(request.getRating());
        feedback.setContent(request.getContent());

        return toDto(feedbackRepository.save(feedback));
    }

    public void delete(Integer id) {
        feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with ID: " + id));

        feedbackRepository.deleteById(id);
    }

    private FeedbackDto toDto(Feedback feedback) {
        return new FeedbackDto(
            feedback.getId(),
            feedback.getUser().getId(),
            feedback.getPost().getId(),
            feedback.getRating(),
            feedback.getContent()
        );
    }
}