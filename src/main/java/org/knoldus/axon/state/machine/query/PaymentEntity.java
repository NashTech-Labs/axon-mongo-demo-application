package org.knoldus.axon.state.machine.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.knoldus.axon.state.machine.aggregate.PaymentState;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@JsonSerialize
public class PaymentEntity {

    @Id
    private String id;

    private PaymentState paymentState;

    private BigDecimal amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PaymentState getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(PaymentState paymentState) {
        this.paymentState = paymentState;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
