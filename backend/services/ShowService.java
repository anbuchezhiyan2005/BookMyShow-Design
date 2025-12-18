package services;

import models.Show;
import repositories.ShowRepository;

import java.util.List;

public class ShowService {
    private ShowRepository showRepository;

    public ShowService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    public Show addShow(Show show) {
        return showRepository.save(show);
    }

    public List<Show> getShowsByMovie(String movieId) {
        return showRepository.findByMovieId(movieId);
    }

    public List<Show> getShowsByTheatre(String theatreId) {
        return showRepository.findByTheatreId(theatreId);
    }

    public Show getShowById(String showId) {
        Show show = showRepository.findById(showId);
        if (show == null) {
            throw new RuntimeException("Show not found with ID: " + showId);
        }
        return show;
    }

    public int getAvailableSeatsForShow(String showId) {
        Show show = getShowById(showId);
        return show.getAvailableSeats();
    }
}
