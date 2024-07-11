package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiGatewayResponse {
    private int statusCode;
    private String body;
    private Map<String, String> headers;
    private boolean isBase64Encoded;

    public ApiGatewayResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }
}