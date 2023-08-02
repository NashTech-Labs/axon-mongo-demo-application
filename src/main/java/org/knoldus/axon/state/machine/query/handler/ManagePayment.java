package org.knoldus.axon.state.machine.query.handler;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.knoldus.axon.state.machine.aggregate.PaymentAggregate;
import org.knoldus.axon.state.machine.aggregate.PaymentState;
import org.knoldus.axon.state.machine.dto.Charge;
import org.knoldus.axon.state.machine.event.AuthorizationApprove;
import org.knoldus.axon.state.machine.event.AuthorizationDeclined;
import org.knoldus.axon.state.machine.event.PreAuthorizationApprove;
import org.knoldus.axon.state.machine.event.PreAuthorizationDeclined;
import org.knoldus.axon.state.machine.event.PreAuthorize;

import org.knoldus.axon.state.machine.query.PaymentQuery;
import org.knoldus.axon.state.machine.query.PaymentsQueryResult;
import org.knoldus.axon.state.machine.query.entity.PaymentEntity;
import org.knoldus.axon.state.machine.query.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service to handle events and persit the payment with state in mongo db as document.
 */
@Service
public class ManagePayment {

    private final static Logger LOGGER = LoggerFactory.getLogger(PaymentAggregate.class);

    private final PaymentRepository paymentRepository;
    PaymentEntity payment = new PaymentEntity();

    public ManagePayment(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @EventHandler
    public void on(PreAuthorize preAuthorize) {

        Charge charge = preAuthorize.getCharge();
        LOGGER.info("Handling preAuthorize event for id {}",
               charge.getId());

        payment.setId(charge.getId());
        payment.setPaymentState(PaymentState.NEW);
        payment.setAmount(charge.getAvailableAmount());
        paymentRepository.save(payment);
    }

    @EventHandler
    public void on(PreAuthorizationApprove preAuthorizationApprove) {

        Charge charge = preAuthorizationApprove.getCharge();
        LOGGER.info("Handling preAuthorizationApprove event for id {}",
                charge.getId());

        payment.setId(charge.getId());
        payment.setPaymentState(PaymentState.PRE_AUTH);
        payment.setAmount(charge.getAvailableAmount());
        paymentRepository.save(payment);
    }

    @EventHandler
    public void on(PreAuthorizationDeclined preAuthorizationDeclined) {

        Charge charge = preAuthorizationDeclined.getCharge();
        LOGGER.info("Handling preAuthorizationDeclined event for id {}",
                charge.getId());

        payment.setId(charge.getId());
        payment.setPaymentState(PaymentState.PRE_AUTH_ERROR);
        payment.setAmount(charge.getAvailableAmount());
        paymentRepository.save(payment);
    }

    @EventHandler
    public void on(AuthorizationApprove authorizationApprove) {

        Charge charge = authorizationApprove.getCharge();
        LOGGER.info("Handling authorizationApprove event for id {}",
                charge.getId());

        payment.setId(charge.getId());
        payment.setPaymentState(PaymentState.AUTH);
        payment.setAmount(charge.getAvailableAmount());
        paymentRepository.save(payment);
    }

    @EventHandler
    public void on(AuthorizationDeclined authorizationDeclined) {

        Charge charge = authorizationDeclined.getCharge();
        LOGGER.info("Handling authorizationDeclined event for id {}",
                charge.getId());

        payment.setId(charge.getId());
        payment.setPaymentState(PaymentState.AUTH_ERROR);
        payment.setAmount(charge.getAvailableAmount());
        paymentRepository.save(payment);
    }

    @QueryHandler
    public PaymentsQueryResult on(PaymentQuery query) {

        LOGGER.info("Query Payment for id {}", query.getId());
        final Optional<PaymentEntity> paymentEntity =
                paymentRepository.findById(query.getId());
        if (paymentEntity.isPresent()) {
            return new PaymentsQueryResult(paymentEntity.get());
        } else {

            throw new IllegalArgumentException("no payment with id '"
                    + query.getId()+"' found");
        }

    }

}
