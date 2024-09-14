package br.com.github.ivansjr.screenmatch;

import br.com.github.ivansjr.screenmatch.model.SeasonData;
import br.com.github.ivansjr.screenmatch.model.SerieData;
import br.com.github.ivansjr.screenmatch.service.APIConsumerService;
import br.com.github.ivansjr.screenmatch.service.DataConverterService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static final String APY_KEY_VALUE = System.getenv("APY_KEY_VALUE");

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var apiConsumerService = new APIConsumerService();
		var json = apiConsumerService.getData("https://www.omdbapi.com/?t=Game%20of%20Thrones&apiKey="+APY_KEY_VALUE);
		var dataConverterService = new DataConverterService();

		SerieData serieData = dataConverterService.dataConverter(json, SerieData.class);
		System.out.println(serieData);

		for (int i = 1; i<=serieData.totalSeasons(); i++) {
			json = apiConsumerService.getData("https://www.omdbapi.com/?t=Game%20of%20Thrones"+"&Season="+i+"&apiKey="+APY_KEY_VALUE);
			SeasonData seasonData = dataConverterService.dataConverter(json, SeasonData.class);
			System.out.println(seasonData);
		}
	}
}
