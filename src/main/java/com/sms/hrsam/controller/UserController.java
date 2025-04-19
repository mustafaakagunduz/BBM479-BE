package com.sms.hrsam.controller;

import com.sms.hrsam.dto.RoleUpdateRequest;
import com.sms.hrsam.dto.UserDTO;
import com.sms.hrsam.dto.UserUpdateDTO;
import com.sms.hrsam.entity.Company;
import com.sms.hrsam.entity.User;
import com.sms.hrsam.exception.ResourceNotFoundException;
import com.sms.hrsam.repository.UserRepository;
import com.sms.hrsam.service.CompanyService;
import com.sms.hrsam.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final CompanyService companyService;

    public UserController(UserService userService,
                          PasswordEncoder passwordEncoder,
                          UserRepository userRepository,
                          CompanyService companyService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.companyService = companyService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadProfileImage(
            @RequestParam("profileImage") MultipartFile file,
            @RequestParam("userId") Long userId
    ) {
        try {
            User user = userService.getUserEntity(userId); // getUserEntity kullan
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
            user.setProfileImage(base64Image);
            userService.updateUser(user);
            return ResponseEntity.ok("Profile image updated successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserUpdateDTO updateDTO) {
        try {
            User user = userService.getUserEntity(userId);

            // Email ve username kontrolü
            if (!user.getEmail().equals(updateDTO.getEmail()) &&
                    userRepository.existsByEmail(updateDTO.getEmail())) {
                return ResponseEntity.badRequest().body("Email already in use");
            }

            if (!user.getUsername().equals(updateDTO.getUsername()) &&
                    userRepository.existsByUsername(updateDTO.getUsername())) {
                return ResponseEntity.badRequest().body("Username already in use");
            }

            user.setName(updateDTO.getName());
            user.setEmail(updateDTO.getEmail());
            user.setUsername(updateDTO.getUsername());

            // Company güncellemesi
            if (updateDTO.getCompanyId() != null) {
                Company company = companyService.getCompanyById(updateDTO.getCompanyId());
                user.setCompany(company);
            }

            userService.updateUser(user);
            return ResponseEntity.ok(userService.convertToDTO(user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{userId}/password")
    public ResponseEntity<?> updatePassword(
            @PathVariable Long userId,
            @RequestBody Map<String, String> passwords
    ) {
        try {
            User user = userService.getUserEntity(userId);

            if (!passwordEncoder.matches(passwords.get("currentPassword"), user.getPassword())) {
                return ResponseEntity.badRequest().body("Current password is incorrect");
            }

            user.setPassword(passwordEncoder.encode(passwords.get("newPassword")));
            userService.updateUser(user);

            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update password");
        }
    }


    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        try {
            UserDTO user = userService.getUser(userId);
            return ResponseEntity.ok(user);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching user with ID {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error fetching users: ", e);
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
