package repositories;

import models.Booking;
import enums.BookingStatus;
import enums.PaymentStatus;
import config.MongoDBConnectionManager;
import utils.DocumentMapperUtil;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class BookingRepositoryImpl implements BookingRepository {
    private MongoCollection<Document> collection;

    public BookingRepositoryImpl() {
        this.collection = MongoDBConnectionManager.getDatabase().getCollection("bookings");
        createIndexes();
    }

    private void createIndexes() {
        collection.createIndex(Indexes.ascending("userId"));
        collection.createIndex(Indexes.ascending("showId"));
    }

    @Override
    public Booking save(Booking booking) {
        Document doc = bookingToDocument(booking);
        collection.insertOne(doc);
        return booking;
    }

    @Override
    public Booking findById(String bookingId) {
        Document doc = collection.find(Filters.eq("_id", bookingId)).first();
        return doc == null ? null : documentToBooking(doc);
    }

    @Override
    public List<Booking> findAll() {
        List<Booking> bookings = new ArrayList<>();
        collection.find().forEach(doc -> bookings.add(documentToBooking(doc)));
        return bookings;
    }

    @Override
    public List<Booking> findByUserId(String userId) {
        List<Booking> bookings = new ArrayList<>();
        collection.find(Filters.eq("userId", userId)).forEach(doc -> bookings.add(documentToBooking(doc)));
        return bookings;
    }

    @Override
    public boolean updateBookingStatus(String bookingId, BookingStatus status) {
        return collection.updateOne(
                Filters.eq("_id", bookingId),
                Updates.set("bookingStatus", status.name())
        ).getModifiedCount() > 0;
    }

    @Override
    public boolean updatePaymentStatus(String bookingId, PaymentStatus status) {
        return collection.updateOne(
                Filters.eq("_id", bookingId),
                Updates.set("paymentStatus", status.name())
        ).getModifiedCount() > 0;
    }

    @Override
    public boolean delete(String bookingId) {
        return collection.deleteOne(Filters.eq("_id", bookingId)).getDeletedCount() > 0;
    }

    private Document bookingToDocument(Booking booking) {
        return new Document("_id", booking.getBookingId())
                .append("userId", booking.getUserId())
                .append("showId", booking.getShowId())
                .append("bookingDate", DocumentMapperUtil.toDate(booking.getBookingDate()))
                .append("totalAmount", booking.getTotalAmount())
                .append("paymentStatus", booking.getPaymentStatus().name())
                .append("bookingStatus", booking.getBookingStatus().name())
                .append("seatIds", booking.getSeatIds());
    }

    @SuppressWarnings("unchecked")
    private Booking documentToBooking(Document doc) {
        Booking booking = new Booking();
        booking.setBookingId(doc.getString("_id"));
        booking.setUserId(doc.getString("userId"));
        booking.setShowId(doc.getString("showId"));
        booking.setBookingDate(DocumentMapperUtil.toLocalDateTime(doc.getDate("bookingDate")));
        booking.setTotalAmount(doc.getDouble("totalAmount"));
        booking.setPaymentStatus(PaymentStatus.valueOf(doc.getString("paymentStatus")));
        booking.setBookingStatus(BookingStatus.valueOf(doc.getString("bookingStatus")));
        booking.setSeatIds((List<String>) doc.get("seatIds"));
        return booking;
    }
}
