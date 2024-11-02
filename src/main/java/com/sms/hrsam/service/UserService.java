package com.sms.hrsam.service;

import com.sms.hrsam.entity.User;
import com.sms.hrsam.exception.ResourceNotFoundException;
import com.sms.hrsam.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        return userRepository.findAll();
    }
}
