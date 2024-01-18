package com.valgaudeau.rabbitmq;

import com.valgaudeau.rabbitmq.services.ServiceOne;
import com.valgaudeau.rabbitmq.services.ServiceTwo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class PerformanceTester {

    private final ServiceOne serviceOne;

    @Autowired
    public PerformanceTester(ServiceOne serviceOne) {
        this.serviceOne = serviceOne;
    }

    public void runTest(int concurrentUsers) {
        Instant start = Instant.now();
        executeConcurrentTest(concurrentUsers);
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println("Total time taken: " + duration.toMillis() + " milliseconds");
    }

    private void executeConcurrentTest(int concurrentUsers) {
        System.out.println("Simulating " + concurrentUsers + " concurrent users...");
        Instant start = Instant.now();
        ExecutorService executorService = Executors.newFixedThreadPool(concurrentUsers);
        int messagesPerSecond = 1;
        int messagesPerUser = messagesPerSecond * 10; // Total number of messages we want to send, which controls time

        for (int i = 1; i <= concurrentUsers; i++) {
            int finalI = i;
            // we use executorService to submit tasks for each of our simulated concurrentUsers on separate threads
            executorService.submit(() -> {
                Instant userStart = Instant.now();
                for (int j = 1; j <= messagesPerUser; j++) {
                    String message = "Test Message " + j;
                    serviceOne.sendMessage(message);
                    sleep(1000); // milliseconds before user sends next message
                }
                Instant userEnd = Instant.now();
                Duration userDuration = Duration.between(userStart, userEnd);
                System.out.println("User " + finalI + " - Average Response Time: " + userDuration.dividedBy(messagesPerUser).toMillis() + " milliseconds");
            });
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println("Time taken for " + concurrentUsers + " users: " + duration.toMillis() + " milliseconds");
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
