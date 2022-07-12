package org.knoldus.axon.state.machine.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.knoldus.axon.state.machine.constant.ApplicationConstant;
import org.knoldus.axon.state.machine.dto.Charge;
import org.knoldus.axon.state.machine.services.ExecuteAuthorization;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.nio.charset.StandardCharsets;

/**
 * Consume events from kafka topic and sending for the authorization.
 */
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ExecuteAuthorization executeAuthorization;

    @KafkaListener(
            groupId = ApplicationConstant.GROUP_ID_JSON,
            topics = ApplicationConstant.TOPIC_NAME,
            containerFactory = ApplicationConstant.KAFKA_LISTENER_CONTAINER_FACTORY)
    public void receivedMessage(byte[] message) throws JsonProcessingException {

        String ChargeEvent = new String(message, StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        Charge charge = mapper.readValue(ChargeEvent, Charge.class);
        executeAuthorization.execute(charge);
    }
}
