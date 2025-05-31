package com.example.demo.controller.payload;

import com.example.demo.ShipmentStatus;

import java.time.LocalDateTime;

public class GetShipmentDto {

    public record GetShipmentDtoResponse(
            String trackingNumber,
            ShipmentStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
    }
}
