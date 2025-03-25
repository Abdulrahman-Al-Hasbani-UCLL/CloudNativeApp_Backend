package be.group16.forum.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.group16.forum.model.User;
import be.group16.forum.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/users")
public class UsersController {
    private UserRepository userRepository;

    @GetMapping
    public User[] getAllUsers() {
        List<User> userList = new ArrayList<>();
        userRepository.findAll().forEach(userList::add);
        return userList.toArray(new User[0]);
    }
}
