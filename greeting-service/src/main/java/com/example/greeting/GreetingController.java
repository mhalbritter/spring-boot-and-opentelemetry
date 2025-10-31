package com.example.greeting;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
class GreetingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GreetingController.class);

    private final GreetingService greetingService;

    private final HeaderLogger headerLogger;

    private final Delay delay;

    GreetingController(GreetingService greetingService, HeaderLogger headerLogger, Delay delay) {
        this.greetingService = greetingService;
        this.headerLogger = headerLogger;
        this.delay = delay;
    }

    @GetMapping({"", "/"})
    String greeting(Locale locale, WebRequest webRequest) {
        this.headerLogger.log(webRequest);
        this.delay.delay();
        return this.greetingService.getGreeting(locale);
    }
}
