package strategy;

public class CardPaymentStrategy implements PaymentStrategy {
    private String cardNumber;
    private String cvv;
    private String expiryDate;

    public CardPaymentStrategy(String cardNumber, String cvv, String expiryDate) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean processPayment(double amount, String bookingId) {
        // Simulate card validation
        if (cardNumber == null || cardNumber.length() != 16) {
            System.out.println("Invalid card number");
            return false;
        }
        if (cvv == null || cvv.length() != 3) {
            System.out.println("Invalid CVV");
            return false;
        }

        System.out.println("Processing Card Payment...");
        System.out.println("Card Number: **** **** **** " + cardNumber.substring(12));
        System.out.println("Amount: ?" + amount);
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Card Payment Successful!");

        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "CARD";
    }
}
