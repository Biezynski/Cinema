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
public class TicketModelCsv {
    @CsvBindByName
    private Long movieId;
    @CsvBindByName
    private String ticketNumber;
    @CsvBindByName
    private String seatNumber;
    @CsvBindByName
    private String ticketPrice;
}
