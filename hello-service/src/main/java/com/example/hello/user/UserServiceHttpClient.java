package com.example.hello.user;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface UserServiceHttpClient {
    @GetExchange("/api/{id}")
    User find(@PathVariable long id);
}
