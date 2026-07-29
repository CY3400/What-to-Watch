package com.whattowatch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Configuration
public class TmdbClientConfig {
    private final TmdbProperties properties;

    public TmdbClientConfig(TmdbProperties properties) {
        this.properties = properties;
    }

    @Bean
    public RestClient tmdbRestClient(RestClient.Builder builder) {
        return builder
                    .baseUrl(properties.getBaseUrl())
                    .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + properties.getReadAccessToken()
                    )
                    .defaultHeader(
                        HttpHeaders.ACCEPT,
                        MediaType.APPLICATION_JSON_VALUE
                    )
                    .build();
    }
}
