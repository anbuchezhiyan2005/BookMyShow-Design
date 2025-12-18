package repositories;

import models.User;
import config.MongoDBConnectionManager;
import utils.DocumentMapperUtil;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import org.bson.Document;

public class UserRepositoryImpl implements UserRepository {
    private MongoCollection<Document> collection;

    public UserRepositoryImpl() {
        this.collection = MongoDBConnectionManager.getDatabase().getCollection("users");
        createIndexes();
    }

    private void createIndexes() {
        collection.createIndex(Indexes.ascending("email"));
    }

    @Override
    public User save(User user) {
        Document doc = userToDocument(user);
        collection.insertOne(doc);
        return user;
    }

    @Override
    public User findById(String userId) {
        Document doc = collection.find(Filters.eq("_id", userId)).first();
        return doc == null ? null : documentToUser(doc);
    }

    @Override
    public User findByEmail(String email) {
        Document doc = collection.find(Filters.eq("email", email)).first();
        return doc == null ? null : documentToUser(doc);
    }

    @Override
    public boolean existsByEmail(String email) {
        return collection.countDocuments(Filters.eq("email", email)) > 0;
    }

    @Override
    public boolean delete(String userId) {
        return collection.deleteOne(Filters.eq("_id", userId)).getDeletedCount() > 0;
    }

    private Document userToDocument(User user) {
        return new Document("_id", user.getUserId())
                .append("name", user.getName())
                .append("email", user.getEmail())
                .append("password", user.getPassword())
                .append("phoneNumber", user.getPhoneNumber())
                .append("createdAt", DocumentMapperUtil.toDate(user.getCreatedAt()));
    }

    private User documentToUser(Document doc) {
        User user = new User();
        user.setUserId(doc.getString("_id"));
        user.setName(doc.getString("name"));
        user.setEmail(doc.getString("email"));
        user.setPassword(doc.getString("password"));
        user.setPhoneNumber(doc.getString("phoneNumber"));
        user.setCreatedAt(DocumentMapperUtil.toLocalDateTime(doc.getDate("createdAt")));
        return user;
    }
}
