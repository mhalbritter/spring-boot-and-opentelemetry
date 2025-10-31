package com.example.hello;

import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.example.hello.greeting.GreetingServiceClient;
import com.example.hello.user.User;
import com.example.hello.user.UserServiceClient;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.annotation.NewSpan;
import io.micrometer.tracing.annotation.SpanTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(HelloConfigurationProperties.class)
class HelloService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloService.class);

    private final GreetingServiceClient greetingServiceClient;

    private final UserServiceClient userServiceClient;

    private final HelloConfigurationProperties properties;

    private final ObservationRegistry observationRegistry;

    HelloService(GreetingServiceClient greetingServiceClient, UserServiceClient userServiceClient, HelloConfigurationProperties properties, ObservationRegistry observationRegistry) {
        this.greetingServiceClient = greetingServiceClient;
        this.userServiceClient = userServiceClient;
        this.properties = properties;
        this.observationRegistry = observationRegistry;
    }

    @Observed(name = "say-hello")
    @NewSpan
    public String sayHello(@SpanTag("locale") Locale locale, @SpanTag("user.id") long userId) {
        LOGGER.info("Saying hello to user {} with locale {}", userId, locale);
        if (this.properties.isAsync()) {
            return sayHelloAsync(locale, userId);
        }
        String greeting = this.greetingServiceClient.greeting(locale);
        User user = this.userServiceClient.find(userId);
        return greeting + " " + user.name();
    }

    private String sayHelloAsync(Locale locale, long userId) {
        Future<String> greeting = this.greetingServiceClient.greetingAsync(locale);
        Future<User> user = this.userServiceClient.findAsync(userId);
        try {
            return greeting.get() + " " + user.get().name();
        } catch (InterruptedException ex) {
            throw new RuntimeException("Got interrupted while waiting for greeting or user", ex);
        } catch (ExecutionException ex) {
            if (ex.getCause() instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new RuntimeException("Fetching greeting or user failed", ex);
        }
    }

}
