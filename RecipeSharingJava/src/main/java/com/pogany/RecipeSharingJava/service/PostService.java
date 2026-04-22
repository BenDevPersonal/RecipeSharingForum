package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.dto.*;
import com.pogany.RecipeSharingJava.entity.*;
import com.pogany.RecipeSharingJava.exception.ResourceNotFoundException;
import com.pogany.RecipeSharingJava.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final AllergyRepository allergyRepository;
    private final FeedbackRepository feedbackRepository;
    private final PostImageRepository postImageRepository;

    public PostService(
            UserRepository userRepository,
            PostRepository postRepository,
            CategoryRepository categoryRepository,
            AllergyRepository allergyRepository,
            FeedbackRepository feedbackRepository,
            PostImageRepository postImageRepository

            ) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.allergyRepository = allergyRepository;
        this.feedbackRepository = feedbackRepository;
        this.postImageRepository = postImageRepository;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();

        return userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + login));
    }

    public List<PostDto> findAll() {
        return postRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public PostDto findById(Integer id) {
        return toDto(
                postRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + id))
        );
    }

    public List<PostDto> search(String q, String category, String allergy) {
        return postRepository.findAll().stream()
                .filter(p -> q == null ||
                        p.getTitle().toLowerCase().contains(q.toLowerCase()) ||
                        p.getContent().toLowerCase().contains(q.toLowerCase()))
                .filter(p -> category == null || category.isBlank() ||
                        p.getCategories().stream().anyMatch(c -> c.getName().equalsIgnoreCase(category)))
                .filter(p -> allergy == null || allergy.isBlank() ||
                        p.getAllergies().stream().anyMatch(a -> a.getName().equalsIgnoreCase(allergy)))
                .map(this::toDto)
                .toList();
    }

    public PostDto createPost(CreatePostRequest request, List<MultipartFile> images) {

        User user = getCurrentUser();

        Post post = new Post();
        post.setUser(user);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCreationDate(LocalDate.now());
        post.setUpdateDate(LocalDate.now());

        post.setAllergies(new ArrayList<>());
        post.setCategories(new ArrayList<>());



        if (request.getAllergyIds() != null) {
            post.getAllergies().addAll(
                    allergyRepository.findAllById(request.getAllergyIds())
            );
        }

        if (request.getCategoryIds() != null) {
            post.getCategories().addAll(
                    categoryRepository.findAllById(request.getCategoryIds())
            );
        }

        if (images != null && !images.isEmpty()) {
            for (MultipartFile file : images) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                Path path = Paths.get("RecipeSharingForum/post_images/" + fileName);

                try {
                    Files.createDirectories(path.getParent());
                    Files.write(path, file.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save image", e);
                }

                PostImage img = new PostImage();
                img.setPost(post);
                img.setImageUrl(fileName);

                postImageRepository.save(img);
            }
        }

        return toDto(postRepository.save(post));
    }

    public PostDto updatePost(Integer id, CreatePostRequest request) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + id));

        User currentUser = getCurrentUser();

        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You cannot edit someone else's post");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setUpdateDate(LocalDate.now());

        post.getAllergies().clear();
        post.getCategories().clear();

        if (request.getAllergyIds() != null) {
            post.getAllergies().addAll(
                    allergyRepository.findAllById(request.getAllergyIds())
            );
        }

        if (request.getCategoryIds() != null) {
            post.getCategories().addAll(
                    categoryRepository.findAllById(request.getCategoryIds())
            );
        }

        return toDto(postRepository.save(post));
    }

    public void delete(Integer id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + id));

        User currentUser = getCurrentUser();

        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You cannot delete someone else's post");
        }

        postRepository.delete(post);
    }

    private PostDto toDto(Post post) {

        User user = post.getUser();

        List<FeedbackDto> feedbacks = feedbackRepository.findByPostId(post.getId())
                .stream()
                .map(f -> new FeedbackDto(
                        f.getId(),
                        f.getUser().getId(),
                        f.getUser().getLogin(),
                        post.getId(),
                        f.getRating(),
                        f.getContent()
                ))
                .toList();

        List<String> images = post.getImages()
                .stream()
                .map(PostImage::getImageUrl)
                .toList();


        return new PostDto(
                post.getId(),
                user.getId(),
                user.getLogin(),
                post.getTitle(),
                post.getContent(),
                post.getCreationDate(),
                post.getUpdateDate(),
                post.getAllergies().stream().map(Allergy::getName).toList(),
                post.getCategories().stream().map(Category::getName).toList(),
                feedbacks,
                images
        );
    }
}