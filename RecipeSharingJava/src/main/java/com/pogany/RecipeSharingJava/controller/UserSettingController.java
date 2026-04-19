package com.pogany.RecipeSharingJava.controller;

import com.pogany.RecipeSharingJava.dto.CreateUserSettingRequest;
import com.pogany.RecipeSharingJava.dto.UserSettingDto;
import com.pogany.RecipeSharingJava.service.UserSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settings")
public class UserSettingController {
    private UserSettingService userSettingService;

    @Autowired
    public UserSettingController(UserSettingService userSettingService) {
        this.userSettingService = userSettingService;
    }

    @GetMapping
    public List<UserSettingDto> getUserSettings() { return userSettingService.findALl(); }

    @GetMapping("{id}")
    public UserSettingDto getUserSetting(@PathVariable Integer id) {  return userSettingService.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserSettingDto createUserSetting(@RequestBody CreateUserSettingRequest request) { return userSettingService.create(request); }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserSettingDto updateUserSetting(@PathVariable Integer id, @RequestBody CreateUserSettingRequest request) { return userSettingService.update(id, request); }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserSetting(@PathVariable Integer id) { userSettingService.delete(id); }
}
