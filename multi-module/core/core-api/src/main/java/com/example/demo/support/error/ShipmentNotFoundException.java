package com.example.demo.support.error;

public class ShipmentNotFoundException extends RuntimeException {
    public ShipmentNotFoundException(Long shipmentId) {
        super("Shipment with ID " + shipmentId + " not found");
    }
}
