package repositories;

import models.Theatre;
import java.util.List;

public interface TheatreRepository {
    Theatre save(Theatre theatre);
    Theatre findById(String theatreId);
    List<Theatre> findAll();
    List<Theatre> findByCity(String city);
    boolean delete(String theatreId);
}
