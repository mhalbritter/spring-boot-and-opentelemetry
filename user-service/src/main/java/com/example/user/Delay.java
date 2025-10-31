package com.example.user;

import io.micrometer.tracing.annotation.NewSpan;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(UserServiceConfigurationProperties.class)
class Delay {
    private final UserServiceConfigurationProperties properties;

    Delay(UserServiceConfigurationProperties properties) {
        this.properties = properties;
    }

    @NewSpan
    public void delay() {
        try {
            Thread.sleep(this.properties.getDelay());
        } catch (InterruptedException ex) {
            throw new RuntimeException("Got interrupted while sleeping", ex);
        }
    }
}
