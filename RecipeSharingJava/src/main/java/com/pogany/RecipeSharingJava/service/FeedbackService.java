package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.dto.CreateFeedbackRequest;
import com.pogany.RecipeSharingJava.dto.FeedbackDto;
import com.pogany.RecipeSharingJava.entity.Feedback;
import com.pogany.RecipeSharingJava.entity.Post;
import com.pogany.RecipeSharingJava.entity.User;
import com.pogany.RecipeSharingJava.exception.ResourceNotFoundException;
import com.pogany.RecipeSharingJava.repository.FeedbackRepository;
import com.pogany.RecipeSharingJava.repository.PostRepository;
import com.pogany.RecipeSharingJava.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public FeedbackService(
            FeedbackRepository feedbackRepository,
            PostRepository postRepository,
            UserRepository userRepository
    ) {
        this.feedbackRepository = feedbackRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<FeedbackDto> findAll() {
        return feedbackRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public FeedbackDto findById(Integer id) {
        return toDto(
                feedbackRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Feedback not found with ID: " + id)
                        )
        );
    }

    public FeedbackDto createFeedback(CreateFeedbackRequest request) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setPost(post);
        feedback.setRating(request.getRating());
        feedback.setContent(request.getContent());

        return toDto(feedbackRepository.save(feedback));
    }

    public FeedbackDto updateFeedback(Integer id, CreateFeedbackRequest request) {

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with ID: " + id));

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + request.getPostId()));

        feedback.setPost(post);
        feedback.setRating(request.getRating());
        feedback.setContent(request.getContent());

        return toDto(feedbackRepository.save(feedback));
    }

    public void delete(Integer id) {
        feedbackRepository.deleteById(id);
    }

    private FeedbackDto toDto(Feedback feedback) {
        return new FeedbackDto(
                feedback.getId(),
                feedback.getUser().getId(),
                feedback.getUser().getLogin(),
                feedback.getPost().getId(),
                feedback.getRating(),
                feedback.getContent()
        );
    }
}