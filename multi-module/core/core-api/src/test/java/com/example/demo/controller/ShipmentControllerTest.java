package com.example.demo.controller;

import com.example.demo.controller.payload.CreateShipmentDto;
import com.example.demo.service.ShipmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShipmentController.class)
public class ShipmentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ShipmentService shipmentService;

	@Test
	public void createShipmentTest() throws Exception {
		// Given
		String trackingNumber = "123456789";
		String email = "example@example.com";
		Long expectedShipmentId = 1L;

		CreateShipmentDto.CreateShipmentRequest request = new CreateShipmentDto.CreateShipmentRequest(trackingNumber, email);
		when(shipmentService.createShipment(any(CreateShipmentDto.CreateShipmentRequest.class))).thenReturn(expectedShipmentId);

		// When & Then
		mockMvc.perform(post("/api/v1/shipments")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"trackingNumber\":\"" + trackingNumber + "\", \"email\":\"" + email + "\"}"))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.status").value(201))
			.andExpect(jsonPath("$.data.shipmentId").value(expectedShipmentId));
	}
}
