package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.*;
import com.example.demo.controller.payload.CreateShipmentDto;
import com.example.demo.support.error.InvalidShipmentRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class ShipmentServiceTest {

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
        Shipment shipment = Shipment.builder()
                .trackingNumber(request.trackingNumber())
                .status(ShipmentStatus.PENDING.toString())
                .build();
        Shipment savedShipment = Shipment.builder()
                .id(1L)
                .trackingNumber(request.trackingNumber())
                .status(ShipmentStatus.PENDING.toString())
                .build();
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(savedShipment);
        when(notificationServiceClient.sendNotification(any(NotificationRequest.class))).thenReturn(new NotificationResponse(any(String.class)));

        // When
        Long result = shipmentService.createShipment(request);

        // Then
        assertEquals(savedShipment.getId(), result);
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
}
