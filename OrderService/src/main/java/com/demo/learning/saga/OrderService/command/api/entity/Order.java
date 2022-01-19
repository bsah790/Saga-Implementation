package com.demo.learning.saga.OrderService.command.api.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Order1")
public class Order {
    @Id
    private String orderId;
    private String productId;
    private String userId;
    private String addressId;
    private Integer quantity;
    private String orderStatus;
}
