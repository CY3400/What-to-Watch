package com.whattowatch.tmdb.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TmdbTvSeriesResponseDto {
    private Integer page;
    private List<TmdbTvSeriesDto> results;

    public Integer getPage() {
        return this.page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<TmdbTvSeriesDto> getResults() {
        return this.results;
    }

    public void setResults(List<TmdbTvSeriesDto> results) {
        this.results = results;
    }
}
