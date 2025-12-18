package services;

import models.Payment;
import models.Booking;
import enums.PaymentMethod;
import enums.PaymentStatus;
import repositories.PaymentRepository;
import repositories.BookingRepository;
import strategy.PaymentStrategy;

import java.util.UUID;

public class PaymentService {
    private PaymentRepository paymentRepository;
    private BookingRepository bookingRepository;

    public PaymentService(PaymentRepository paymentRepository, BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    public Payment processPayment(String bookingId, double amount, PaymentStrategy paymentStrategy) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new RuntimeException("Booking not found with ID: " + bookingId);
        }

        // Create payment record
        String paymentId = "PAY_" + UUID.randomUUID().toString().substring(0, 8);
        PaymentMethod method = PaymentMethod.valueOf(paymentStrategy.getPaymentMethod());
        Payment payment = new Payment(paymentId, bookingId, amount, method);

        // Process payment using strategy
        boolean paymentSuccess = paymentStrategy.processPayment(amount, bookingId);

        if (paymentSuccess) {
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId("TXN_" + UUID.randomUUID().toString().substring(0, 12));
            
            // Update booking payment status
            bookingRepository.updatePaymentStatus(bookingId, PaymentStatus.SUCCESS);
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            
            // Update booking payment status
            bookingRepository.updatePaymentStatus(bookingId, PaymentStatus.FAILED);
        }

        return paymentRepository.save(payment);
    }

    public Payment getPaymentByBooking(String bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId);
        if (payment == null) {
            throw new RuntimeException("Payment not found for booking ID: " + bookingId);
        }
        return payment;
    }

    public boolean validatePayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId);
        return payment != null && payment.getPaymentStatus() == PaymentStatus.SUCCESS;
    }
}
