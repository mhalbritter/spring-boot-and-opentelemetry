package com.example.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component
class HeaderLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderLogger.class);

    void log(WebRequest webRequest) {
        webRequest.getHeaderNames().forEachRemaining(header -> {
            String[] values = webRequest.getHeaderValues(header);
            for (String value : values) {
                LOGGER.debug("{}: {}", header, value);
            }
        });
    }
}
