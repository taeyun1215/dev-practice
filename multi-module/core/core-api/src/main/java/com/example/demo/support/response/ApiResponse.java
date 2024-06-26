package com.example.demo.support.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class ApiResponse {
	protected int status;
	protected String message;
}