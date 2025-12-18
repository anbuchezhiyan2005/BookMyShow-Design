package services;

import models.Theatre;
import models.Show;
import repositories.TheatreRepository;
import repositories.ShowRepository;

import java.util.List;
import java.util.stream.Collectors;

public class TheatreService {
    private TheatreRepository theatreRepository;
    private ShowRepository showRepository;

    public TheatreService(TheatreRepository theatreRepository, ShowRepository showRepository) {
        this.theatreRepository = theatreRepository;
        this.showRepository = showRepository;
    }

    public Theatre addTheatre(Theatre theatre) {
        return theatreRepository.save(theatre);
    }

    public List<Theatre> getAllTheatres() {
        return theatreRepository.findAll();
    }

    public Theatre getTheatreById(String theatreId) {
        Theatre theatre = theatreRepository.findById(theatreId);
        if (theatre == null) {
            throw new RuntimeException("Theatre not found with ID: " + theatreId);
        }
        return theatre;
    }

    public List<Theatre> getTheatresByCity(String city) {
        return theatreRepository.findByCity(city);
    }

    public List<Theatre> getTheatresByMovie(String movieId) {
        // Get all shows for the movie
        List<Show> shows = showRepository.findByMovieId(movieId);
        
        // Extract unique theatre IDs
        List<String> theatreIds = shows.stream()
                .map(Show::getTheatreId)
                .distinct()
                .collect(Collectors.toList());
        
        // Get theatres
        return theatreIds.stream()
                .map(theatreRepository::findById)
                .filter(theatre -> theatre != null)
                .collect(Collectors.toList());
    }
}
