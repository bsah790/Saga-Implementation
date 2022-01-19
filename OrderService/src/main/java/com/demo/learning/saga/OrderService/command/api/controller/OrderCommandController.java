package com.demo.learning.saga.OrderService.command.api.controller;

import com.demo.learning.saga.OrderService.command.api.command.CreateOrderCommand;
import com.demo.learning.saga.OrderService.command.api.model.OrderModel;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderCommandController {
    @Autowired
    private CommandGateway commandGateway;

    @PostMapping(value = "/create")
    public String createOrder(@RequestBody OrderModel orderModel) {
        CreateOrderCommand createOrderCommand = CreateOrderCommand.
                builder()
                .orderId(UUID.randomUUID().toString())
                .productId(orderModel.getProductId())
                .userId(orderModel.getUserId())
                .addressId(orderModel.getAddressId())
                .quantity(orderModel.getQuantity())
                .orderStatus("CREATED")
                .build();
        commandGateway.sendAndWait(createOrderCommand);
        return "Order Created";
    }
}
