package com.demo.learning.saga.ShipmentService.command.api.event;

import com.demo.learning.saga.CommonService.event.OrderShippedEvent;
import com.demo.learning.saga.CommonService.event.ShipmentCancelledEvent;
import com.demo.learning.saga.ShipmentService.command.api.entity.Shipment;
import com.demo.learning.saga.ShipmentService.command.api.repository.ShipmentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderShippedEventHandler {

    @Autowired
    private ShipmentRepository repository;

    @EventHandler
    public void handle(OrderShippedEvent event) {
        Shipment shipment = new Shipment();
        shipment.setShipmentId(event.getShipmentId());
        shipment.setOrderId(event.getOrderId());
        shipment.setShipmentStatus(event.getShipmentStatus());
        repository.save(shipment);
    }

    @EventHandler
    public void handle(ShipmentCancelledEvent event) {
        Shipment shipment = repository.findById(event.getShipmentId()).get();
        shipment.setShipmentStatus(event.getPaymentStatus());
        repository.save(shipment);
    }
}
