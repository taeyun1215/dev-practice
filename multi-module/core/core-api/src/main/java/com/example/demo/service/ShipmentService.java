package com.example.demo.service;

import com.example.demo.support.error.ShipmentNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.NotificationRequest;
import com.example.demo.NotificationServiceClient;
import com.example.demo.Shipment;
import com.example.demo.ShipmentRepository;
import com.example.demo.ShipmentStatus;
import com.example.demo.controller.payload.CreateShipmentDto;
import com.example.demo.support.error.InvalidShipmentRequestException;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShipmentService {

	private final ShipmentRepository shipmentRepository;
	private final NotificationServiceClient notificationServiceClient;

	@Transactional
	public void createShipment(CreateShipmentDto.CreateShipmentRequest request) {
		if (request.trackingNumber() == null || request.trackingNumber().isEmpty()) {
			throw new InvalidShipmentRequestException("Invalid or missing tracking number.");
		}

		Shipment shipment = Shipment.builder()
				.trackingNumber(request.trackingNumber())
				.status(ShipmentStatus.PENDING)
				.build();

		Shipment savedShipment = shipmentRepository.save(shipment);
		sendShipmentOrderConfirmation(request.email(), savedShipment.getId());
	}

	// 배송 주문 완료 알림 발송
	private void sendShipmentOrderConfirmation(String userEmail, Long shipmentId) {
		String message = String.format("Your shipment order has been placed successfully. Your shipment ID is %d.", shipmentId);
		NotificationRequest notificationRequest = new NotificationRequest(userEmail, message);
		notificationServiceClient.sendNotification(notificationRequest);
	}

	@Transactional(readOnly = true)
	public Shipment getShipmentById(Long shipmentId) {
		return shipmentRepository.findById(shipmentId)
				.orElseThrow(() -> new ShipmentNotFoundException(shipmentId));
	}
}
