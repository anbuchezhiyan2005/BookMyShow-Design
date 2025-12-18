package repositories;

import models.Payment;

public interface PaymentRepository {
    Payment save(Payment payment);
    Payment findById(String paymentId);
    Payment findByBookingId(String bookingId);
    boolean delete(String paymentId);
}
