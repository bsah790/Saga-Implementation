package com.demo.learning.saga.CommonService.event;

import lombok.Data;

@Data
public class ShipmentCancelledEvent {
    private String shipmentId;
    private String paymentId;
    private String orderId;
    private String paymentStatus;
}
