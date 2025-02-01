package com.sms.hrsam.service;

import com.sms.hrsam.dto.CompanyDTO;
import com.sms.hrsam.dto.UserDTO;
import com.sms.hrsam.entity.Role;
import com.sms.hrsam.entity.User;
import com.sms.hrsam.entity.UserRole;
import com.sms.hrsam.exception.ResourceNotFoundException;
import com.sms.hrsam.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;


    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;

    }
    /**
     * Creates a new user and saves it to the database.
     *
     * @param user User entity to be created.
     * @return Created User.
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    // DTO döndüren metot (API responses için)
    public UserDTO getUser(Long userId) {
        User user = getUserEntity(userId);
        return convertToDTO(user);
    }


    /**
     * Retrieves all users from the database.
     *
     * @return List of all users.
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .username(user.getUsername())
                .profileImage(user.getProfileImage())  // Add this line
                .role(user.getRole() != null ? user.getRole().getName().toString() : null)
                .company(user.getCompany() != null ? CompanyDTO.builder()
                        .id(user.getCompany().getId())
                        .name(user.getCompany().getName())
                        .build() : null)
                .build();
    }


    @Transactional
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResourceNotFoundException("No authenticated user found");
        }

        // Principal'dan email'i alıyoruz
        String userEmail = auth.getName();
        System.out.println("Getting current user for email: " + userEmail);

        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for email: " + userEmail));
    }

    @Transactional
    public void updateUser(User user) {
        userRepository.save(user);
    }


    @Transactional
    public User updateUserRole(Long userId, String roleName) {
        User user = getUserEntity(userId);

        if (user.getId() == 1) {
            throw new IllegalArgumentException("Cannot change role of system administrator");
        }

        // String'i UserRole enum'una çeviriyoruz
        UserRole newRole = UserRole.valueOf(roleName);

        // Yeni Role objesi oluştur veya mevcut olanı bul
        Role role = roleService.getRoleByName(newRole);
        user.setRole(role);

        return userRepository.save(user);
    }
}
