package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.*;
import com.example.demo.controller.payload.CreateShipmentDto;
import com.example.demo.support.error.InvalidShipmentRequestException;
import com.example.demo.support.error.ShipmentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @InjectMocks
    private ShipmentService shipmentService;

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private NotificationServiceClient notificationServiceClient;

    @BeforeEach
    void setup() {
        // 필요시 사용
    }

    @Test
    @DisplayName("유효한 요청으로 배송 생성 시 배송 ID 반환")
    void 유효한_요청으로_배송_생성_시_배송_ID_반환() {
        // Given
        CreateShipmentDto.CreateShipmentRequest request = new CreateShipmentDto.CreateShipmentRequest("123456789", "user@example.com");
        Shipment savedShipment = Shipment.builder()
                .id(1L)
                .trackingNumber(request.trackingNumber())
                .status(ShipmentStatus.PENDING)
                .build();
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(savedShipment);
        when(notificationServiceClient.sendNotification(any(NotificationRequest.class))).thenReturn(new NotificationResponse(any(String.class)));

        // When
        shipmentService.createShipment(request);

        // Then
        verify(shipmentRepository).save(any(Shipment.class));
        verify(notificationServiceClient).sendNotification(any(NotificationRequest.class));

    }

    @Test
    @DisplayName("트래킹 번호 누락 시 예외 발생")
    void 트래킹_번호_누락_시_예외_발생() {
        // Given
        CreateShipmentDto.CreateShipmentRequest request = new CreateShipmentDto.CreateShipmentRequest("", "user@example.com");

        // When & Then
        assertThrows(InvalidShipmentRequestException.class, () -> shipmentService.createShipment(request));
    }

    @Test
    @DisplayName("ID로 배송 조회 시 정확한 배송 정보 반환")
    void ID로_배송_조회_시_정확한_배송_정보_반환() {
        // Given
        Long shipmentId = 1L;
        Shipment expectedShipment = Shipment.builder()
                .id(shipmentId)
                .trackingNumber("123456789")
                .status(ShipmentStatus.DELIVERED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.of(expectedShipment));

        // When
        Shipment actualShipment = shipmentService.getShipmentById(shipmentId);

        // Then
        assertEquals(expectedShipment, actualShipment);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 배송 조회 시 예외 발생")
    void 존재하지_않는_ID로_배송_조회_시_예외_발생() {
        // Given
        Long shipmentId = 9999L;
        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ShipmentNotFoundException.class, () -> shipmentService.getShipmentById(shipmentId));
    }
}
