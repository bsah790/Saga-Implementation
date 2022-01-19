package com.demo.learning.saga.OrderService.command.api.event;

import com.demo.learning.saga.CommonService.event.OrderCancelledEvent;
import com.demo.learning.saga.CommonService.event.OrderCompletedEvent;
import com.demo.learning.saga.OrderService.command.api.entity.Order;
import com.demo.learning.saga.OrderService.command.api.repository.OrderRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {

    @Autowired
    private OrderRepository orderRepository;

    @EventHandler
    public void handle(OrderCreatedEvent event) {
        Order order = new Order();
        BeanUtils.copyProperties(event, order);
        orderRepository.save(order);
    }

    @EventHandler
    public void handle(OrderCompletedEvent event) {
        Order order = orderRepository.getById(event.getOrderId());
        order.setOrderStatus(event.getOrderStatus());
        orderRepository.save(order);
    }

    @EventHandler
    public void handle(OrderCancelledEvent event) {
        Order order = orderRepository.getById(event.getOrderId());
        order.setOrderStatus(event.getOrderStatus());
        orderRepository.save(order);
    }
}
