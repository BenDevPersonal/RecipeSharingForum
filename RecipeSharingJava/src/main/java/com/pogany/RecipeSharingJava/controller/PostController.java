package com.pogany.RecipeSharingJava.controller;

import com.pogany.RecipeSharingJava.dto.CreatePostRequest;
import com.pogany.RecipeSharingJava.dto.PostDto;
import com.pogany.RecipeSharingJava.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostDto> getAllPosts() {
        return postService.findAll();
    }

    @GetMapping("/{id}")
    public PostDto getPostById(@PathVariable Integer id) {
        return postService.findById(id);
    }

    @GetMapping("/search")
    public List<PostDto> searchPosts(
            @RequestParam String q,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String allergy
    ) {
        return postService.search(q, category, allergy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createPost(@RequestBody CreatePostRequest request) {
        return postService.createPost(request);
    }

    @PutMapping("/update/{id}")
    public PostDto updatePost(@PathVariable Integer id,
                              @RequestBody CreatePostRequest request) {

        return postService.updatePost(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePost(@PathVariable Integer id) {
        postService.delete(id);
    }
}