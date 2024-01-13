package com.valgaudeau.rabbitmq;

import com.valgaudeau.rabbitmq.services.ServiceOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PerformanceTester {

    private final ServiceOne serviceOne;

    @Autowired
    public PerformanceTester(ServiceOne serviceOne) {
        this.serviceOne = serviceOne;
    }

    public void runTest(int numberOfMessages) {
        for (int i = 1; i <= numberOfMessages; i++) {
            String message = "Test Message " + i;
            serviceOne.sendMessage(message);
        }
    }
}
