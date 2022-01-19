package com.demo.learning.saga.PaymentService.command.api.aggregate;

import com.demo.learning.saga.CommonService.command.CancelPaymentCommand;
import com.demo.learning.saga.CommonService.command.ValidatePaymentCommand;
import com.demo.learning.saga.CommonService.event.PaymentCancelledEvent;
import com.demo.learning.saga.CommonService.event.PaymentProcessedEvent;
import com.demo.learning.saga.CommonService.model.CardDetails;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@Slf4j
public class PaymentAggregate {

    @AggregateIdentifier
    private String paymentId;
    private String orderId;
    private CardDetails cardDetails;

    public PaymentAggregate() {

    }
    @CommandHandler
    public PaymentAggregate(ValidatePaymentCommand validatePaymentCommand) {
        // validate command
        log.info("Executing ValidatePaymentCommand for order id : {} and payment id: {}"
                , validatePaymentCommand.getOrderId(),
                validatePaymentCommand.getPaymentId());
        PaymentProcessedEvent paymentProcessedEvent = PaymentProcessedEvent
                .builder()
                .orderId(validatePaymentCommand.getOrderId())
                .paymentId(validatePaymentCommand.getPaymentId())
                .cardDetails(validatePaymentCommand.getCardDetails())
                .build();
        AggregateLifecycle.apply(paymentProcessedEvent);
    }

    @CommandHandler
    public void on(CancelPaymentCommand cancelPaymentCommand) {
        log.info("Executing ValidatePaymentCommand for order id : {} and payment id: {}"
                , cancelPaymentCommand.getOrderId(),
                cancelPaymentCommand.getPaymentId());
        PaymentCancelledEvent paymentCancelledEvent = new PaymentCancelledEvent();

        BeanUtils.copyProperties(cancelPaymentCommand, paymentCancelledEvent);
        AggregateLifecycle.apply(paymentCancelledEvent);
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent event) {
        this.orderId = event.getOrderId();
        this.paymentId = event.getPaymentId();
        this.cardDetails = event.getCardDetails();
    }

    @EventSourcingHandler
    public void on(PaymentCancelledEvent event) {
        this.orderId = event.getOrderId();
        this.paymentId = event.getPaymentId();
    }

}
