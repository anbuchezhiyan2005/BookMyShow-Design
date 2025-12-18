package models;

import enums.BookingStatus;
import enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Booking {
    private String bookingId;
    private String userId;
    private String showId;
    private LocalDateTime bookingDate;
    private double totalAmount;
    private PaymentStatus paymentStatus;
    private BookingStatus bookingStatus;
    private List<String> seatIds;

    public Booking() {
        this.bookingDate = LocalDateTime.now();
        this.paymentStatus = PaymentStatus.PENDING;
        this.bookingStatus = BookingStatus.PENDING;
        this.seatIds = new ArrayList<>();
    }

    public Booking(String bookingId, String userId, String showId, double totalAmount) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.showId = showId;
        this.totalAmount = totalAmount;
        this.bookingDate = LocalDateTime.now();
        this.paymentStatus = PaymentStatus.PENDING;
        this.bookingStatus = BookingStatus.PENDING;
        this.seatIds = new ArrayList<>();
    }

    // Getters and Setters
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public List<String> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<String> seatIds) {
        this.seatIds = seatIds;
    }
}
