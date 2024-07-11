package com.example.demo.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.demo.config.AppConfig;
import com.example.demo.model.ApiGatewayRequest;
import com.example.demo.model.ApiGatewayResponse;
import com.example.demo.user.User;
import com.example.demo.user.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class RegisterUserHandler implements RequestHandler<ApiGatewayRequest, ApiGatewayResponse> {

    private final Gson gson = new Gson();
    private final UserService userService;

    public RegisterUserHandler() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.userService = context.getBean(UserService.class);
    }

    @Override
    public ApiGatewayResponse handleRequest(ApiGatewayRequest request, Context context) {
        JsonObject body = gson.fromJson(request.getBody(), JsonObject.class);
        User user = gson.fromJson(body, User.class);
        userService.registerUser(user);
        return new ApiGatewayResponse(200, "User registered successfully");
    }
}