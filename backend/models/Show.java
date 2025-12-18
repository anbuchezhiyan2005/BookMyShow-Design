package models;

import java.time.LocalDateTime;

public class Show {
    private String showId;
    private String movieId;
    private String theatreId;
    private LocalDateTime showTime;
    private double price;
    private int availableSeats;

    public Show() {}

    public Show(String showId, String movieId, String theatreId, LocalDateTime showTime, double price, int availableSeats) {
        this.showId = showId;
        this.movieId = movieId;
        this.theatreId = theatreId;
        this.showTime = showTime;
        this.price = price;
        this.availableSeats = availableSeats;
    }

    // Getters and Setters
    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTheatreId() {
        return theatreId;
    }

    public void setTheatreId(String theatreId) {
        this.theatreId = theatreId;
    }

    public LocalDateTime getShowTime() {
        return showTime;
    }

    public void setShowTime(LocalDateTime showTime) {
        this.showTime = showTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}
