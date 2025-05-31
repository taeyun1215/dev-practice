package com.example.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ShipmentRepositoryTest extends RdbmsContextTest {

	@Autowired
	private ShipmentRepository shipmentRepository;

	@Test
	@DisplayName("레파지토리 레이어 saveTest 통합 테스트")
	void saveTest() {
		// Given
		String trackingNumber = "TRACK123";
		ShipmentStatus status = ShipmentStatus.PENDING;
		Shipment newShipment = Shipment.builder()
				.trackingNumber(trackingNumber)
				.status(status)
				.build();

		// When
		Shipment savedShipment = shipmentRepository.save(newShipment);

		// Then
		assertNotNull(savedShipment, "Saved shipment should not be null");
		assertNotNull(savedShipment.getId(), "Saved shipment should have a non-null ID");
		assertEquals(trackingNumber, savedShipment.getTrackingNumber(), "Tracking number should match the input");
		assertEquals(status, savedShipment.getStatus(), "Status should match the input");
	}

	@Test
	@DisplayName("레파지토리 레이어 findByIdTest 통합 테스트")
	void findByIdTest() {
		// Given
		String trackingNumber = "TRACK123";
		ShipmentStatus status = ShipmentStatus.PENDING;
		Shipment newShipment = Shipment.builder()
				.trackingNumber(trackingNumber)
				.status(status)
				.build();
		shipmentRepository.save(newShipment);

		// When
		Shipment foundShipment = shipmentRepository.findByTrackingNumber(trackingNumber);

		// Then (
		assertNotNull(foundShipment, "Should find a shipment by tracking number");
		assertEquals(newShipment.getId(), foundShipment.getId(), "Found shipment ID should match saved shipment's ID");
		assertEquals(newShipment.getTrackingNumber(), foundShipment.getTrackingNumber(), "Found tracking number should match");
		assertEquals(newShipment.getStatus(), foundShipment.getStatus(), "Found status should match");
	}
}