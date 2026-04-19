package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.dto.BlacklistDto;
import com.pogany.RecipeSharingJava.dto.CreateBlacklistRequest;
import com.pogany.RecipeSharingJava.entity.Blacklist;
import com.pogany.RecipeSharingJava.entity.User;
import com.pogany.RecipeSharingJava.exception.ResourceNotFoundException;
import com.pogany.RecipeSharingJava.repository.BlacklistRepository;
import com.pogany.RecipeSharingJava.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlacklistService {
    private BlacklistRepository blacklistRepository;
    private UserRepository userRepository;

    public BlacklistService(BlacklistRepository blacklistRepository, UserRepository userRepository) {
        this.blacklistRepository = blacklistRepository;
        this.userRepository = userRepository;
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

    public BlacklistDto create(CreateBlacklistRequest request) {
        Blacklist blacklist = new Blacklist();

        User blacklistingUser = userRepository.findByLogin(getCurrentUser().getLogin())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found: " + getCurrentUser().getLogin()));

        User blacklistedUser = userRepository.findById(request.getBlacklistedUserId())
                        .orElseThrow(()  -> new ResourceNotFoundException("User not found: " + request.getBlacklistedUserId()));

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
