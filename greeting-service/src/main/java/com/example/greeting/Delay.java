package com.example.greeting;

import io.micrometer.observation.annotation.Observed;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(GreetingServiceConfigurationProperties.class)
class Delay {
    private final GreetingServiceConfigurationProperties properties;

    Delay(GreetingServiceConfigurationProperties properties) {
        this.properties = properties;
    }

    @Observed
    public void delay() {
        try {
            Thread.sleep(this.properties.getDelay());
        } catch (InterruptedException ex) {
            throw new RuntimeException("Got interrupted while sleeping", ex);
        }
    }
}
