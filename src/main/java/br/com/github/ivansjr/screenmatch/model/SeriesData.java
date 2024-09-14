package br.com.github.ivansjr.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeriesData(
    @JsonAlias("Title") String title,
    @JsonAlias("Year") Integer year,
    @JsonAlias("imdbRating") Double imdbRating
) {}
