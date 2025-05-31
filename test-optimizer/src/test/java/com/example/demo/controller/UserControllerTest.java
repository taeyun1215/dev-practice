package com.example.demo.controller;


import com.example.demo.controller.payload.UserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class UserControllerTest extends ControllerTest {

    @Test
    void 유저_생성() throws Exception {
        // given
        UserRequest request = new UserRequest("Charlie", "charlie@example.com");

        // when & then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Charlie"))
                .andExpect(jsonPath("$.email").value("charlie@example.com"));
    }

    @Test
    void 유저_조회() throws Exception {
        // given
        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
