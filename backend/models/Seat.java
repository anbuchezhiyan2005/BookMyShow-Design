package models;

import enums.SeatType;

public class Seat {
    private String seatId;
    private String showId;
    private String seatNumber;
    private boolean isAvailable;
    private SeatType seatType;

    public Seat() {
        this.isAvailable = true;
    }

    public Seat(String seatId, String showId, String seatNumber, SeatType seatType) {
        this.seatId = seatId;
        this.showId = showId;
        this.seatNumber = seatNumber;
        this.isAvailable = true;
        this.seatType = seatType;
    }

    // Getters and Setters
    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }
}
