package com.example.demo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam String username, @RequestParam String password) {
        userService.registerUser(username, password);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
        boolean isAuthenticated = userService.loginUser(username, password);
        if (isAuthenticated) {
            return ResponseEntity.ok("User logged in successfully");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}
