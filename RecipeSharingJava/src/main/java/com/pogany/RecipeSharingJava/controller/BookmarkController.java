package com.pogany.RecipeSharingJava.controller;

import com.pogany.RecipeSharingJava.dto.PostDto;
import com.pogany.RecipeSharingJava.entity.User;
import com.pogany.RecipeSharingJava.repository.UserRepository;
import com.pogany.RecipeSharingJava.service.BookmarkService;
import com.pogany.RecipeSharingJava.service.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final UserRepository userRepository;
    private final PostService postService;

    public BookmarkController(
            BookmarkService bookmarkService,
            UserRepository userRepository,
            PostService postService
    ) {
        this.bookmarkService = bookmarkService;
        this.userRepository = userRepository;
        this.postService = postService;
    }
    
    @PostMapping("/{postId}")
    public void toggleBookmark(
            @PathVariable Integer postId,
            Authentication authentication
    ) {
        User user = userRepository.findByLogin(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        bookmarkService.toggleBookmark(user.getId(), postId);
    }

    @GetMapping("/me")
    public List<PostDto> getMyBookmarks(Authentication authentication) {

        User user = userRepository.findByLogin(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return bookmarkService.getBookmarkedPosts(user)
                .stream()
                .map(postService::toDto)
                .toList();
    }
}