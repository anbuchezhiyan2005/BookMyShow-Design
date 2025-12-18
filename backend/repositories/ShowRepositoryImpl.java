package repositories;

import models.Show;
import config.MongoDBConnectionManager;
import utils.DocumentMapperUtil;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ShowRepositoryImpl implements ShowRepository {
    private MongoCollection<Document> collection;

    public ShowRepositoryImpl() {
        this.collection = MongoDBConnectionManager.getDatabase().getCollection("shows");
        createIndexes();
    }

    private void createIndexes() {
        collection.createIndex(Indexes.ascending("movieId"));
        collection.createIndex(Indexes.ascending("theatreId"));
    }

    @Override
    public Show save(Show show) {
        Document doc = showToDocument(show);
        collection.insertOne(doc);
        return show;
    }

    @Override
    public Show findById(String showId) {
        Document doc = collection.find(Filters.eq("_id", showId)).first();
        return doc == null ? null : documentToShow(doc);
    }

    @Override
    public List<Show> findByMovieId(String movieId) {
        List<Show> shows = new ArrayList<>();
        collection.find(Filters.eq("movieId", movieId)).forEach(doc -> shows.add(documentToShow(doc)));
        return shows;
    }

    @Override
    public List<Show> findByTheatreId(String theatreId) {
        List<Show> shows = new ArrayList<>();
        collection.find(Filters.eq("theatreId", theatreId)).forEach(doc -> shows.add(documentToShow(doc)));
        return shows;
    }

    @Override
    public boolean updateAvailableSeats(String showId, int availableSeats) {
        return collection.updateOne(
                Filters.eq("_id", showId),
                Updates.set("availableSeats", availableSeats)
        ).getModifiedCount() > 0;
    }

    @Override
    public boolean delete(String showId) {
        return collection.deleteOne(Filters.eq("_id", showId)).getDeletedCount() > 0;
    }

    private Document showToDocument(Show show) {
        return new Document("_id", show.getShowId())
                .append("movieId", show.getMovieId())
                .append("theatreId", show.getTheatreId())
                .append("showTime", DocumentMapperUtil.toDate(show.getShowTime()))
                .append("price", show.getPrice())
                .append("availableSeats", show.getAvailableSeats());
    }

    private Show documentToShow(Document doc) {
        Show show = new Show();
        show.setShowId(doc.getString("_id"));
        show.setMovieId(doc.getString("movieId"));
        show.setTheatreId(doc.getString("theatreId"));
        show.setShowTime(DocumentMapperUtil.toLocalDateTime(doc.getDate("showTime")));
        show.setPrice(doc.getDouble("price"));
        show.setAvailableSeats(doc.getInteger("availableSeats"));
        return show;
    }
}
