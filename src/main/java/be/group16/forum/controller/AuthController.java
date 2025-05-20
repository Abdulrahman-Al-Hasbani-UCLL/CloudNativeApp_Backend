package be.group16.forum.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.group16.forum.dto.LoginInput;
import be.group16.forum.dto.RegisterInput;
import be.group16.forum.model.User;
import be.group16.forum.repository.UserRepository;
import be.group16.forum.service.UserService;
import be.group16.forum.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and account management")
public class AuthController {
    private UserRepository userRepository;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, UserService userService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    // private void saveResetToken(String email, String resetToken) {
    //     User user = userRepository.findByEmail(email).get();
    //     user.addExtendedData("resetToken", resetToken);
    //     userRepository.save(user);
    // }

    // @PostMapping("/forgot-password")
    // public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> requestBody) {
    //     String email = requestBody.get("email");

    //     boolean emailExists = userRepository.existsByEmail(email);

    //     if (!emailExists) {
    //         return ResponseEntity.status(404).body(Map.of("error", "Email not found"));
    //     }

    //     String resetToken = UUID.randomUUID().toString();

    //     saveResetToken(email, resetToken);

    //     // Return the reset token in the response
    //     Map<String, String> response = new HashMap<>();
    //     response.put("resetToken", resetToken);
    //     return ResponseEntity.ok(response);
    // }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginInput requestBody) {
        String login = requestBody.getLogin();
        String password = requestBody.getPassword();

        User user = userRepository.findByUsername(login)
                .or(() -> userRepository.findByEmail(login))
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid login credentials"));
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid login credentials"));
        }

        String token = jwtUtil.generateToken(user.getUsername());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        // Extract the token from the Authorization header
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Authorization header is missing or invalid"));
        }

        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix

        String username;
        try {
            username = jwtUtil.validateToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired token"));
        }

        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterInput newUser) {
        if (userRepository.existsByUsername(newUser.getUsername())) {
            return ResponseEntity.status(400).body(Map.of("error", "Username is already taken"));
        }

        if (userRepository.existsByEmail(newUser.getEmail())) {
            return ResponseEntity.status(400).body(Map.of("error", "Email is already registered"));
        }

        if (newUser.getDisplayName() == null || newUser.getDisplayName().isEmpty()) {
            newUser.setDisplayName(newUser.getUsername());
        }

        if (newUser.getRoles() == null || newUser.getRoles().length == 0) {
            newUser.addRole("user");
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        User user = new User();
        user.setUsername(newUser.getUsername());
        user.setEmail(newUser.getEmail());
        user.setPassword(newUser.getPassword());
        user.setDisplayName(newUser.getDisplayName());
        user.setEmailVerified(false);
        user.setRoles(newUser.getRoles());
        user.setExtendedData(newUser.getExtendedData());

        User savedUser = userService.registerUser(user);

        return ResponseEntity.ok(savedUser);
    }

    // @PostMapping("/reset-password")
    // public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> requestBody) {
    //     String email = requestBody.get("email");
    //     String oldPassword = requestBody.get("oldPassword");
    //     String newPassword = requestBody.get("password");

    //     User user = userRepository.findByEmail(email).orElse(null);

    //     if (user == null) {
    //         return ResponseEntity.status(404).body(Map.of("error", "User not found"));
    //     }

    //     if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
    //         return ResponseEntity.status(401).body(Map.of("error", "Old password is incorrect"));
    //     }

    //     user.setPassword(passwordEncoder.encode(newPassword));
    //     userRepository.save(user);

    //     return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
    // }
}
