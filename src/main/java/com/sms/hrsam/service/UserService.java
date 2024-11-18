package com.sms.hrsam.service;

import com.sms.hrsam.entity.Role;
import com.sms.hrsam.entity.User;
import com.sms.hrsam.entity.UserRole;
import com.sms.hrsam.exception.ResourceNotFoundException;
import com.sms.hrsam.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * Retrieves a user by their ID. Throws exception if user not found.
     *
     * @param userId ID of the user to retrieve.
     * @return User entity.
     * @throws ResourceNotFoundException if user is not found.
     */
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    /**
     * Retrieves all users from the database.
     *
     * @return List of all users.
     */
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        System.out.println("Service found users: " + users.size()); // Log ekleyelim
        return users;
    }

    @Transactional
    public User updateUserRole(Long userId, String roleName) {
        User user = getUser(userId);

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
