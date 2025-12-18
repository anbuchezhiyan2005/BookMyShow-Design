package repositories;

import models.Booking;
import enums.BookingStatus;
import enums.PaymentStatus;

import java.util.List;

public interface BookingRepository {
    Booking save(Booking booking);
    Booking findById(String bookingId);
    List<Booking> findAll();
    List<Booking> findByUserId(String userId);
    boolean updateBookingStatus(String bookingId, BookingStatus status);
    boolean updatePaymentStatus(String bookingId, PaymentStatus status);
    boolean delete(String bookingId);
}
