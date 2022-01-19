package com.demo.learning.saga.OrderService.command.api.repository;

import com.demo.learning.saga.OrderService.command.api.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}
