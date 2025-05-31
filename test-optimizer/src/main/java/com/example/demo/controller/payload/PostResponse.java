package com.example.demo.controller.payload;

import com.example.demo.model.Post;
import lombok.Getter;

@Getter
public class PostResponse {

    private Long id;
    private String title;
    private String content;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
