package com.demo.learning.saga.OrderService.command.api.aggregate;

import com.demo.learning.saga.CommonService.command.CancelOrderCommand;
import com.demo.learning.saga.CommonService.command.CompleteOrderCommand;
import com.demo.learning.saga.CommonService.event.OrderCancelledEvent;
import com.demo.learning.saga.CommonService.event.OrderCompletedEvent;
import com.demo.learning.saga.OrderService.command.api.command.CreateOrderCommand;
import com.demo.learning.saga.OrderService.command.api.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@Slf4j
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private String addressId;
    private Integer quantity;
    private String orderStatus;

    public OrderAggregate(){

    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command){
        // validate command
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        BeanUtils.copyProperties(command, orderCreatedEvent);
        AggregateLifecycle.apply(orderCreatedEvent);
    }

    @CommandHandler
    public void handle(CompleteOrderCommand completeOrderCommand) {
        // validate command
        log.info("Executing CompleteOrderCommand for order id : {} "
                , completeOrderCommand.getOrderId());
        OrderCompletedEvent orderCompletedEvent = OrderCompletedEvent
                .builder()
                .orderId(completeOrderCommand.getOrderId())
                .orderStatus(completeOrderCommand.getOrderStatus())
                .build();
        AggregateLifecycle.apply(orderCompletedEvent);
    }

    @CommandHandler
    public void on(CancelOrderCommand cancelOrderCommand) {
        // validate command
        log.info("Executing CancelOrderCommand for order id : {} "
                , cancelOrderCommand.getOrderId());
        OrderCancelledEvent orderCancelledEvent = new OrderCancelledEvent();
        BeanUtils.copyProperties(cancelOrderCommand, orderCancelledEvent);
        AggregateLifecycle.apply(orderCancelledEvent);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        this.orderId = event.getOrderId();
        this.productId = event.getProductId();
        this.userId = event.getUserId();
        this.addressId = event.getAddressId();
        this.quantity = event.getQuantity();
        this.orderStatus = event.getOrderStatus();
    }

    @EventSourcingHandler
    public void on(OrderCompletedEvent event) {
        this.orderId = event.getOrderId();
        this.orderStatus = event.getOrderStatus();
    }

    @EventSourcingHandler
    public void on(OrderCancelledEvent event) {
        this.orderId = event.getOrderId();
        this.orderStatus = event.getOrderStatus();
    }
}
