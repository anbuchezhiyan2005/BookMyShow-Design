package repositories;

import models.Movie;
import config.MongoDBConnectionManager;
import utils.DocumentMapperUtil;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MovieRepositoryImpl implements MovieRepository {
    private MongoCollection<Document> collection;

    public MovieRepositoryImpl() {
        this.collection = MongoDBConnectionManager.getDatabase().getCollection("movies");
        createIndexes();
    }

    private void createIndexes() {
        collection.createIndex(Indexes.ascending("title"));
        collection.createIndex(Indexes.ascending("genre"));
    }

    @Override
    public Movie save(Movie movie) {
        Document doc = movieToDocument(movie);
        collection.insertOne(doc);
        return movie;
    }

    @Override
    public Movie findById(String movieId) {
        Document doc = collection.find(Filters.eq("_id", movieId)).first();
        return doc == null ? null : documentToMovie(doc);
    }

    @Override
    public List<Movie> findAll() {
        List<Movie> movies = new ArrayList<>();
        collection.find().forEach(doc -> movies.add(documentToMovie(doc)));
        return movies;
    }

    @Override
    public List<Movie> findByTitle(String title) {
        List<Movie> movies = new ArrayList<>();
        Pattern pattern = Pattern.compile(title, Pattern.CASE_INSENSITIVE);
        collection.find(Filters.regex("title", pattern)).forEach(doc -> movies.add(documentToMovie(doc)));
        return movies;
    }

    @Override
    public List<Movie> findByGenre(String genre) {
        List<Movie> movies = new ArrayList<>();
        collection.find(Filters.eq("genre", genre)).forEach(doc -> movies.add(documentToMovie(doc)));
        return movies;
    }

    @Override
    public boolean delete(String movieId) {
        return collection.deleteOne(Filters.eq("_id", movieId)).getDeletedCount() > 0;
    }

    private Document movieToDocument(Movie movie) {
        return new Document("_id", movie.getMovieId())
                .append("title", movie.getTitle())
                .append("description", movie.getDescription())
                .append("duration", movie.getDuration())
                .append("genre", movie.getGenre())
                .append("language", movie.getLanguage())
                .append("releaseDate", DocumentMapperUtil.toDate(movie.getReleaseDate()));
    }

    private Movie documentToMovie(Document doc) {
        Movie movie = new Movie();
        movie.setMovieId(doc.getString("_id"));
        movie.setTitle(doc.getString("title"));
        movie.setDescription(doc.getString("description"));
        movie.setDuration(doc.getInteger("duration"));
        movie.setGenre(doc.getString("genre"));
        movie.setLanguage(doc.getString("language"));
        movie.setReleaseDate(DocumentMapperUtil.toLocalDate(doc.getDate("releaseDate")));
        return movie;
    }
}
