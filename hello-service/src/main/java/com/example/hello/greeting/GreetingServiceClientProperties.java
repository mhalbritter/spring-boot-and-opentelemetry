package com.example.hello.greeting;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Moritz Halbritter
 */
@ConfigurationProperties(prefix = "hello.greeting-client")
public class GreetingServiceClientProperties {
    private boolean useRestClient;

    public boolean isUseRestClient() {
        return this.useRestClient;
    }

    public void setUseRestClient(boolean useRestClient) {
        this.useRestClient = useRestClient;
    }
}
