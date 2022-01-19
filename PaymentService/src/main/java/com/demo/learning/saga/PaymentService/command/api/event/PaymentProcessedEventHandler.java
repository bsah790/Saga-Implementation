package com.demo.learning.saga.PaymentService.command.api.event;

import com.demo.learning.saga.CommonService.event.PaymentCancelledEvent;
import com.demo.learning.saga.CommonService.event.PaymentProcessedEvent;
import com.demo.learning.saga.PaymentService.command.api.entity.Payment;
import com.demo.learning.saga.PaymentService.command.api.repository.PaymentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PaymentProcessedEventHandler {

    @Autowired
    private PaymentRepository paymentRepository;

    @EventHandler
    public void handle(PaymentProcessedEvent event) {
        Payment payment = Payment
                .builder()
                .orderId(event.getOrderId())
                .paymentId(event.getPaymentId())
                .paymentStatus("COMPLETED")
                .timestamp(new Date())
                .build();
        paymentRepository.save(payment);
    }

    @EventHandler
    public void handle(PaymentCancelledEvent event) {
        Payment payment = paymentRepository.findById(event.getPaymentId()).get();
        payment.setPaymentStatus(event.getPaymentStatus());
        payment.setTimestamp(new Date());
        paymentRepository.save(payment);
    }

}
