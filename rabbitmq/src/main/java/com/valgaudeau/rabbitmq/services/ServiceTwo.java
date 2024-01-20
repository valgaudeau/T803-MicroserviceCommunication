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
    public void receiveMessage(String fullMessage) {
        try {
            // Capture time at which the message is received first
            long receivedTimeMillis = System.currentTimeMillis();
            String receivedTimeFormatted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(receivedTimeMillis));

            // Process the message in another method so that the timestamp doesn't include the time
            // it takes to split the message string
            processMessage(fullMessage, receivedTimeFormatted);

            processedMessageCount.incrementAndGet();
        } catch (Exception e) {
            LOGGER.error("Error processing received message: {}", e.getMessage(), e);
        }
    }

    private void processMessage(String fullMessage, String receivedTimeFormatted) {
        // Split the full message
        String[] parts = fullMessage.split(" ", 2);
        String sentTimeFormatted = parts[0] + " " + parts[1];

        // Log the message, sent time, and time difference
        logMessage(sentTimeFormatted, receivedTimeFormatted, parts[1]);
    }

    private void logMessage(String sentTimeFormatted, String receivedTimeFormatted, String message) {
        LOGGER.info("Message received at {} -> {}", receivedTimeFormatted, message);
        LOGGER.info("Sent time: {}", sentTimeFormatted);
        LOGGER.info("Time difference: {} ms", getTimeDifference(sentTimeFormatted, receivedTimeFormatted));
    }

    private long getTimeDifference(String sentTimeFormatted, String receivedTimeFormatted) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date sentTime = sdf.parse(sentTimeFormatted);
            Date receivedTime = sdf.parse(receivedTimeFormatted);
            return receivedTime.getTime() - sentTime.getTime();
        } catch (Exception e) {
            LOGGER.error("Error calculating time difference: {}", e.getMessage(), e);
            return -1;
        }
    }
}
