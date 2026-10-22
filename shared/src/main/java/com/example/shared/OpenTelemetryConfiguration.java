package com.example.shared;

import io.opentelemetry.api.OpenTelemetry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers the {@link InstallOpenTelemetryAppender}.
 */
@Configuration(proxyBeanMethods = false)
public class OpenTelemetryConfiguration {

    @Bean
    InstallOpenTelemetryAppender installOpenTelemetryAppender(OpenTelemetry openTelemetry) {
        return new InstallOpenTelemetryAppender(openTelemetry);
    }

}
