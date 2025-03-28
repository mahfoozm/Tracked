package com.tracked.user.controller;

import com.tracked.user.model.User;
import com.tracked.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        return ResponseEntity.ok(userService.allUsers());
    }

    @PutMapping(value = "/update", consumes = "multipart/form-data")
    public ResponseEntity<User> updateUser(
        @RequestPart("fullName") String fullName,
        @RequestPart("email") String email,
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        User updated = userService.updateUser(fullName, email, profileImage);
        return ResponseEntity.ok(updated);
    }
}