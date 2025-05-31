package com.example.demo.acceptance;

import com.example.demo.controller.payload.UserRequest;
import com.example.demo.controller.payload.UserResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AcceptanceTest {

    @Test
    void createUserTest() {
        dataSourceSelector.toWrite();
        ExtractableResponse<Response> response = createUser("testUser", "testEmail@example.com");

        assertThat(response.statusCode()).isEqualTo(201);
    }

    @Test
    void findUserTest() {
        dataSourceSelector.toRead();
        UserResponse userResponse = findUserById(1L);

        assertThat(userResponse.getName()).isEqualTo("Alice");
        assertThat(userResponse.getEmail()).isEqualTo("alice@example.com");
    }

    private ExtractableResponse<Response> createUser(String name, String email) {
        UserRequest request = new UserRequest(name, email);

        return RestAssured.given().log().all()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/users")
                .then()
                .extract();
    }

    private UserResponse findUserById(Long id) {
        return RestAssured.given().log().all()
                .contentType("application/json")
                .when()
                .get("/users/" + id)
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponse.class);
    }
}