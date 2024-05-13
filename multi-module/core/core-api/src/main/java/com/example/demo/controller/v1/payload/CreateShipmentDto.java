package com.example.demo.controller.v1.payload;

public class CreateShipmentDto {

	public record CreateShipmentRequest(
		String trackingNumber,
		String email
	) {
	}

	public record CreateShipmentResponse(
		Long shipmentId
	) {
	}
}
