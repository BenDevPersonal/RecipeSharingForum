package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.dto.CreateFollowRequest;
import com.pogany.RecipeSharingJava.dto.FollowDto;
import com.pogany.RecipeSharingJava.entity.Follow;
import com.pogany.RecipeSharingJava.entity.User;
import com.pogany.RecipeSharingJava.exception.ResourceNotFoundException;
import com.pogany.RecipeSharingJava.repository.FollowRepository;
import com.pogany.RecipeSharingJava.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {
    private FollowRepository followRepository;
    private UserRepository userRepository;

    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
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

    public FollowDto create(CreateFollowRequest request) {
        User followingUser = userRepository.findByLogin(getCurrentUser().getLogin())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + getCurrentUser().getLogin()));

        User followedUser = userRepository.findById(request.getFollowingUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getFollowingUserId()));

        Follow follow = new Follow();

        follow.setFollowingUser(followingUser);
        follow.setFollowedUser(followedUser);

        return toDto(followRepository.save(follow));
    }

    public FollowDto update(Integer id, CreateFollowRequest request) {
        Follow follow = followRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Follow not found: " + id));

        User followingUser = userRepository.findByLogin(getCurrentUser().getLogin())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + getCurrentUser().getLogin()));

        User followedUser = userRepository.findById(request.getFollowingUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getFollowingUserId()));

        follow.setFollowingUser(followingUser);
        follow.setFollowedUser(followedUser);

        return toDto(followRepository.save(follow));
    }

    public void delete(Integer id) {
        Follow follow = followRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Follow not found: " + id));

        followRepository.delete(follow);
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
