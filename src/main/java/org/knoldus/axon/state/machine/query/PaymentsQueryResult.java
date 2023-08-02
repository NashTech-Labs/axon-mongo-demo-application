package org.knoldus.axon.state.machine.query;

import lombok.Getter;
import org.knoldus.axon.state.machine.query.entity.PaymentEntity;

@Getter
public class PaymentsQueryResult {

    private final PaymentEntity paymentEntity;

    public PaymentsQueryResult(PaymentEntity paymentEntity){
        this.paymentEntity = paymentEntity;
    }

}
