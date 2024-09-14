package br.com.github.ivansjr.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Episode(
    @JsonAlias("Title") String title,
    @JsonAlias("Released") String released,
    @JsonAlias("imdbRating") String imdbRating
) {}
