package com.example.demo.controller;

import com.example.demo.controller.payload.PostRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class PostControllerTest extends ControllerTest {

    @Test
    void 게시글_생성() throws Exception {
        // given
        PostRequest request = new PostRequest("New Post", "This is a post");

        // when & then
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Post"))
                .andExpect(jsonPath("$.content").value("This is a post"));
    }

    @Test
    void 게시글_조회() throws Exception {
        // given
        mockMvc.perform(get("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
