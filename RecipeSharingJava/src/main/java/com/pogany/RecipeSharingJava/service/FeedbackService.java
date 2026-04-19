package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.dto.CreateFeedbackRequest;
import com.pogany.RecipeSharingJava.dto.CreateNotificationRequest;
import com.pogany.RecipeSharingJava.dto.FeedbackDto;
import com.pogany.RecipeSharingJava.entity.Feedback;
import com.pogany.RecipeSharingJava.entity.Post;
import com.pogany.RecipeSharingJava.entity.User;
import com.pogany.RecipeSharingJava.enums.NotificationType;
import com.pogany.RecipeSharingJava.exception.ResourceNotFoundException;
import com.pogany.RecipeSharingJava.repository.FeedbackRepository;
import com.pogany.RecipeSharingJava.repository.PostRepository;
import com.pogany.RecipeSharingJava.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private NotificationService notificationService;

    public FeedbackService(FeedbackRepository feedbackRepository, PostRepository postRepository, UserRepository userRepository, NotificationService notificationService) {
        this.feedbackRepository = feedbackRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
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
        User user = getCurrentUser();

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setPost(post);
        feedback.setRating(request.getRating());
        feedback.setContent(request.getContent());

        String postTitle = feedback.getPost().getTitle();

        CreateNotificationRequest req = new CreateNotificationRequest();
        req.setType(NotificationType.FEEDBACK_RECEIVED);
        req.setPostId(feedback.getPost().getId());
        req.setMetadata(Map.of(
                "postTitle", postTitle,
                "actorName", user.getLogin()
        ));

        notificationService.create(
                post.getUser().getId(),
                req
        );

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
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with ID: " + id));

        User currentUser = getCurrentUser();

        User feedbackOwner = feedback.getUser();
        String postTitle = feedback.getPost().getTitle();

        feedbackRepository.delete(feedback);

        CreateNotificationRequest req = new CreateNotificationRequest();
        req.setType(NotificationType.FEEDBACK_DELETED);
        req.setPostId(feedback.getPost().getId());
        req.setMetadata(Map.of(
                "postTitle", postTitle,
                "actorName", currentUser.getLogin()
        ));

        notificationService.create(
                feedbackOwner.getId(),
                req
        );
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();

        return userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + login));
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