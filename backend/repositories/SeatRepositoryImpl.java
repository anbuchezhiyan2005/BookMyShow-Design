package repositories;

import models.Seat;
import enums.SeatType;
import config.MongoDBConnectionManager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class SeatRepositoryImpl implements SeatRepository {
    private MongoCollection<Document> collection;

    public SeatRepositoryImpl() {
        this.collection = MongoDBConnectionManager.getDatabase().getCollection("seats");
        createIndexes();
    }

    private void createIndexes() {
        collection.createIndex(Indexes.compoundIndex(
            Indexes.ascending("showId"),
            Indexes.ascending("isAvailable")
        ));
    }

    @Override
    public Seat save(Seat seat) {
        Document doc = seatToDocument(seat);
        collection.insertOne(doc);
        return seat;
    }

    @Override
    public Seat findById(String seatId) {
        Document doc = collection.find(Filters.eq("_id", seatId)).first();
        return doc == null ? null : documentToSeat(doc);
    }

    @Override
    public List<Seat> findByShowId(String showId) {
        List<Seat> seats = new ArrayList<>();
        try {
            ObjectId objectId = new ObjectId(showId);
            collection.find(Filters.eq("showId", objectId)).forEach(doc -> seats.add(documentToSeat(doc)));
        } catch (IllegalArgumentException e) {
            collection.find(Filters.eq("showId", showId)).forEach(doc -> seats.add(documentToSeat(doc)));
        }
        return seats;
    }

    @Override
    public List<Seat> findAvailableByShowId(String showId) {
        List<Seat> seats = new ArrayList<>();
        collection.find(Filters.and(
            Filters.eq("showId", showId),
            Filters.eq("isAvailable", true)
        )).forEach(doc -> seats.add(documentToSeat(doc)));
        return seats;
    }

    @Override
    public boolean updateAvailability(String seatId, boolean isAvailable) {
        return collection.updateOne(
                Filters.eq("_id", seatId),
                Updates.set("isAvailable", isAvailable)
        ).getModifiedCount() > 0;
    }

    @Override
    public boolean atomicBlockSeat(String seatId) {
        // Atomic operation: only update if seat exists AND is available
        // This prevents race conditions in concurrent booking scenarios
        Document result = collection.findOneAndUpdate(
                Filters.and(
                        Filters.eq("_id", seatId),
                        Filters.eq("isAvailable", true)
                ),
                Updates.set("isAvailable", false)
        );
        // Returns true if a document was found and updated, false otherwise
        return result != null;
    }

    @Override
    public boolean delete(String seatId) {
        return collection.deleteOne(Filters.eq("_id", seatId)).getDeletedCount() > 0;
    }

    private Document seatToDocument(Seat seat) {
        return new Document("_id", seat.getSeatId())
                .append("showId", seat.getShowId())
                .append("seatNumber", seat.getSeatNumber())
                .append("isAvailable", seat.isAvailable())
                .append("seatType", seat.getSeatType().name());
    }

    private Seat documentToSeat(Document doc) {
        Seat seat = new Seat();
        Object idField = doc.get("_id");
        if (idField instanceof ObjectId) {
            seat.setSeatId(((ObjectId) idField).toString());
        } else if (idField instanceof String) {
            seat.setSeatId((String) idField);
        }
        
        Object showIdField = doc.get("showId");
        if (showIdField instanceof ObjectId) {
            seat.setShowId(((ObjectId) showIdField).toString());
        } else if (showIdField instanceof String) {
            seat.setShowId((String) showIdField);
        }
        
        seat.setSeatNumber(doc.getString("seatNumber"));
        seat.setAvailable(doc.getBoolean("isAvailable"));
        seat.setSeatType(SeatType.valueOf(doc.getString("seatType")));
        return seat;
    }
}
