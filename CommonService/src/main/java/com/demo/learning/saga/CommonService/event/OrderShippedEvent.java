package com.demo.learning.saga.CommonService.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderShippedEvent {
    private String shipmentId;
    private String orderId;
    private String paymentId;
    private String shipmentStatus;
}
