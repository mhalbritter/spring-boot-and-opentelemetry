package com.example.hello;

import java.util.Locale;

import com.example.hello.user.UserNotFoundException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
class HelloController {
    private final HelloService helloService;

    HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/{userId}")
    ResponseEntity<String> hello(Locale locale, @PathVariable long userId) {
        try {
            String hello = this.helloService.sayHello(locale, userId);
            return ResponseEntity.ok(hello);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/boom")
    void boom() {
        this.helloService.boom();
    }
}
