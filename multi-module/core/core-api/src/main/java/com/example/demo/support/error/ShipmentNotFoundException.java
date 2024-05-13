package com.example.demo.support.error;

public class ShipmentNotFoundException extends RuntimeException {
	public ShipmentNotFoundException(String message) {
		super(message);
	}
}