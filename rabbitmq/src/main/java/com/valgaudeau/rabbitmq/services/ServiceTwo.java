package com.valgaudeau.rabbitmq.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServiceTwo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTwo.class);

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveMessage(String message) {
        try {
            LOGGER.info("Message received -> {}", message);

        } catch (Exception e) {
            LOGGER.error("Error processing received message: {}", e.getMessage(), e);
        }
    }
}
