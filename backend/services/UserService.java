package services;

import models.User;
import repositories.UserRepository;

import java.util.UUID;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String name, String email, String password, String phoneNumber) {
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User with email " + email + " already exists!");
        }

        String userId = "USER_" + UUID.randomUUID().toString().substring(0, 8);
        User user = new User(userId, name, email, password, phoneNumber);
        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials!");
        }
        return user;
    }

    public User getUserById(String userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        return user;
    }

    public boolean validateCredentials(String email, String password) {
        try {
            loginUser(email, password);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
