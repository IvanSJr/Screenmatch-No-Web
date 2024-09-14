package br.com.github.ivansjr.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeasonData(
    @JsonAlias("Title") String title,
    @JsonAlias("Season") Integer season,
    @JsonAlias("Episodes") List<Episode> episodes
) {
}
