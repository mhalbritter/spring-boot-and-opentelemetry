package com.example.hello;

import com.example.hello.greeting.GreetingServiceHttpClient;
import com.example.hello.user.UserServiceHttpClient;

import org.springframework.boot.restclient.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

/**
 * Configuration for Spring HTTP clients. Base url and other properties are configured in {@code application.properties}.
 */
@Configuration(proxyBeanMethods = false)
@ImportHttpServices(group = "greeting", types = GreetingServiceHttpClient.class)
@ImportHttpServices(group = "user", types = UserServiceHttpClient.class)
class HttpServiceClientConfiguration {
    @Bean
    RestClientCustomizer userAgentCustomizer() {
        return (restClientBuilder -> restClientBuilder.defaultHeader("User-Agent", "hello-service"));
    }
}
