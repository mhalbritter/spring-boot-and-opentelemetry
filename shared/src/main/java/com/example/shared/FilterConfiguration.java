package com.example.shared;

import io.micrometer.tracing.Tracer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers the {@link HeaderLoggerFilter} and {@link AddTraceIdFilter}.
 */
@Configuration(proxyBeanMethods = false)
public class FilterConfiguration {

    @Bean
    HeaderLoggerFilter headerLoggerFilter() {
        return new HeaderLoggerFilter();
    }

    @Bean
    AddTraceIdFilter addTraceIdFilter(Tracer tracer) {
        return new AddTraceIdFilter(tracer);
    }

}
