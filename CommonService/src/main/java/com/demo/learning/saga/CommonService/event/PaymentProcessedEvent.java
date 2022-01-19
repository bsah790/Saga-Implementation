package com.demo.learning.saga.CommonService.event;

import com.demo.learning.saga.CommonService.model.CardDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentProcessedEvent {
    private String paymentId;
    private String orderId;
    private CardDetails cardDetails;
}
