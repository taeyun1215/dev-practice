package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.payload.CreateShipmentDto;
import com.example.demo.service.ShipmentService;
import com.example.demo.support.response.SuccessApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shipments")
public class ShipmentController {

	private final ShipmentService shipmentService;

	@PostMapping
	public SuccessApiResponse<CreateShipmentDto.CreateShipmentResponse> createShipment(
		@RequestBody CreateShipmentDto.CreateShipmentRequest request
	) {
		Long shipmentId = shipmentService.createShipment(request);
		CreateShipmentDto.CreateShipmentResponse response = new CreateShipmentDto.CreateShipmentResponse(shipmentId);
		return SuccessApiResponse.of(HttpStatus.CREATED.value(), response);
	}
}