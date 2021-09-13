package net.biezynski.Cinema;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import net.biezynski.Cinema.CSV.Car;
import net.biezynski.Cinema.CSV.MovieModelCsv;
import net.biezynski.Cinema.Utils.Utils;
import net.biezynski.Cinema.model.Movie;
import net.biezynski.Cinema.repository.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@SpringBootApplication
public class CinemaApplication implements CommandLineRunner {

    private final MovieRepository movieRepository;

    public CinemaApplication(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        //generate full movie data
        Utils.generateStartData(movieRepository);


  /*      String fileName = "src/main/resources/static/Movie.csv";
        Path myPath = Paths.get(fileName);

        try (BufferedReader br = Files.newBufferedReader(myPath, StandardCharsets.UTF_8))
		{
            HeaderColumnNameMappingStrategy<MovieModelCsv> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(MovieModelCsv.class);

            CsvToBean<MovieModelCsv> csvToBean = new CsvToBeanBuilder<MovieModelCsv>(br)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<MovieModelCsv> cars = csvToBean.parse();
            cars.forEach(System.out::println);
        }*/


    }
}
