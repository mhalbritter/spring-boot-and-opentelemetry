package com.example.hello.greeting;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;

public interface GreetingServiceHttpClient {
    @GetExchange("/api")
    String greeting(@RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) String locale);
}
