package com.valgaudeau.rabbitmq.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ServiceOne {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key.name}")
    private String routingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceOne.class);

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ServiceOne(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message) {
        try {
            long sentTimeMillis = System.currentTimeMillis();
            String sentTimeFormatted = formatMillis(sentTimeMillis);
            LOGGER.info("Message sent at {} -> {}", sentTimeFormatted, message);
            rabbitTemplate.convertAndSend(exchange, routingKey, sentTimeFormatted + " " + message);
        } catch (Exception e) {
            LOGGER.error("Error sending message: {}", e.getMessage(), e);
        }
    }

    private String formatMillis(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(millis));
    }

}
