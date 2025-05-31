package com.example.demo.controller.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequest {

    private String name;
    private String email;

    public UserRequest(String name, String email) {
        this.name = name;
        this.email = email;
    }
}