package com.example.hello.greeting;

import java.util.Locale;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class GreetingServiceClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(GreetingServiceClient.class);

    private final HttpFacade httpFacade;

    private final AsyncTaskExecutor asyncTaskExecutor;

    GreetingServiceClient(HttpFacade httpFacade, @Qualifier("applicationTaskExecutor") AsyncTaskExecutor asyncTaskExecutor) {
        this.httpFacade = httpFacade;
        this.asyncTaskExecutor = asyncTaskExecutor;
    }

    public String greeting(Locale locale) {
        return greetingImpl(locale);
    }

    public Future<String> greetingAsync(Locale locale) {
        LOGGER.debug("Submitting async task");
        return this.asyncTaskExecutor.submit(() -> greetingImpl(locale));
    }

    private String greetingImpl(Locale locale) {
        LOGGER.debug("Fetching greeting for locale {}", locale);
        return this.httpFacade.greeting(locale);
    }
}
