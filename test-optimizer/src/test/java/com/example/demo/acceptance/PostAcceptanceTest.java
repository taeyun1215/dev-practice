package com.example.demo.acceptance;

import com.example.demo.controller.payload.PostRequest;
import com.example.demo.controller.payload.PostResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PostAcceptanceTest extends AcceptanceTest {

    @Test
    void createPost() {
        dataSourceSelector.toWrite();
        ExtractableResponse<Response> response = createPost("My Post", "Content");

        assertThat(response.statusCode()).isEqualTo(201);
    }

    @Test
    void findPost() {
        dataSourceSelector.toRead();
        PostResponse postResponse = findPostById(1L);

        assertThat(postResponse.getTitle()).isEqualTo("My Post");
    }

    private ExtractableResponse<Response> createPost(String title, String content) {
        PostRequest request = new PostRequest(title, content);

        return RestAssured.given().log().all()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/posts")
                .then()
                .extract();
    }

    private PostResponse findPostById(Long id) {
        return RestAssured.given().log().all()
                .contentType("application/json")
                .when()
                .get("/posts/" + id)
                .then()
                .statusCode(200)
                .extract()
                .as(PostResponse.class);
    }
}