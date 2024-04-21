package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")  // Use application-test.yml configuration
public class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Test
    public void testAccountCreationAndRetrieval() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();

        String accountJson = "{\"owner\":\"John Doe\",\"balance\":1000.0}";

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.owner").value("John Doe"))
                .andExpect(jsonPath("$.balance").value(1000.0));

        mockMvc.perform(get("/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.owner").value("John Doe"))
                .andExpect(jsonPath("$.balance").value(1000.0));
    }
}
