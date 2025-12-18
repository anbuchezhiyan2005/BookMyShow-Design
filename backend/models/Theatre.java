package models;

public class Theatre {
    private String theatreId;
    private String name;
    private String location;
    private String city;
    private int totalSeats;

    public Theatre() {}

    public Theatre(String theatreId, String name, String location, String city, int totalSeats) {
        this.theatreId = theatreId;
        this.name = name;
        this.location = location;
        this.city = city;
        this.totalSeats = totalSeats;
    }

    // Getters and Setters
    public String getTheatreId() {
        return theatreId;
    }

    public void setTheatreId(String theatreId) {
        this.theatreId = theatreId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }
}
