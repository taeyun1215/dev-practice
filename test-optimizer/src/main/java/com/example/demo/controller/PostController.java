package com.example.demo.controller;

import com.example.demo.controller.payload.PostRequest;
import com.example.demo.controller.payload.PostResponse;
import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest request) {
        return ResponseEntity.status(201).body(postService.createPost(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.findPostById(id));
    }
}