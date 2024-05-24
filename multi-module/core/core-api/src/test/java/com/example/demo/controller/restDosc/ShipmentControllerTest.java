package com.example.demo.controller.restDosc;

import com.example.demo.RestDocsTest;
import com.example.demo.controller.ShipmentController;
import com.example.demo.controller.payload.CreateShipmentDto;
import com.example.demo.service.ShipmentService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.example.demo.RestDocsUtils.requestPreprocessor;
import static com.example.demo.RestDocsUtils.responsePreprocessor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class ShipmentControllerTest extends RestDocsTest {

    private ShipmentService shipmentService;

    @BeforeEach
    public void setUp() {
        shipmentService = mock(ShipmentService.class);
        ShipmentController shipmentController = new ShipmentController(shipmentService);
        mockMvc = mockController(shipmentController);
    }

    @Test
    public void createShipmentTest() {
        CreateShipmentDto.CreateShipmentRequest request = new CreateShipmentDto.CreateShipmentRequest("123456789", "email@example.com");
        Long shipmentId = 1L;
        when(shipmentService.createShipment(any())).thenReturn(shipmentId);

        given().contentType(ContentType.JSON)
                .body(request)
                .post("/api/v1/shipments")
                .then()
                .status(HttpStatus.CREATED)
                .apply(document("create-shipment",
                        requestPreprocessor(),
                        responsePreprocessor(),
                        requestFields(
                                fieldWithPath("trackingNumber").description("The tracking number of the shipment."),
                                fieldWithPath("email").description("The email address associated with the shipment.")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("The HTTP status code of the response."),
                                fieldWithPath("data.shipmentId").type(JsonFieldType.NUMBER).description("The ID of the created shipment.")
                        )));
    }
}
