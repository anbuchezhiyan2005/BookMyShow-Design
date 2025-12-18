package repositories;

import models.Receipt;

public interface ReceiptRepository {
    Receipt save(Receipt receipt);
    Receipt findById(String receiptId);
    Receipt findByBookingId(String bookingId);
    boolean delete(String receiptId);
}
