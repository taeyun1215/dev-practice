package com.example.demo.controller;

import com.example.demo.support.response.ErrorApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ShipmentControllerAdvice {

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorApiResponse handleEmailNotFoundException(Exception ex) {
		return ErrorApiResponse.of(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
	}

}
