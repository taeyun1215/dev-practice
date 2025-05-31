package com.example.demo.global.util;

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