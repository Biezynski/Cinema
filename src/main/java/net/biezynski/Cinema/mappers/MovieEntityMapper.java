package net.biezynski.Cinema.mappers;

import net.biezynski.Cinema.model.Movie;
import net.biezynski.Cinema.request.EditMovieRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MovieEntityMapper {
    void updateMovieEntity(EditMovieRequest editMovieRequest, @MappingTarget Movie movie);
}
