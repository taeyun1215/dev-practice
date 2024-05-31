package com.example.demo.controller.payload;

public class CreateShipmentDto {

	public record CreateShipmentRequest(
		String trackingNumber,
		String email
	) {
	}
}
