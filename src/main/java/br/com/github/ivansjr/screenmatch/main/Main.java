package br.com.github.ivansjr.screenmatch.main;

import br.com.github.ivansjr.screenmatch.model.Episode;
import br.com.github.ivansjr.screenmatch.model.EpisodeData;
import br.com.github.ivansjr.screenmatch.model.SeasonData;
import br.com.github.ivansjr.screenmatch.model.SerieData;
import br.com.github.ivansjr.screenmatch.service.APIConsumerService;
import br.com.github.ivansjr.screenmatch.service.DataConverterService;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static final String API_URL_VALUE = System.getenv("API_URL_VALUE");
    public static final String API_KEY_VALUE = System.getenv("API_KEY_VALUE");

    private final Scanner scanner = new Scanner(System.in);
    private final APIConsumerService apiConsumerService = new APIConsumerService();
    private final DataConverterService dataConverterService = new DataConverterService();

    public void showMenu() {
        System.out.println("Digite o nome da série: ");
        var seriesTitle = scanner.nextLine();
        String replacedSeriesTitle = seriesTitle.replaceAll(" ", "+");
        var json = apiConsumerService.getData(API_URL_VALUE + replacedSeriesTitle + API_KEY_VALUE);
        SerieData serieData = dataConverterService.dataConverter(json, SerieData.class);

        System.out.println("---------------------------------------------------------------------");
        System.out.println("Titulo: " + serieData.title());
        System.out.println("Total de temporadas: " + serieData.totalSeasons());
        System.out.println("Data de lançamento: " + serieData.released());
        System.out.println("IMBD Rating: " + serieData.imdbRating());
        System.out.println("IMBD Votes: " + serieData.imdbVotes());
        System.out.println("---------------------------------------------------------------------");

        List<SeasonData> seasonDataList = new ArrayList<>();
        for (int i = 1; i<=serieData.totalSeasons(); i++) {
            json = apiConsumerService.getData(
            API_URL_VALUE + replacedSeriesTitle + "&Season=" + i + API_KEY_VALUE
            );
            SeasonData seasonData = dataConverterService.dataConverter(json, SeasonData.class);
            seasonDataList.add(seasonData);
        }

        final int[] seasonNumber = {1};
        seasonDataList.forEach(
            seasonData -> {
                System.out.println("Season " + seasonNumber[0]);
                final int[] episodeNumber = {1};
                seasonData.episodeData().forEach(
                        episodeData -> {
                        System.out.println("Ep " + episodeNumber[0] + ": " + episodeData.title());
                        episodeNumber[0]++;
                    }
                );
                System.out.println("---------------------------------------------------------------------");
                seasonNumber[0]++;
            }
        );

        System.out.println("Top 5 episódios de acordo com o IMDB");
        List<EpisodeData> allEps = seasonDataList.stream()
            .flatMap(t -> t.episodeData().stream())
            .toList();

        List<EpisodeData> topFiveEpsData = allEps.stream()
            .filter(e -> !e.imdbRating().equalsIgnoreCase("N/A"))
            .sorted(Comparator.comparing(EpisodeData::imdbRating).reversed())
            .limit(5)
            .toList();

        topFiveEpsData.forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------");

        List<Episode> episodes = seasonDataList.stream().flatMap(
            t -> t.episodeData().stream().map(
                episodeData -> new Episode(t.season(), episodeData)
            )
        ).toList();

        episodes.forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------");

        System.out.println("Digite o trecho do titulo do episódio: ");
        var partOfTitle = scanner.nextLine();
        episodes
            .stream()
            .filter(
                episode -> episode.getTitle().toLowerCase().contains(partOfTitle.toLowerCase())
            )
            .findFirst()
            .ifPresent(System.out::println);

        Map<Integer, Double> ratingToSeason = episodes
            .stream()
            .filter(episode -> episode.getImdbRating() > 0.0)
            .collect(
                Collectors.groupingBy(
                    Episode::getSeason,
                    Collectors.averagingDouble(Episode::getImdbRating)
                )
            );
        ratingToSeason.forEach((season, rating) ->
            System.out.printf("Season %d: Average Rating = %.2f%n", season, rating)
        );
    }
}
