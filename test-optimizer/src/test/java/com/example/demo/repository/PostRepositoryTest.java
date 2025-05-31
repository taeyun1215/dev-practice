package com.example.demo.repository;

import com.example.demo.model.Post;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class PostRepositoryTest extends RepositoryTest {

    @Test
    void 게시글_저장() {
        // given
        Post post = new Post("Post Title", "Post Content");

        // when
        Post savedPost = postRepository.save(post);

        // then
        assertThat(savedPost.getId()).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo("Post Title");
    }

    @Test
    void 게시글_조회() {
        // given
        Post post = postRepository.save(new Post("Another Post", "More Content"));

        // when
        Post foundPost = postRepository.findById(post.getId()).orElseThrow();

        // then
        assertThat(foundPost.getTitle()).isEqualTo("Another Post");
        assertThat(foundPost.getContent()).isEqualTo("More Content");
    }
}