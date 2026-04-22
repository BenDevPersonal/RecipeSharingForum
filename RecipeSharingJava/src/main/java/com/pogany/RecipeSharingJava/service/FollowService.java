package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.dto.CreateFollowRequest;
import com.pogany.RecipeSharingJava.dto.CreateNotificationRequest;
import com.pogany.RecipeSharingJava.dto.FollowDto;
import com.pogany.RecipeSharingJava.entity.Follow;
import com.pogany.RecipeSharingJava.entity.User;
import com.pogany.RecipeSharingJava.enums.NotificationType;
import com.pogany.RecipeSharingJava.exception.ResourceNotFoundException;
import com.pogany.RecipeSharingJava.repository.FollowRepository;
import com.pogany.RecipeSharingJava.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class FollowService {
    private FollowRepository followRepository;
    private UserRepository userRepository;
    private NotificationService notificationService;

    public FollowService(FollowRepository followRepository, UserRepository userRepository, NotificationService notificationService) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public List<FollowDto> findAll() {
        return followRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public FollowDto findById(Integer id) {
        return toDto(followRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Follow not found: " + id)));
    }

    public boolean isFollowing(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        User currentUser = userRepository.findByLogin(getCurrentUser().getLogin())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found: " + getCurrentUser().getLogin()
                ));

        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found: " + userId
                ));

        return followRepository.existsByFollowingUserAndFollowedUser(currentUser, targetUser);
    }

    public FollowDto create(CreateFollowRequest request) {
        User followingUser = userRepository.findByLogin(getCurrentUser().getLogin())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + getCurrentUser().getLogin()));

        User followedUser = userRepository.findById(request.getFollowedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getFollowedUserId()));

        if (followingUser.getId().equals(followedUser.getId())) {
            throw new IllegalArgumentException("You cannot follow yourself");
        }

        Follow follow = new Follow();

        follow.setFollowingUser(followingUser);
        follow.setFollowedUser(followedUser);

        CreateNotificationRequest req = new CreateNotificationRequest();
        req.setType(NotificationType.FOLLOWER_GAINED);
        req.setPostId(null);
        req.setMetadata(Map.of(
                "actorName", followingUser.getLogin()
        ));

        notificationService.create(
                followedUser.getId(),
                req
        );

        return toDto(followRepository.save(follow));
    }

    public FollowDto update(Integer id, CreateFollowRequest request) {
        Follow follow = followRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Follow not found: " + id));

        User followingUser = userRepository.findByLogin(getCurrentUser().getLogin())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + getCurrentUser().getLogin()));

        User followedUser = userRepository.findById(request.getFollowedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getFollowedUserId()));

        follow.setFollowingUser(followingUser);
        follow.setFollowedUser(followedUser);

        return toDto(followRepository.save(follow));
    }

    public void delete(Integer id) {
        Follow follow = followRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Follow not found: " + id));

        followRepository.delete(follow);
    }

    @Transactional
    public void unfollowUser(Integer followedUserId) {
        User followingUser = userRepository.findByLogin(getCurrentUser().getLogin())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found: " + getCurrentUser().getLogin()));

        User followedUser = userRepository.findById(followedUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found: " + followedUserId));

        if (followingUser.getId().equals(followedUser.getId())) {
            throw new IllegalArgumentException("You cannot unfollow yourself");
        }

        Follow follow = followRepository
                .findByFollowingUserAndFollowedUser(followingUser, followedUser)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Follow relationship not found"));

        followRepository.delete(follow);
    }

    public long getFollowerCount(Integer userId) {
        return followRepository.countByFollowedUserId(userId);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();

        return userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + login));
    }

    public FollowDto toDto(Follow follow) {
        return new FollowDto(
                follow.getId(),
                follow.getFollowedUser().getId(),
                follow.getFollowedUser().getLogin()
        );
    }
}
