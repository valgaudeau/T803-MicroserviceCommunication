package com.valgaudeau.rabbitmq;

import com.valgaudeau.rabbitmq.services.ServiceOne;
import com.valgaudeau.rabbitmq.services.ServiceTwo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class PerformanceTester {

    private final ServiceOne serviceOne;
    private final ServiceTwo serviceTwo;

    @Autowired
    public PerformanceTester(ServiceOne serviceOne, ServiceTwo serviceTwo) {
        this.serviceOne = serviceOne;
        this.serviceTwo = serviceTwo;
    }

    public void runTest(int numberOfMessages) {
        Instant start = Instant.now();

        for (int i = 1; i <= numberOfMessages; i++) {
            String message = "Test Message " + i;
            serviceOne.sendMessage(message);
        }

        // We have to wait for all messages to be processed
        // The @RabbitListener annotated method in ServiceTwo is invoked in a separate thread
        // and operates asynchronously
        while (serviceTwo.getProcessedMessageCount() < numberOfMessages) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Once we've waited for all messages to be processed, we resume this thread
        // Not perfect, but it's vey close to the real time performance
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println("Total time taken: " + duration.toMillis() + " milliseconds");
    }
}
