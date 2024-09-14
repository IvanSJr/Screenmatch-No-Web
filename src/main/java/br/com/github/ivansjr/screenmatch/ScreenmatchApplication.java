package br.com.github.ivansjr.screenmatch;

import br.com.github.ivansjr.screenmatch.model.SeriesData;
import br.com.github.ivansjr.screenmatch.service.APIConsumerService;
import br.com.github.ivansjr.screenmatch.service.DataConverterService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var apiConsumerService = new APIConsumerService();
		var json = apiConsumerService.getData("https://www.omdbapi.com/?i=tt3896198&apikey=da26fe7c");

		System.out.println(json);

		var dataConverterService = new DataConverterService();
		var dataSeries = dataConverterService.dataConverter(json, SeriesData.class);
		System.out.println(dataSeries);
	}
}
