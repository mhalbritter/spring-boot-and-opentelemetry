package com.example.user;

import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
class CreateUsers implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateUsers.class);

    private final UserService userService;

    CreateUsers(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Observed(name = "create-users")
    public void run(String... args) {
        User moritz = this.userService.create("Moritz");
        LOGGER.info("Moritz has id {}", moritz.id());
        User andy = this.userService.create("Andy");
        LOGGER.info("Andy has id {}", andy.id());
        User phil = this.userService.create("Phil");
        LOGGER.info("Phil has id {}", phil.id());
        User brian = this.userService.create("Brian");
        LOGGER.info("Brian has id {}", brian.id());
        User stephane = this.userService.create("Stephane");
        LOGGER.info("Stephane has id {}", stephane.id());
    }
}
