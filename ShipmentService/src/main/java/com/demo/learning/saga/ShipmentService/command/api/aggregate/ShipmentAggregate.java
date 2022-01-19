package com.demo.learning.saga.ShipmentService.command.api.aggregate;

import com.demo.learning.saga.CommonService.command.CancelShipmentCommand;
import com.demo.learning.saga.CommonService.command.OrderShipmentCommand;
import com.demo.learning.saga.CommonService.event.OrderShippedEvent;
import com.demo.learning.saga.CommonService.event.ShipmentCancelledEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@Slf4j
public class ShipmentAggregate {
    @AggregateIdentifier
    private String shipmentId;
    private String orderId;
    private String paymentId;
    private String shipmentStatus;

   public ShipmentAggregate() {

    }

    @CommandHandler
    public ShipmentAggregate(OrderShipmentCommand orderShipmentCommand) {
        // validate command
        log.info("Executing OrderShipmentCommand for order id : {} and shipment id: {}"
                , orderShipmentCommand.getOrderId(),
                orderShipmentCommand.getShipmentId());
        OrderShippedEvent orderShippedEvent = OrderShippedEvent
                .builder()
                .orderId(orderShipmentCommand.getOrderId())
                .shipmentId(orderShipmentCommand.getShipmentId())
                .paymentId(orderShipmentCommand.getPaymentId())
                .shipmentStatus("COMPLETED")
                .build();
        AggregateLifecycle.apply(orderShippedEvent);
    }

    @CommandHandler
    public void on(CancelShipmentCommand cancelShipmentCommand) {
        log.info("Executing CancelShipmentCommand for order id : {} and shipment id: {}"
                , cancelShipmentCommand.getOrderId(),
                cancelShipmentCommand.getShipmentId());
        ShipmentCancelledEvent shipmentCancelledEvent = new ShipmentCancelledEvent();
        BeanUtils.copyProperties(cancelShipmentCommand, shipmentCancelledEvent);
        AggregateLifecycle.apply(shipmentCancelledEvent);
    }

    @EventSourcingHandler
    public void on(OrderShippedEvent event) {
        log.info("Handling OrderShippedEvent for order id : {} and shipment id: {}"
                , event.getOrderId(),
                event.getShipmentId());
       this.orderId = event.getOrderId();
       this.shipmentId = event.getShipmentId();
       this.paymentId = event.getPaymentId();
    }

    @EventSourcingHandler
    public void on(ShipmentCancelledEvent event) {
        log.info("Handling ShipmentCancelledEvent for order id : {} and shipment id: {}"
                , event.getOrderId(),
                event.getShipmentId());
        this.orderId = event.getOrderId();
        this.shipmentId = event.getShipmentId();
        this.paymentId = event.getPaymentId();
        this.shipmentStatus = event.getPaymentStatus();
    }
}
