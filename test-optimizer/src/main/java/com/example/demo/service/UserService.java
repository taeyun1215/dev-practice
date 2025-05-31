package com.example.demo.service;

import com.example.demo.controller.payload.UserRequest;
import com.example.demo.controller.payload.UserResponse;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse createUser(UserRequest request) {
        User user = new User(request.getName(), request.getEmail());
        return new UserResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponse findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return new UserResponse(user);
    }
}