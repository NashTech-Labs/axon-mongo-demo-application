package org.knoldus.axon.state.machine.query;

import lombok.Getter;

@Getter
public class PaymentQuery {

    private final String id;

    public PaymentQuery(String  id){
        this.id = id;
    }

}
