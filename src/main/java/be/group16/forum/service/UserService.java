package be.group16.forum.service;

import be.group16.forum.model.User;
import be.group16.forum.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken");
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already registered");
        }
        
        if (user.getDisplayName() == null || user.getDisplayName().trim().isEmpty()) {
            user.setDisplayName(user.getUsername());
        }
        
        user.addRole("user");
        
        return userRepository.save(user);
    }
    
    public User findUserById(String id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
    }
    
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with username: " + username));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email: " + email));
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }
    
    public List<User> getVerifiedUsers() {
        return userRepository.findByEmailVerifiedTrue();
    }
    
    public User updateUser(String id, User updatedUser) {
        User existingUser = findUserById(id);
        
        if (updatedUser.getDisplayName() != null) {
            existingUser.setDisplayName(updatedUser.getDisplayName());
        }
        if (updatedUser.getBio() != null) {
            existingUser.setBio(updatedUser.getBio());
        }
        if (updatedUser.getSignature() != null) {
            existingUser.setSignature(updatedUser.getSignature());
        }
        if (updatedUser.getImage() != null) {
            existingUser.setImage(updatedUser.getImage());
        }
        if (updatedUser.getUrl() != null) {
            existingUser.setUrl(updatedUser.getUrl());
        }
        
        return userRepository.save(existingUser);
    }
    
    public void updatePassword(String id, String currentPassword, String newPassword) {
        User user = findUserById(id);
        user.setPassword(newPassword);
        
        userRepository.save(user);
    }
    
    public User verifyEmail(String id) {
        User user = findUserById(id);
        user.setEmailVerified(true);
        return userRepository.save(user);
    }
    
    public User addRole(String id, String role) {
        User user = findUserById(id);
        user.addRole(role);
        return userRepository.save(user);
    }
    
    public User removeRole(String id, String role) {
        User user = findUserById(id);
        user.removeRole(role);
        return userRepository.save(user);
    }
    
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}