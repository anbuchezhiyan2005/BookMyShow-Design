package repositories;

import models.Show;
import java.util.List;

public interface ShowRepository {
    Show save(Show show);
    Show findById(String showId);
    List<Show> findByMovieId(String movieId);
    List<Show> findByTheatreId(String theatreId);
    boolean updateAvailableSeats(String showId, int availableSeats);
    boolean delete(String showId);
}
