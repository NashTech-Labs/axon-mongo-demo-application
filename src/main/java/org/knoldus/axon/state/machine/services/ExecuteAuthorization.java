package org.knoldus.axon.state.machine.services;

import lombok.RequiredArgsConstructor;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.knoldus.axon.state.machine.command.CheckIn;
import org.knoldus.axon.state.machine.command.CheckOut;
import org.knoldus.axon.state.machine.dto.Charge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ExecuteAuthorization {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExecuteAuthorization.class);
    private final CommandGateway commandGateway;

    public void execute(Charge charge)  {

        switch (charge.getFlow()) {

            case "check-in":
            {
                commandGateway.send(new CheckIn(UUID.randomUUID().toString(), charge));
                break;
            }

            case "check-out":
            {
                commandGateway.send(new CheckOut(charge.getId(), charge));
                break;
            }

            default: LOGGER.info("Wrong service flow");
        }
    }

}
