package com.demo.learning.saga.ShipmentService.command.api.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Shipment")
public class Shipment {
    @Id
    private String shipmentId;
    private String orderId;
    private String shipmentStatus;
}
