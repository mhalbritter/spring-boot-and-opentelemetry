package com.example.user;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
class UserController {
    private final UserService userService;

    private final HeaderLogger headerLogger;

    private final Delay delay;

    UserController(UserService userService, HeaderLogger headerLogger, Delay delay) {
        this.userService = userService;
        this.headerLogger = headerLogger;
        this.delay = delay;
    }

    @GetMapping({"", "/"})
    List<User> all(WebRequest webRequest) {
        logAndDelay(webRequest);
        return this.userService.listAll();
    }

    @GetMapping({"/{id}",})
    ResponseEntity<User> findById(@PathVariable Long id, WebRequest webRequest) {
        logAndDelay(webRequest);
        User user = this.userService.findWithId(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    private void logAndDelay(WebRequest webRequest) {
        this.headerLogger.log(webRequest);
        this.delay.delay();
    }
}
