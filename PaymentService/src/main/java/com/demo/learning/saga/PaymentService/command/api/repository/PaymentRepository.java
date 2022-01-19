package com.demo.learning.saga.PaymentService.command.api.repository;

import com.demo.learning.saga.PaymentService.command.api.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
}
