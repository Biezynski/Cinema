package net.biezynski.Cinema.mappers;

import net.biezynski.Cinema.model.Movie;
import net.biezynski.Cinema.model.MovieDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper( componentModel="spring", uses = {TicketMapper.class})
public interface MovieMapper {
    @Mapping(target = "dateString" ,
            expression = "java(parseLocalDateTime(movie.getDateString()))")
    MovieDto mapMovieToMovieDto(Movie movie);

    List<MovieDto> mapMovieListToMovieListDto(List<Movie> movie);

    default String parseLocalDateTime(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH/mm");//looks bad this format :( but was given in exercise description
        return localDateTime.format(formatter);
    }
}