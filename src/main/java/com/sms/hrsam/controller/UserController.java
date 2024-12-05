package com.sms.hrsam.controller;

import com.sms.hrsam.dto.RoleUpdateRequest;
import com.sms.hrsam.entity.User;
import com.sms.hrsam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadProfileImage(
            @RequestParam("profileImage") MultipartFile file,
            @RequestParam("userId") Long userId
    ) {
        try {
            User user = userService.getUser(userId);
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
            user.setProfileImage(base64Image);
            userService.updateUser(user);
            return ResponseEntity.ok("Profile image updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            System.out.println("Found users: " + users.size()); // Log ekleyelim
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            System.err.println("Error getting users: " + e.getMessage()); // Hata logu
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<User> updateUserRole(
            @PathVariable Long userId,
            @RequestBody RoleUpdateRequest roleRequest) {
        try {
            System.out.println("Received role update request for user: " + userId + " with role: " + roleRequest.getRoleName());
            User updatedUser = userService.updateUserRole(userId, roleRequest.getRoleName());
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            System.err.println("Error updating role: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
