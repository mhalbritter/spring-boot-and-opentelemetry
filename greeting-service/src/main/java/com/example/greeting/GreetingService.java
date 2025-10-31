package com.example.greeting;

import java.util.Locale;
import java.util.Map;

import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.annotation.NewSpan;
import io.micrometer.tracing.annotation.SpanTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

@Service
class GreetingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GreetingService.class);

    private final Map<Locale, String> greetings = Map.of(
        Locale.GERMAN, "Hallo",
        Locale.ENGLISH, "Hello",
        Locale.of("es"), "Hola"
    );

    @Observed(name = "greeting.get")
    @NewSpan
    public String getGreeting(@SpanTag("locale") Locale locale) {
        LOGGER.info("Looking up greeting for locale {}", locale);
        String greeting = this.greetings.get(locale);
        if (greeting == null) {
            greeting = this.greetings.get(Locale.of(locale.getLanguage()));
        }
        return (greeting != null) ? greeting : this.greetings.get(Locale.ENGLISH);
    }
}
