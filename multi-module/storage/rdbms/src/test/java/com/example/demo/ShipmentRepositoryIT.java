package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ShipmentRepositoryIT extends RdbmsContextTest {

	@Autowired
	private ShipmentRepository shipmentRepository;

	@Test
	public void testShipmentCanBeSavedAndFound() {
		// 테스트용 Shipment 객체 생성 및 저장
		String trackingNumber = "TRACK123";
		String status = "Delivered";
		Shipment newShipment = Shipment.builder()
				.trackingNumber(trackingNumber)
				.status(status)
				.build();
		Shipment savedShipment = shipmentRepository.save(newShipment);

		// 저장 확인
		assertNotNull(savedShipment);
		assertNotNull(savedShipment.getId());
		assertEquals(trackingNumber, savedShipment.getTrackingNumber());
		assertEquals(status, savedShipment.getStatus());

		// 저장된 Shipment을 추적 번호로 조회
		Shipment foundShipment = shipmentRepository.findByTrackingNumber(trackingNumber);
		assertNotNull(foundShipment);
		assertEquals(savedShipment.getId(), foundShipment.getId());
		assertEquals(savedShipment.getTrackingNumber(), foundShipment.getTrackingNumber());
		assertEquals(savedShipment.getStatus(), foundShipment.getStatus());
	}
}