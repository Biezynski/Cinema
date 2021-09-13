package net.biezynski.Cinema.CSV;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class MovieModelCsv {

    @CsvBindByName
    private Long id;
    @CsvBindByName
    private String movieName;
    @CsvBindByName
    private String description;
    @CsvBindByName
    private String dateString;
    @CsvBindByName
    private int movieLength;
}
