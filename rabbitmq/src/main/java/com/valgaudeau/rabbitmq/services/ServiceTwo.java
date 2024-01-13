package com.valgaudeau.rabbitmq.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ServiceTwo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTwo.class);

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    private final AtomicInteger processedMessageCount = new AtomicInteger(0);

    public int getProcessedMessageCount() {
        return processedMessageCount.get();
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveMessage(String message) {
        try {
            long receivedTimeMillis = System.currentTimeMillis();
            String receivedTimeFormatted = formatMillis(receivedTimeMillis);
            LOGGER.info("Message received at {} -> {}", receivedTimeFormatted, message);
            processedMessageCount.incrementAndGet();
        } catch (Exception e) {
            LOGGER.error("Error processing received message: {}", e.getMessage(), e);
        }
    }

    private String formatMillis(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(new Date(millis));
    }
}
