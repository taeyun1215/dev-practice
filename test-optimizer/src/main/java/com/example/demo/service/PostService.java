package com.example.demo.service;

import com.example.demo.controller.payload.PostRequest;
import com.example.demo.controller.payload.PostResponse;
import com.example.demo.model.Post;
import com.example.demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public PostResponse createPost(PostRequest request) {
        Post post = new Post(request.getTitle(), request.getContent());
        return new PostResponse(postRepository.save(post));
    }

    @Transactional(readOnly = true)
    public PostResponse findPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
        return new PostResponse(post);
    }
}