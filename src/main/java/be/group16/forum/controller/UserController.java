package be.group16.forum.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.group16.forum.model.User;
import be.group16.forum.service.UserService;
import be.group16.forum.util.JwtUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    JwtUtil jwtUtil;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable String id,
            @RequestBody User updatedUser,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Missing or invalid Authorization header."));
        }

        String userId = userService.getUserIdFromToken(token);
        if (!id.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "You are not allowed to update this user."));
        }

        User user = userService.findUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found."));
        }

        user.setDisplayName(updatedUser.getDisplayName());
        user.setBio(updatedUser.getBio());
        user.setUrl(updatedUser.getUrl());
        user.setImage(updatedUser.getImage());
        user.setEmail(updatedUser.getEmail());

        userService.updateUser(id, user);
        return ResponseEntity.ok(user);
    }
}
