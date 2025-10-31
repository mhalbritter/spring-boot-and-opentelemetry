package com.example.hello.user;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class UserServiceClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceClient.class);

    private final UserServiceHttpClient httpClient;

    private final AsyncTaskExecutor asyncTaskExecutor;

    UserServiceClient(UserServiceHttpClient httpClient, AsyncTaskExecutor asyncTaskExecutor) {
        this.httpClient = httpClient;
        this.asyncTaskExecutor = asyncTaskExecutor;
    }

    public User find(long id) {
        LOGGER.debug("Fetching user with id {}", id);
        return findImpl(id);
    }

    public Future<User> findAsync(long id) {
        LOGGER.debug("Fetching user async with id {}", id);
        return this.asyncTaskExecutor.submit(() -> findImpl(id));
    }

    private User findImpl(long id) {
        try {
            return this.httpClient.find(id);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new UserNotFoundException(id, ex);
        }
    }


}
