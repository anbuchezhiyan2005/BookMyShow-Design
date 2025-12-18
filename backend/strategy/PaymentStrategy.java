package strategy;

public interface PaymentStrategy {
    boolean processPayment(double amount, String bookingId);
    String getPaymentMethod();
}
