package com.example.hello;

import java.util.Locale;

import com.example.hello.user.UserNotFoundException;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
class HelloController {
    private final HelloService helloService;

    private final HeaderLogger headerLogger;

    private final Tracer tracer;

    HelloController(HelloService helloService, HeaderLogger headerLogger, Tracer tracer) {
        this.helloService = helloService;
        this.headerLogger = headerLogger;
        this.tracer = tracer;
    }

    @GetMapping("/{userId}")
    ResponseEntity<String> hello(Locale locale, @PathVariable long userId, WebRequest webRequest) {
        this.headerLogger.log(webRequest);
        HttpHeaders headers = addTraceIdHeader();
        try {
            String hello = this.helloService.sayHello(locale, userId);
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(hello);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().headers(headers).build();
        }
    }

    private HttpHeaders addTraceIdHeader() {
        HttpHeaders headers = new HttpHeaders();
        TraceContext context = this.tracer.currentTraceContext().context();
        if (context != null) {
            headers.add("X-Trace-Id", context.traceId());
        }
        return headers;
    }
}
