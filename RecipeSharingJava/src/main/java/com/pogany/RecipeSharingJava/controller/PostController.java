package com.pogany.RecipeSharingJava.controller;

import com.pogany.RecipeSharingJava.dto.CreatePostRequest;
import com.pogany.RecipeSharingJava.dto.CreateUserRequest;
import com.pogany.RecipeSharingJava.dto.PostDto;
import com.pogany.RecipeSharingJava.dto.UserDto;
import com.pogany.RecipeSharingJava.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private PostService postService;

    @Autowired
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createPost(@RequestBody CreatePostRequest request) {
        return postService.createPost(request);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDto updatePost(@PathVariable Integer id, @RequestBody CreatePostRequest request) { return postService.updatePost(id, request); }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@PathVariable Integer id) { postService.delete(id); }
}
