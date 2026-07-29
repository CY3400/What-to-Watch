package com.whattowatch.tmdb.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.whattowatch.tmdb.dto.TmdbTvSeriesResponseDto;

@Service
public class TmdbService {
    private final RestClient tmdbRestClient;

    public TmdbService(@Qualifier("tmdbRestClient") RestClient tmdbRestClient) {
        this.tmdbRestClient = tmdbRestClient;
    }

    public TmdbTvSeriesResponseDto getPopularTvSeries() {
        return tmdbRestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/tv/popular")
                        .queryParam("language", "fr-FR")
                        .queryParam("page", 1)
                        .build()
                )
                .retrieve()
                .body(TmdbTvSeriesResponseDto.class);
    }
}
