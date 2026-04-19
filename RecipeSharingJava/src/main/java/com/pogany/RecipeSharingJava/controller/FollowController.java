package com.pogany.RecipeSharingJava.controller;

import com.pogany.RecipeSharingJava.dto.CreateFollowRequest;
import com.pogany.RecipeSharingJava.dto.FollowDto;
import com.pogany.RecipeSharingJava.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
public class FollowController {
    private FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @GetMapping
    public List<FollowDto> getFollows() { return followService.findAll(); }

    @GetMapping("/{id}")
    public FollowDto getFollowById(@PathVariable Integer id) { return followService.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FollowDto createFollow(@RequestBody CreateFollowRequest request) { return followService.create(request); }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FollowDto updateFollow(@PathVariable Integer id, @RequestBody CreateFollowRequest request) { return followService.update(id, request); }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFollow(@PathVariable Integer id) { followService.delete(id); }
}
