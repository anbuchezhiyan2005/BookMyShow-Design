package repositories;

import models.Payment;
import enums.PaymentMethod;
import enums.PaymentStatus;
import config.MongoDBConnectionManager;
import utils.DocumentMapperUtil;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import org.bson.Document;

public class PaymentRepositoryImpl implements PaymentRepository {
    private MongoCollection<Document> collection;

    public PaymentRepositoryImpl() {
        this.collection = MongoDBConnectionManager.getDatabase().getCollection("payments");
        createIndexes();
    }

    private void createIndexes() {
        collection.createIndex(Indexes.ascending("bookingId"));
    }

    @Override
    public Payment save(Payment payment) {
        Document doc = paymentToDocument(payment);
        collection.insertOne(doc);
        return payment;
    }

    @Override
    public Payment findById(String paymentId) {
        Document doc = collection.find(Filters.eq("_id", paymentId)).first();
        return doc == null ? null : documentToPayment(doc);
    }

    @Override
    public Payment findByBookingId(String bookingId) {
        Document doc = collection.find(Filters.eq("bookingId", bookingId)).first();
        return doc == null ? null : documentToPayment(doc);
    }

    @Override
    public boolean delete(String paymentId) {
        return collection.deleteOne(Filters.eq("_id", paymentId)).getDeletedCount() > 0;
    }

    private Document paymentToDocument(Payment payment) {
        return new Document("_id", payment.getPaymentId())
                .append("bookingId", payment.getBookingId())
                .append("amount", payment.getAmount())
                .append("paymentMethod", payment.getPaymentMethod().name())
                .append("paymentStatus", payment.getPaymentStatus().name())
                .append("transactionId", payment.getTransactionId())
                .append("paymentDate", DocumentMapperUtil.toDate(payment.getPaymentDate()));
    }

    private Payment documentToPayment(Document doc) {
        Payment payment = new Payment();
        payment.setPaymentId(doc.getString("_id"));
        payment.setBookingId(doc.getString("bookingId"));
        payment.setAmount(doc.getDouble("amount"));
        payment.setPaymentMethod(PaymentMethod.valueOf(doc.getString("paymentMethod")));
        payment.setPaymentStatus(PaymentStatus.valueOf(doc.getString("paymentStatus")));
        payment.setTransactionId(doc.getString("transactionId"));
        payment.setPaymentDate(DocumentMapperUtil.toLocalDateTime(doc.getDate("paymentDate")));
        return payment;
    }
}
