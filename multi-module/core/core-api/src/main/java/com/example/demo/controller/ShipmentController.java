package com.example.demo.controller;

import com.example.demo.Shipment;
import com.example.demo.controller.payload.GetShipmentDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
	public SuccessApiResponse<String> createShipment(
		@RequestBody CreateShipmentDto.CreateShipmentRequest request
	) {
		shipmentService.createShipment(request);
		return SuccessApiResponse.of(HttpStatus.CREATED.value(), "추가가 완료되었습니다.");
	}

	@GetMapping("/{shipmentId}")
	public SuccessApiResponse<GetShipmentDto.GetShipmentDtoResponse> getShipment(
			@PathVariable Long shipmentId
	) {
		Shipment shipment = shipmentService.getShipmentById(shipmentId);
		GetShipmentDto.GetShipmentDtoResponse response = new GetShipmentDto.GetShipmentDtoResponse(
				shipment.getTrackingNumber(),
				shipment.getStatus(),
				shipment.getCreatedAt(),
				shipment.getUpdatedAt()
		);
		return SuccessApiResponse.of(HttpStatus.OK.value(), response);
	}
}