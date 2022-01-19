package com.demo.learning.saga.OrderService.command.api.saga;

import com.demo.learning.saga.CommonService.command.*;
import com.demo.learning.saga.CommonService.event.*;
import com.demo.learning.saga.CommonService.model.User;
import com.demo.learning.saga.CommonService.query.GetUserDetailsQuery;
import com.demo.learning.saga.OrderService.command.api.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Executable;
import java.util.UUID;

@Saga
@Slf4j
public class OrderProcessingSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent event) {
        log.info("OrderCreatedEvent in saga for order id : {}", event.getOrderId());

        GetUserDetailsQuery getUserDetailsQuery = GetUserDetailsQuery
                .builder()
                .userId(event.getUserId())
                .build();
        User user = null;
        try {
            user = queryGateway.query(getUserDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
        } catch (Exception e) {
            log.info(e.getMessage());
            //start the compensate transaction
            createCancelOrderCommand(event.getOrderId());
        }
        ValidatePaymentCommand validatePaymentCommand = ValidatePaymentCommand
                .builder()
                .paymentId(UUID.randomUUID().toString())
                .orderId(event.getOrderId())
                .cardDetails(user.getCardDetails())
                .build();
        commandGateway.sendAndWait(validatePaymentCommand);
    }

    private void createCancelOrderCommand(String orderId) {
        CancelOrderCommand cancelOrderCommand = new CancelOrderCommand(orderId);
        commandGateway.send(cancelOrderCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent event) {
        log.info("PaymentProcessedEvent in saga for order id : {}", event.getOrderId());
        try{
            OrderShipmentCommand orderShipmentCommand = OrderShipmentCommand
                    .builder()
                    .shipmentId(UUID.randomUUID().toString())
                    .orderId(event.getOrderId())
                    .paymentId(event.getPaymentId())
                    .build();
            commandGateway.sendAndWait(orderShipmentCommand);

        } catch (Exception e) {
            log.error(e.getMessage());
            //start the compensate transaction
            createCancelPaymentCommand(event.getPaymentId(), event.getOrderId());
        }
    }

    private void createCancelPaymentCommand(String paymentId, String orderId) {
        CancelPaymentCommand cancelPaymentCommand = new CancelPaymentCommand(paymentId,orderId);
        commandGateway.send(cancelPaymentCommand);
    }


    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderShippedEvent event) {
        log.info("OrderShippedEvent in saga for order id : {}", event.getOrderId());
        try{
            if(true)
                throw new Exception("Test Exception");
            CompleteOrderCommand completeOrderCommand =  CompleteOrderCommand
                    .builder()
                    .orderId(event.getOrderId())
                    .orderStatus("APPROVED")
                    .build();
            commandGateway.send(completeOrderCommand);
        } catch (Exception e) {
            log.error(e.getMessage());
            //start the compensate transaction
            createCancelShipmentCommand(event);
        }
    }

    private void createCancelShipmentCommand(OrderShippedEvent event) {
        CancelShipmentCommand cancelShipmentCommand =
                new CancelShipmentCommand(event.getShipmentId(), event.getPaymentId(), event.getOrderId());
        commandGateway.send(cancelShipmentCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCompletedEvent event) {
        log.info("OrderCompletedEvent in saga for order id : {}", event.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void handle(OrderCancelledEvent event) {
        log.info("OrderCancelledEvent in saga for order id : {}", event.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentCancelledEvent event) {
        log.info("PaymentCancelledEvent in saga for order id : {}", event.getOrderId());
        createCancelOrderCommand(event.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ShipmentCancelledEvent event) {
        log.info("ShipmentCancelledEvent in saga for order id : {}", event.getOrderId());
        createCancelPaymentCommand(event.getPaymentId(), event.getOrderId());
    }
}
