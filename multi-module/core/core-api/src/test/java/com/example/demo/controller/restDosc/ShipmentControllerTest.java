package com.example.demo.controller.restDosc;

import com.example.demo.RestDocsTest;
import com.example.demo.Shipment;
import com.example.demo.ShipmentStatus;
import com.example.demo.controller.ShipmentController;
import com.example.demo.controller.payload.CreateShipmentDto;
import com.example.demo.service.ShipmentService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;

import static com.example.demo.RestDocsUtils.requestPreprocessor;
import static com.example.demo.RestDocsUtils.responsePreprocessor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

class ShipmentControllerTest extends RestDocsTest {

    private ShipmentService shipmentService;

    @BeforeEach
    public void setUp() {
        shipmentService = mock(ShipmentService.class);
        ShipmentController shipmentController = new ShipmentController(shipmentService);
        mockMvc = mockController(shipmentController);
    }

    @Test
    void createShipmentTest() {
        CreateShipmentDto.CreateShipmentRequest createShipmentRequest = new CreateShipmentDto.CreateShipmentRequest("123456789", "email@example.com");

        given().contentType(ContentType.JSON)
                .body(createShipmentRequest)
                .post("/api/v1/shipments")
                .then()
                .status(HttpStatus.OK)
                .apply(document("create-shipment",
                        requestPreprocessor(),
                        responsePreprocessor(),
                        requestFields(
                                fieldWithPath("trackingNumber").description("The tracking number of the shipment."),
                                fieldWithPath("email").description("The email address associated with the shipment.")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP status code"),
                                fieldWithPath("data").type(JsonFieldType.NULL).ignored(),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("A message about the response")
                        )));
    }

    @Test
    void getShipmentTest() {
        Shipment shipment = Shipment.builder()
                .trackingNumber("ABCDEFG")
                .status(ShipmentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(shipmentService.getShipmentById(any(Long.class))).thenReturn(shipment);

        given().contentType(ContentType.JSON)
                .get("/api/v1/shipments/{shipmentId}", "1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .apply(document("get-shipment",
                        requestPreprocessor(),
                        responsePreprocessor(),
                        pathParameters(
                                parameterWithName("shipmentId").description("The ID of the shipment to retrieve")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP status code"),
                                fieldWithPath("data.trackingNumber").type(JsonFieldType.STRING).description("The tracking number of the shipment"),
                                fieldWithPath("data.status").type(JsonFieldType.STRING).description("The current status of the shipment"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("The creation date and time of the shipment"),
                                fieldWithPath("data.updatedAt").type(JsonFieldType.STRING).description("The last update date and time of the shipment"),
                                fieldWithPath("message").type(JsonFieldType.NULL).ignored()
                        )));
    }
}
