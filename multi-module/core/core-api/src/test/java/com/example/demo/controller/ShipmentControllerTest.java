package com.example.demo.controller;

import com.example.demo.Shipment;
import com.example.demo.ShipmentStatus;
import com.example.demo.controller.payload.CreateShipmentDto;
import com.example.demo.service.ShipmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShipmentController.class)
class ShipmentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ShipmentService shipmentService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        // 필요시 사용
    }

    @Test
    void createShipmentTest() throws Exception {
        // Given
        CreateShipmentDto.CreateShipmentRequest createShipmentRequest = new CreateShipmentDto.CreateShipmentRequest("123456789", "user@example.com");
        doNothing().when(shipmentService).createShipment(any(CreateShipmentDto.CreateShipmentRequest.class));

        // When & Then
        mockMvc.perform(post("/api/v1/shipments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createShipmentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("추가가 완료되었습니다."));
    }

    @Test
    void getShipmentTest() throws Exception {
        // Given
        Long shipmentId = 1L;
        Shipment shipment = Shipment.builder()
                .trackingNumber("ABCDEFG")
                .status(ShipmentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(shipmentService.getShipmentById(shipmentId)).thenReturn(shipment);

        // When & Then
        mockMvc.perform(get("/api/v1/shipments/{shipmentId}", shipmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.trackingNumber").value("ABCDEFG"))
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andExpect(jsonPath("$.data.createdAt").exists())
                .andExpect(jsonPath("$.data.updatedAt").exists());
    }
}

