package com.example.demo.controller;

import com.example.demo.support.error.InvalidShipmentRequestException;
import com.example.demo.support.error.ShipmentNotFoundException;
import com.example.demo.support.response.ErrorApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ShipmentControllerAdvice {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidShipmentRequestException.class)
	public ErrorApiResponse handleInvalidShipmentRequest(InvalidShipmentRequestException ex) {
		return ErrorApiResponse.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ShipmentNotFoundException.class)
	public ErrorApiResponse handleShipmentNotFound(ShipmentNotFoundException ex) {
		return ErrorApiResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage());
	}
}
