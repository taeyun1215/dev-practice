package com.example.demo.example;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.global.annotation.SlackNotification;

@RestControllerAdvice
public class ExampleExceptionAdvice {

	@SlackNotification
	@ExceptionHandler(ExampleException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleExampleException(ExampleException ex) {
		return "Access denied due to invalid credentials: " + ex.getMessage();
	}
}
