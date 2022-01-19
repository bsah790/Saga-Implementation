package com.demo.learning.saga.OrderService.command.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel {
    private String productId;
    private String userId;
    private String addressId;
    private Integer quantity;
}
