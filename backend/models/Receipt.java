package models;

import java.time.LocalDateTime;

public class Receipt {
    private String receiptId;
    private String bookingId;
    private LocalDateTime generatedDate;
    private String receiptDetails;

    public Receipt() {
        this.generatedDate = LocalDateTime.now();
    }

    public Receipt(String receiptId, String bookingId, String receiptDetails) {
        this.receiptId = receiptId;
        this.bookingId = bookingId;
        this.receiptDetails = receiptDetails;
        this.generatedDate = LocalDateTime.now();
    }

    // Getters and Setters
    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public LocalDateTime getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(LocalDateTime generatedDate) {
        this.generatedDate = generatedDate;
    }

    public String getReceiptDetails() {
        return receiptDetails;
    }

    public void setReceiptDetails(String receiptDetails) {
        this.receiptDetails = receiptDetails;
    }
}
