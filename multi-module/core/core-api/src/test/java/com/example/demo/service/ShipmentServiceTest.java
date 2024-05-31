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
    @DisplayName("서비스 레이어 createShipment 단위 테스트")
    void createShipmentTest() {
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
    @DisplayName("서비스 레이어 createShipment 단위 테스트 실패 케이스")
    void createShipmentFailTest() {
        // Given
        CreateShipmentDto.CreateShipmentRequest request = new CreateShipmentDto.CreateShipmentRequest("", "user@example.com");

        // When & Then
        assertThrows(InvalidShipmentRequestException.class, () -> shipmentService.createShipment(request));
    }

    @Test
    @DisplayName("서비스 레이어 getShipmentById 단위 테스트")
    void getShipmentByIdTest() {
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
    @DisplayName("서비스 레이어 getShipmentById 단위 테스트 실패 케이스")
    void getShipmentByIdFailTest() {
        // Given
        Long shipmentId = 9999L;
        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ShipmentNotFoundException.class, () -> shipmentService.getShipmentById(shipmentId));
    }
}
