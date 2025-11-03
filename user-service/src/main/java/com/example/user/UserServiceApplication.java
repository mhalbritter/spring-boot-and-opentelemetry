package com.example.user;

import com.example.shared.ContextPropagationConfiguration;
import com.example.shared.OpenTelemetryConfiguration;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({OpenTelemetryConfiguration.class, ContextPropagationConfiguration.class})
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner configureLogging(OpenTelemetry openTelemetry) {
        return (_) -> OpenTelemetryAppender.install(openTelemetry);
    }

}
