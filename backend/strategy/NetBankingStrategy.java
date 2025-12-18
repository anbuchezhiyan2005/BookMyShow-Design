package strategy;

public class NetBankingStrategy implements PaymentStrategy {
    private String bankName;
    private String accountNumber;

    public NetBankingStrategy(String bankName, String accountNumber) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
    }

    @Override
    public boolean processPayment(double amount, String bookingId) {
        // Simulate net banking validation
        if (bankName == null || bankName.isEmpty()) {
            System.out.println("Invalid bank name");
            return false;
        }
        if (accountNumber == null || accountNumber.length() < 8) {
            System.out.println("Invalid account number");
            return false;
        }

        System.out.println("Processing Net Banking Payment...");
        System.out.println("Bank: " + bankName);
        System.out.println("Account: *****" + accountNumber.substring(accountNumber.length() - 4));
        System.out.println("Amount: ?" + amount);
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Net Banking Payment Successful!");

        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "NET_BANKING";
    }
}
