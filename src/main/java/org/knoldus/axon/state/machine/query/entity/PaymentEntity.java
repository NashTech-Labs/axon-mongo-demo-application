package org.knoldus.axon.state.machine.query.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.knoldus.axon.state.machine.aggregate.PaymentState;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Getter
@JsonSerialize
public class PaymentEntity {

    @Id
    private String id;

    private PaymentState paymentState;

    private BigDecimal amount;

    public void setId(String id) {
        this.id = id;
    }

    public void setPaymentState(PaymentState paymentState) {
        this.paymentState = paymentState;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
