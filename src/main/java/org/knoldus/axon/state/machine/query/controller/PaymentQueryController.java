package org.knoldus.axon.state.machine.query.controller;

import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryGateway;
import org.knoldus.axon.state.machine.aggregate.PaymentState;
import org.knoldus.axon.state.machine.query.PaymentQuery;
import org.knoldus.axon.state.machine.query.PaymentsQueryResult;
import org.knoldus.axon.state.machine.query.entity.PaymentEntity;
import org.knoldus.axon.state.machine.services.ExecuteAuthorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Rest controller to get payments update of the user.
 */
@RestController
@RequestMapping(value = "/payments")
@AllArgsConstructor
public class PaymentQueryController {

    private final static Logger LOGGER =
            LoggerFactory.getLogger(PaymentQueryController.class);

    private QueryGateway queryGateway;

    @GetMapping("/{id}/getPayments")
    public ResponseEntity<Object> getPayment(
            @PathVariable(value = "id") String id) {

        LOGGER.info("Get Payment Request for id {}", id);
        AtomicReference<ResponseEntity<Object>> responseEntity
                = new AtomicReference<>();
        queryGateway.query(new PaymentQuery(id), PaymentsQueryResult.class)
                .thenApply(payment -> {
                    LOGGER.info("Payment found for the id {}", id);
                   responseEntity.set(new ResponseEntity<>(payment.getPaymentEntity(),
                            HttpStatus.OK));
                    return responseEntity.get();
                }
        ).exceptionally(exception -> {

            LOGGER.error("Failed get payment for the id {}, exception {} ",
                    id, exception.getMessage());
            responseEntity.set(new ResponseEntity<>(exception.getMessage(),
                    HttpStatus.BAD_REQUEST));
                    return responseEntity.get();
                });
        return responseEntity.get();
    }

}
