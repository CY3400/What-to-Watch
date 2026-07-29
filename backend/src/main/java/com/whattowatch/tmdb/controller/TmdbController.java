package com.whattowatch.tmdb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whattowatch.tmdb.dto.TmdbTvSeriesResponseDto;
import com.whattowatch.tmdb.service.TmdbService;

@RestController
@RequestMapping("/api/series")
public class TmdbController {
    private final TmdbService tmdbService;

    public TmdbController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("/popular")
    public TmdbTvSeriesResponseDto getPopular() {
        return tmdbService.getPopularTvSeries();
    }
}
