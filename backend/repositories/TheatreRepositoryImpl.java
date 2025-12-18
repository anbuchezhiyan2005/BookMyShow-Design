package repositories;

import models.Theatre;
import config.MongoDBConnectionManager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class TheatreRepositoryImpl implements TheatreRepository {
    private MongoCollection<Document> collection;

    public TheatreRepositoryImpl() {
        this.collection = MongoDBConnectionManager.getDatabase().getCollection("theatres");
        createIndexes();
    }

    private void createIndexes() {
        collection.createIndex(Indexes.ascending("city"));
    }

    @Override
    public Theatre save(Theatre theatre) {
        Document doc = theatreToDocument(theatre);
        collection.insertOne(doc);
        return theatre;
    }

    @Override
    public Theatre findById(String theatreId) {
        Document doc = collection.find(Filters.eq("_id", theatreId)).first();
        return doc == null ? null : documentToTheatre(doc);
    }

    @Override
    public List<Theatre> findAll() {
        List<Theatre> theatres = new ArrayList<>();
        collection.find().forEach(doc -> theatres.add(documentToTheatre(doc)));
        return theatres;
    }

    @Override
    public List<Theatre> findByCity(String city) {
        List<Theatre> theatres = new ArrayList<>();
        collection.find(Filters.eq("city", city)).forEach(doc -> theatres.add(documentToTheatre(doc)));
        return theatres;
    }

    @Override
    public boolean delete(String theatreId) {
        return collection.deleteOne(Filters.eq("_id", theatreId)).getDeletedCount() > 0;
    }

    private Document theatreToDocument(Theatre theatre) {
        return new Document("_id", theatre.getTheatreId())
                .append("name", theatre.getName())
                .append("location", theatre.getLocation())
                .append("city", theatre.getCity())
                .append("totalSeats", theatre.getTotalSeats());
    }

    private Theatre documentToTheatre(Document doc) {
        Theatre theatre = new Theatre();
        theatre.setTheatreId(doc.getString("_id"));
        theatre.setName(doc.getString("name"));
        theatre.setLocation(doc.getString("location"));
        theatre.setCity(doc.getString("city"));
        theatre.setTotalSeats(doc.getInteger("totalSeats"));
        return theatre;
    }
}
