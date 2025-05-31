package com.example.demo.controller.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequest {

    private String title;
    private String content;

    public PostRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
