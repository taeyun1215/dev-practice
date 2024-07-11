package com.example.demo.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.demo.config.AppConfig;
import com.example.demo.model.ApiGatewayRequest;
import com.example.demo.model.ApiGatewayResponse;
import com.example.demo.user.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class LoginUserHandler implements RequestHandler<ApiGatewayRequest, ApiGatewayResponse> {

    private final Gson gson = new Gson();
    private final UserService userService;

    public LoginUserHandler() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.userService = context.getBean(UserService.class);
    }

    @Override
    public ApiGatewayResponse handleRequest(ApiGatewayRequest request, Context context) {
        JsonObject body = gson.fromJson(request.getBody(), JsonObject.class);
        String username = body.get("username").getAsString();
        String password = body.get("password").getAsString();

        if (userService.validatePassword(username, password)) {
            return new ApiGatewayResponse(200, "Login successful");
        } else {
            return new ApiGatewayResponse(401, "Invalid credentials");
        }
    }
}