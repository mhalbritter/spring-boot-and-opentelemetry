package com.example.user;

import java.util.List;

import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.annotation.NewSpan;
import io.micrometer.tracing.annotation.SpanTag;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Observed(name = "user.create")
    @Transactional
    public User create(String name) {
        LOGGER.info("Creating user '{}'", name);
        return this.userRepository.save(new User(null, name));
    }

    @Observed(name = "user.list-all")
    @Transactional(readOnly = true)
    public List<User> listAll() {
        LOGGER.info("Listing all users");
        return this.userRepository.findAll();
    }

    @Observed(name = "user.find-with-id")
    @NewSpan
    @Transactional(readOnly = true)
    public @Nullable User findWithId(@SpanTag("id") long id) {
        LOGGER.info("Finding user with id {}", id);
        return this.userRepository.findById(id).orElse(null);
    }
}
