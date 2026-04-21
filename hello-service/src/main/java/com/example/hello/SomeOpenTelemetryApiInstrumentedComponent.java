package com.example.hello;

import java.util.Random;

import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.TracerProvider;
import io.opentelemetry.context.Scope;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This component is instrumented using the OpenTelemetry API, not Micrometer.
 * Set the property {@code hello.open-telemetry-api-usage} to {@code true} to use it.
 * It will run every 10 seconds, increment a counter and creates a new span. Randomly
 * the span will be an error.
 */
@Component
@ConditionalOnBooleanProperty("hello.open-telemetry-api-usage")
class SomeOpenTelemetryApiInstrumentedComponent {

    private final LongCounter counter;

    private final Tracer tracer;

    private final Random random = new Random();

    SomeOpenTelemetryApiInstrumentedComponent(MeterProvider meterProvider, TracerProvider tracerProvider) {
        Meter meter = meterProvider.get(getClass().getName());
        this.tracer = tracerProvider.get(getClass().getName());
        this.counter = meter.counterBuilder("dummy-counter").build();
    }

    @Scheduled(fixedDelayString = "10s")
    private void run() {
        this.counter.add(1);
        Span span = this.tracer.spanBuilder("dummy-span").startSpan();
        try (Scope _ = span.makeCurrent()) {
            sleep();
            span.setStatus(StatusCode.OK);
        } catch (RuntimeException ex) {
            span.recordException(ex);
            span.setStatus(StatusCode.ERROR);
        } finally {
            span.end();
        }
    }

    private void sleep() {
        if (this.random.nextBoolean()) {
            throw new RuntimeException("Boom");
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            throw new RuntimeException("Got interrupted", ex);
        }
    }

    /**
     * If this is called from e.g. HelloService, it also contributes to the current running span started by
     * Micrometer Tracing. This demonstrates that Micrometer's context is seamlessly propagated to OpenTelemetry.
     */
    public void doSomething() {
        Span span = this.tracer.spanBuilder("do-something").startSpan();
        try (Scope _ = span.makeCurrent()) {
            span.setStatus(StatusCode.OK);
        } finally {
            span.end();
        }
    }
}
