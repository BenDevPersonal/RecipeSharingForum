package com.pogany.RecipeSharingJava.controller;

import com.pogany.RecipeSharingJava.dto.CreateNotificationRequest;
import com.pogany.RecipeSharingJava.dto.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private com.pogany.RecipeSharingJava.service.NotificationService notificationService;

    @Autowired
    public NotificationController(com.pogany.RecipeSharingJava.service.NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<NotificationDto> getNotifications() { return notificationService.findAll(); }

    @GetMapping("{id}")
    public NotificationDto getNotification(@PathVariable Integer id) { return notificationService.findById(id); }

    @GetMapping("/me")
    public List<NotificationDto> getMyNotifications() {
        return notificationService.findMyNotifications();
    }

    @GetMapping("/unread/count")
    public long countUnread() {
        return notificationService.countUnreadForCurrentUser();
    }

    @PostMapping("/{recipientId}")
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationDto create(
            @PathVariable Integer recipientId,
            @RequestBody CreateNotificationRequest request
    ) {
        return notificationService.create(recipientId, request);
    }

    @PatchMapping("/mark-all-read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markAllAsRead() {
        notificationService.markAllAsReadForCurrentUser();
    }
}
