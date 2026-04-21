package com.example.hello.greeting;

import java.util.Locale;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;

/**
 * A facade to easily switch between the Spring HTTP interface client {@link GreetingServiceHttpClient} and a plain
 * {@link RestClient}. The {@link RestClient} uses the same base URL as the interface client does.
 *
 * @author Moritz Halbritter
 */
@Component
@EnableConfigurationProperties(GreetingServiceClientProperties.class)
class HttpFacade {
    private final GreetingServiceClientProperties properties;

    private final GreetingServiceHttpClient interfaceHttpClient;

    private final RestClient restClient;

    HttpFacade(GreetingServiceClientProperties properties, GreetingServiceHttpClient interfaceHttpClient, RestClient.Builder restClientBuilder, Environment environment) {
        this.properties = properties;
        this.interfaceHttpClient = interfaceHttpClient;
        String baseUrl = environment.getProperty("spring.http.serviceclient.greeting.base-url");
        Assert.notNull(baseUrl, "'baseUrl' must not be null");
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
        // WRONG: Never create a RestClient yourself, use the injected one!
        // this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    String greeting(Locale locale) {
        if (this.properties.isUseRestClient()) {
            return this.restClient.get().uri("http://localhost:8082/api").header(HttpHeaders.ACCEPT_LANGUAGE, locale.toLanguageTag()).retrieve()
                .body(String.class);
        }
        return this.interfaceHttpClient.greeting(locale.toLanguageTag());
    }
}
