package com.demo.learning.saga.CommonService.command;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class CancelShipmentCommand {
    @TargetAggregateIdentifier
    private String shipmentId;
    private String paymentId;
    private String orderId;
    private String paymentStatus="CANCELLED";
}
