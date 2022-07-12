package org.knoldus.axon.state.machine.constant;

public class ApplicationConstant {

    public static final String TOPIC_NAME = "#{'${spring.kafka.consumer.topic}'}";
    public static final String KAFKA_LISTENER_CONTAINER_FACTORY = "kafkaListenerContainerFactory";
    public static final String GROUP_ID_JSON = "group-id-json-3";

    public static final String TRUSTED_PACKAGE = "org.knoldus.ssm.dto";
}
