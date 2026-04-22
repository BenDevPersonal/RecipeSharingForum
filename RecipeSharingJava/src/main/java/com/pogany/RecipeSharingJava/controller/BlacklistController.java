package com.pogany.RecipeSharingJava.controller;

import com.pogany.RecipeSharingJava.dto.BlacklistDto;
import com.pogany.RecipeSharingJava.dto.CreateBlacklistRequest;
import com.pogany.RecipeSharingJava.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blacklist")
public class BlacklistController {
    private BlacklistService blacklistService;

    @Autowired
    public BlacklistController(BlacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    @GetMapping
    public List<BlacklistDto> getBlacklists() { return blacklistService.findAll(); }

    @GetMapping("/{id}")
    public BlacklistDto getBlacklistById(@PathVariable Integer id) { return blacklistService.findById(id); }

    @GetMapping("/is-blacklisted/{id}")
    public boolean isBlacklisted(@PathVariable Integer id) { return blacklistService.isBlacklisted(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BlacklistDto createBlacklist(@RequestBody CreateBlacklistRequest request) { return blacklistService.create(request); }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BlacklistDto updateBlacklist(@PathVariable Integer id, @RequestBody CreateBlacklistRequest request) {
        return blacklistService.update(id, request);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBlacklist(@PathVariable Integer id) { blacklistService.delete(id); }

    @DeleteMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void unblacklistUser(@PathVariable Integer id) { blacklistService.unblacklistUser(id); }
}
