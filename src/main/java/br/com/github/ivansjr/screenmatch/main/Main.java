package br.com.github.ivansjr.screenmatch.main;

import br.com.github.ivansjr.screenmatch.model.Episode;
import br.com.github.ivansjr.screenmatch.model.SeasonData;
import br.com.github.ivansjr.screenmatch.model.SerieData;
import br.com.github.ivansjr.screenmatch.service.APIConsumerService;
import br.com.github.ivansjr.screenmatch.service.DataConverterService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static final String APY_URL_VALUE = System.getenv("APY_URL_VALUE");
    public static final String APY_KEY_VALUE = System.getenv("APY_KEY_VALUE");

    private final Scanner scanner = new Scanner(System.in);
    private final APIConsumerService apiConsumerService = new APIConsumerService();
    private final DataConverterService dataConverterService = new DataConverterService();

    public void showMenu() {
        System.out.println("Digite o nome da série: ");
        var seriesTitle = scanner.nextLine();
        String replacedSeriesTitle = seriesTitle.replaceAll(" ", "+");
        var json = apiConsumerService.getData(APY_URL_VALUE + replacedSeriesTitle + APY_KEY_VALUE);
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
            APY_URL_VALUE + replacedSeriesTitle + "&Season=" + i + APY_KEY_VALUE
            );
            SeasonData seasonData = dataConverterService.dataConverter(json, SeasonData.class);
            seasonDataList.add(seasonData);
        }

        final int[] seasonNumber = {1};
        seasonDataList.forEach(
            seasonData -> {
                System.out.println("Season " + seasonNumber[0]);
                final int[] episodeNumber = {1};
                seasonData.episodes().forEach(
                    episode -> {
                        System.out.println("Ep " + episodeNumber[0] + ": " + episode.title());
                        episodeNumber[0]++;
                    }
                );
                System.out.println("---------------------------------------------------------------------");
                seasonNumber[0]++;
            }
        );
    }
}
