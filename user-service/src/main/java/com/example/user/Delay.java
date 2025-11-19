package com.example.user;

import io.micrometer.observation.annotation.Observed;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(UserServiceConfigurationProperties.class)
class Delay {
    private final UserServiceConfigurationProperties properties;

    Delay(UserServiceConfigurationProperties properties) {
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
