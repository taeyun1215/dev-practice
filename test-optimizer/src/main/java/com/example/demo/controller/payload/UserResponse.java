package com.example.demo.controller.payload;

import com.example.demo.model.User;
import lombok.Getter;

@Getter
public class UserResponse {

    private Long id;
    private String name;
    private String email;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}