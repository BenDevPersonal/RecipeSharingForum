package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.dto.CreateNotificationRequest;
import com.pogany.RecipeSharingJava.dto.NotificationDto;
import com.pogany.RecipeSharingJava.entity.Notification;
import com.pogany.RecipeSharingJava.entity.Post;
import com.pogany.RecipeSharingJava.entity.User;
import com.pogany.RecipeSharingJava.enums.NotificationType;
import com.pogany.RecipeSharingJava.exception.ResourceNotFoundException;
import com.pogany.RecipeSharingJava.repository.NotificationRepository;
import com.pogany.RecipeSharingJava.repository.PostRepository;
import com.pogany.RecipeSharingJava.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class NotificationService {
    private NotificationRepository notificationRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository, PostRepository postRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public List<NotificationDto> findAll() {
        return notificationRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public NotificationDto findById(Integer id) {
        return toDto(notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + id)));
    }

    public NotificationDto create(Integer recipientId, CreateNotificationRequest request) {
        Post post = null;
        if (request.getPostId() != null) {
            post = postRepository.findById(request.getPostId())
                    .orElseThrow(() -> new ResourceNotFoundException("Post not found: " + request.getPostId()));
        }

        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipient not found: " + recipientId));

        User actor = getCurrentUser();

        Notification notification = new Notification();

        notification.setUser(recipient);
        notification.setType(request.getType());
        notification.setMessage(buildMessage(request.getType(), request.getMetadata()));
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDate.now());
        notification.setPost(post);
        notification.setActor(actor);
        notification.setMetadata(toJson(request.getMetadata()));

        return toDto(notificationRepository.save(notification));
    }

    public NotificationDto update(Integer id, Integer recipientId, Integer actorId, CreateNotificationRequest request) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + id));

        Post post = null;
        if (request.getPostId() != null) {
            post = postRepository.findById(request.getPostId())
                    .orElseThrow(() -> new ResourceNotFoundException("Post not found: " + request.getPostId()));
        }

        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipient not found: " + recipientId));

        User actor = getCurrentUser();

        notification.setUser(recipient);
        notification.setType(request.getType());
        notification.setMessage(buildMessage(request.getType(), request.getMetadata()));
        notification.setPost(post);
        notification.setActor(actor);
        notification.setMetadata(toJson(request.getMetadata()));

        return toDto(notificationRepository.save(notification));
    }

    public void delete(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + id));

        notificationRepository.delete(notification);
    }

    public List<NotificationDto> findByUserId(Integer userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public long countUnread(Integer userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public void markAsRead(Integer notificationId) {
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        n.setIsRead(true);
        notificationRepository.save(n);
    }

    public List<NotificationDto> findMyNotifications() {
        return findByUserId(getCurrentUser().getId());
    }

    public long countUnreadForCurrentUser() {
        return notificationRepository.countByUserIdAndIsReadFalse(getCurrentUser().getId());
    }

    @Transactional
    public void markAllAsReadForCurrentUser() {
        notificationRepository.markAllAsRead(getCurrentUser().getId());
    }

    // ----- HELPER METHODS -----

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();

        return userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + login));
    }

    private Map<String, Object> parseMetadata(String json) {
        try {
            return new ObjectMapper().readValue(json, Map.class);
        } catch (Exception e) {
            return Map.of();
        }
    }

    private String toJson(Map<String, Object> metadata) {
        try {
            return new ObjectMapper().writeValueAsString(metadata);
        } catch (Exception e) {
            return "{}";
        }
    }

    private String buildMessage(NotificationType type, Map<String, Object> meta) {
        return switch (type) {
            case FEEDBACK_RECEIVED ->
                    meta.getOrDefault("actorName", "Someone") + " left feedback on your recipe \"" + meta.get("postTitle") + "\"";
            case FEEDBACK_DELETED -> "Your feedback from \"" + meta.getOrDefault("postTitle", "a recipe") + "\" was deleted";
            case POST_DELETED -> "Your recipe \"" + meta.get("postTitle") + "\" was deleted";
            case FOLLOWER_GAINED -> meta.getOrDefault("actorName", "Someone") + " started following you";
            case POST_CREATED_BY_FOLLOWED_USER ->  meta.getOrDefault("actorName", "Someone") + " made a new recipe \""  + meta.get("postTitle") + "\"";
            default -> "New notification";
        };
    }

    private NotificationDto toDto(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getType(),
                notification.getMessage(),
                notification.getIsRead(),
                notification.getCreatedAt(),
                notification.getPost() != null ? notification.getPost().getId() : null,
                notification.getPost() != null ? notification.getPost().getTitle() : null,
                notification.getActor() != null ? notification.getActor().getId() : null,
                notification.getActor() != null ? notification.getActor().getLogin() : null,
                parseMetadata(notification.getMetadata())
        );
    }
}
