package com.example.demo.support.error;

public class InvalidShipmentRequestException extends RuntimeException {
	public InvalidShipmentRequestException(String message) {
		super(message);
	}
}