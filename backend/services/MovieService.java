package services;

import models.Movie;
import repositories.MovieRepository;

import java.util.List;

public class MovieService {
    private MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(String movieId) {
        Movie movie = movieRepository.findById(movieId);
        if (movie == null) {
            throw new RuntimeException("Movie not found with ID: " + movieId);
        }
        return movie;
    }

    public List<Movie> searchMoviesByTitle(String title) {
        return movieRepository.findByTitle(title);
    }

    public List<Movie> getMoviesByGenre(String genre) {
        return movieRepository.findByGenre(genre);
    }
}
