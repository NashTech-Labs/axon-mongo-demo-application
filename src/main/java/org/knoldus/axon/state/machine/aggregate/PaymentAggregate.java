package org.knoldus.axon.state.machine.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.knoldus.axon.state.machine.command.CheckIn;
import org.knoldus.axon.state.machine.command.CheckOut;
import org.knoldus.axon.state.machine.dto.Charge;
import org.knoldus.axon.state.machine.event.Authorize;
import org.knoldus.axon.state.machine.event.AuthorizationApprove;
import org.knoldus.axon.state.machine.event.AuthorizationDeclined;
import org.knoldus.axon.state.machine.event.PreAuthorizationApprove;
import org.knoldus.axon.state.machine.event.PreAuthorizationDeclined;
import org.knoldus.axon.state.machine.event.PreAuthorize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * [[PaymentAggregate]] - an aggregate class use to represent a state model for payment.
 * it can handle different commands and events and managing states.
 */
@Aggregate
public class PaymentAggregate {

    private final static Logger LOGGER = LoggerFactory.getLogger(PaymentAggregate.class);

    @AggregateIdentifier
    private String paymentId;

    private PaymentState paymentState;

    PaymentAggregate() {}

    @CommandHandler
    public PaymentAggregate(CheckIn checkIn) {

        LOGGER.info("check in received for Id - {}.", checkIn.getCharge().getId());
        AggregateLifecycle.apply(new PreAuthorize(checkIn.getId(),
               checkIn.getCharge()));
    }

    @EventSourcingHandler
    public void on(PreAuthorize preAuthorize) {

        Charge charge = preAuthorize.getCharge();
        LOGGER.info("Pre Authorization event for id - {}.", charge.getId());
        this.paymentId = preAuthorize.getCharge().getId();
        this.paymentState = PaymentState.NEW;
        if (charge.getAvailableAmount()
                .compareTo(charge.getHoldAmount())>0) {

            LOGGER.info("Sufficient Amount available for hotel services for id {}",
                    charge.getId());
            AggregateLifecycle.apply(new PreAuthorizationApprove(charge.getId(),
                    charge));
        } else {

            LOGGER.info("Sufficient amount not available for hotel services for id {}",
                    charge.getId());
            AggregateLifecycle.apply(new PreAuthorizationDeclined(charge.getId(),
                    charge));
        }

    }

    @EventSourcingHandler
    public void on(PreAuthorizationApprove preAuthorizationApprove) {

        Charge charge = preAuthorizationApprove.getCharge();
        LOGGER.info("Pre authorization done, Enjoy the services id - {}.", charge.getId());
        this.paymentId = charge.getId();
        this.paymentState = PaymentState.PRE_AUTH;
    }

    @EventSourcingHandler
    public void on(PreAuthorizationDeclined preAuthorizationDeclined) {

        Charge charge = preAuthorizationDeclined.getCharge();
        LOGGER.info("Pre authorization failed, provide another card id - {}.", charge.getId());
        this.paymentId = charge.getId();
        this.paymentState = PaymentState.PRE_AUTH_ERROR;
    }

    @CommandHandler
    public void on(CheckOut checkOut) {

        LOGGER.info("check out received for Id - {}.", checkOut.getCharge().getId());
        AggregateLifecycle.apply(new Authorize(checkOut.getId(),
                checkOut.getCharge()));
    }

    @EventSourcingHandler
    public void on(Authorize authorize) {

        Charge charge = authorize.getCharge();
        LOGGER.info("Authorization event for id - {}.", charge.getId());
        if (charge.isAuthorize()) {

            LOGGER.info("Pre-authorization done and authorization check " +
                    "met for id {}", charge.getId());
            AggregateLifecycle.apply(new AuthorizationApprove(charge.getId(),
                    charge));
        } else {

            LOGGER.info("either Pre-authorization not done or authorization " +
                    "check failed for id {}", charge.getId());
            AggregateLifecycle.apply(new AuthorizationDeclined(charge.getId(),
                    charge));
        }

    }

    @EventSourcingHandler
    public void on(AuthorizationApprove authorizationApprove) {

        Charge charge = authorizationApprove.getCharge();
        LOGGER.info("Authorization done, captured amount {} " +
                        "from you card id - {}, visit again",
                charge.getHoldAmount(), charge.getId());
        this.paymentId = charge.getId();
        this.paymentState = PaymentState.AUTH;
    }

    @EventSourcingHandler
    public void on(AuthorizationDeclined authorizationDeclined) {

        Charge charge = authorizationDeclined.getCharge();
        LOGGER.info("Authorization failed, provide another card id - {}",
                charge.getId());
        this.paymentId = charge.getId();
        this.paymentState = PaymentState.AUTH_ERROR;
    }

}
