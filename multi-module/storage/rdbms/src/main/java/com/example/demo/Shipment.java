package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Setter;

@Entity
@Setter
@Table(name = "shipments")
public class Shipment extends BaseEntity {

	private String trackingNumber;
	private String status;

}