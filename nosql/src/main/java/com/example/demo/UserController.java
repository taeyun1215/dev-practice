package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserRepo userRepo;

    @GetMapping
    public List<User> getPeople() {
        return userRepo.findAll();
    }

    @PostMapping
    public User addPerson(@RequestBody User user) {
        return userRepo.save(user);
    }
}