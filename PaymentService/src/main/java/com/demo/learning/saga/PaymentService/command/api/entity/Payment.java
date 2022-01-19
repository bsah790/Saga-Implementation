package com.demo.learning.saga.PaymentService.command.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Payment")
public class Payment {
    @Id
    private String paymentId;
    private String orderId;
    private Date timestamp;
    private String paymentStatus;
}
