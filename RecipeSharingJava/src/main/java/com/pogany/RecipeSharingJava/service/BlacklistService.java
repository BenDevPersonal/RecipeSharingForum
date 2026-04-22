package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.dto.BlacklistDto;
import com.pogany.RecipeSharingJava.dto.CreateBlacklistRequest;
import com.pogany.RecipeSharingJava.entity.Blacklist;
import com.pogany.RecipeSharingJava.entity.User;
import com.pogany.RecipeSharingJava.exception.ResourceNotFoundException;
import com.pogany.RecipeSharingJava.repository.BlacklistRepository;
import com.pogany.RecipeSharingJava.repository.FollowRepository;
import com.pogany.RecipeSharingJava.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlacklistService {
    private BlacklistRepository blacklistRepository;
    private UserRepository userRepository;
    private FollowRepository followRepository;

    public BlacklistService(BlacklistRepository blacklistRepository, UserRepository userRepository, FollowRepository followRepository) {
        this.blacklistRepository = blacklistRepository;
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    public List<BlacklistDto> findAll() {
        return blacklistRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public BlacklistDto findById(Integer id) {
        return toDto(blacklistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blacklist not found: " + id)));
    }

    public boolean isBlacklisted(Integer userId) {
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

        return blacklistRepository.existsByBlacklistingUserAndBlacklistedUser(currentUser, targetUser);
    }

    public BlacklistDto create(CreateBlacklistRequest request) {
        Blacklist blacklist = new Blacklist();

        User blacklistingUser = userRepository.findByLogin(getCurrentUser().getLogin())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found: " + getCurrentUser().getLogin()));

        User blacklistedUser = userRepository.findById(request.getBlacklistedUserId())
                        .orElseThrow(()  -> new ResourceNotFoundException("User not found: " + request.getBlacklistedUserId()));

        if (blacklistingUser.getId().equals(blacklistedUser.getId())) {
            throw new IllegalArgumentException("You cannot blacklist yourself");
        }

        followRepository.findByFollowingUserAndFollowedUser(blacklistingUser, blacklistedUser)
                .ifPresent(followRepository::delete);

        blacklist.setBlacklistingUser(blacklistingUser);
        blacklist.setBlacklistedUser(blacklistedUser);

        return toDto(blacklistRepository.save(blacklist));
    }

    public BlacklistDto update(Integer id, CreateBlacklistRequest request) {
        Blacklist blacklist = blacklistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blacklist not found: " + id));

        User blacklistingUser = userRepository.findByLogin(getCurrentUser().getLogin())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + getCurrentUser().getLogin()));

        User blacklistedUser = userRepository.findById(request.getBlacklistedUserId())
                .orElseThrow(()  -> new ResourceNotFoundException("User not found: " + request.getBlacklistedUserId()));

        blacklist.setBlacklistingUser(blacklistingUser);
        blacklist.setBlacklistedUser(blacklistedUser);

        return toDto(blacklistRepository.save(blacklist));
    }

    public void delete(Integer id) {
        Blacklist blacklist = blacklistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blacklist not found: " + id));

        blacklistRepository.delete(blacklist);
    }

    @Transactional
    public void unblacklistUser(Integer blacklistedUserId) {

        User blacklistingUser = userRepository.findByLogin(getCurrentUser().getLogin())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found: " + getCurrentUser().getLogin()));

        User blacklistedUser = userRepository.findById(blacklistedUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found: " + blacklistedUserId));

        if (blacklistingUser.getId().equals(blacklistedUser.getId())) {
            throw new IllegalArgumentException("You cannot unblacklist yourself");
        }

        Blacklist entry = blacklistRepository
                .findByBlacklistingUserAndBlacklistedUser(blacklistingUser, blacklistedUser)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Blacklist entry not found"));

        blacklistRepository.delete(entry);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();

        return userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + login));
    }

    private BlacklistDto toDto(Blacklist blacklist) {
        return new  BlacklistDto(
                blacklist.getId(),
                blacklist.getBlacklistedUser().getId(),
                blacklist.getBlacklistedUser().getLogin()
        );
    }
}
