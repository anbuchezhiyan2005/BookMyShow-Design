package strategy;

public class UpiPaymentStrategy implements PaymentStrategy {
    private String upiId;

    public UpiPaymentStrategy(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public boolean processPayment(double amount, String bookingId) {
        // Simulate UPI validation
        if (upiId == null || !upiId.contains("@")) {
            System.out.println("Invalid UPI ID");
            return false;
        }

        System.out.println("Processing UPI Payment...");
        System.out.println("UPI ID: " + upiId);
        System.out.println("Amount: ?" + amount);
        System.out.println("Booking ID: " + bookingId);
        System.out.println("UPI Payment Successful!");

        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "UPI";
    }
}
