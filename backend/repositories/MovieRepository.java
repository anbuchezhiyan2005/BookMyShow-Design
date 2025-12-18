package repositories;

import models.Movie;
import java.util.List;

public interface MovieRepository {
    Movie save(Movie movie);
    Movie findById(String movieId);
    List<Movie> findAll();
    List<Movie> findByTitle(String title);
    List<Movie> findByGenre(String genre);
    boolean delete(String movieId);
}
