package com.valgaudeau.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitmqApplication implements CommandLineRunner {

	private final PerformanceTester performanceTester;

	@Autowired
	public RabbitmqApplication(PerformanceTester performanceTester) {
		this.performanceTester = performanceTester;
	}

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		performanceTester.runTestNumberOfMessages(1000);
		// performanceTester.runTestConcurrentUsers(100);
	}
}
