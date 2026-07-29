package com.whattowatch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.tmdb")
public class TmdbProperties {
    private String baseUrl;
    private String readAccessToken;

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getReadAccessToken() {
        return this.readAccessToken;
    }

    public void setReadAccessToken(String readAccessToken) {
        this.readAccessToken = readAccessToken;
    }
}
