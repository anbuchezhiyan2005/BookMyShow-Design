package repositories;

import models.Receipt;
import config.MongoDBConnectionManager;
import utils.DocumentMapperUtil;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import org.bson.Document;

public class ReceiptRepositoryImpl implements ReceiptRepository {
    private MongoCollection<Document> collection;

    public ReceiptRepositoryImpl() {
        this.collection = MongoDBConnectionManager.getDatabase().getCollection("receipts");
        createIndexes();
    }

    private void createIndexes() {
        collection.createIndex(Indexes.ascending("bookingId"));
    }

    @Override
    public Receipt save(Receipt receipt) {
        Document doc = receiptToDocument(receipt);
        collection.insertOne(doc);
        return receipt;
    }

    @Override
    public Receipt findById(String receiptId) {
        Document doc = collection.find(Filters.eq("_id", receiptId)).first();
        return doc == null ? null : documentToReceipt(doc);
    }

    @Override
    public Receipt findByBookingId(String bookingId) {
        Document doc = collection.find(Filters.eq("bookingId", bookingId)).first();
        return doc == null ? null : documentToReceipt(doc);
    }

    @Override
    public boolean delete(String receiptId) {
        return collection.deleteOne(Filters.eq("_id", receiptId)).getDeletedCount() > 0;
    }

    private Document receiptToDocument(Receipt receipt) {
        return new Document("_id", receipt.getReceiptId())
                .append("bookingId", receipt.getBookingId())
                .append("generatedDate", DocumentMapperUtil.toDate(receipt.getGeneratedDate()))
                .append("receiptDetails", receipt.getReceiptDetails());
    }

    private Receipt documentToReceipt(Document doc) {
        Receipt receipt = new Receipt();
        receipt.setReceiptId(doc.getString("_id"));
        receipt.setBookingId(doc.getString("bookingId"));
        receipt.setGeneratedDate(DocumentMapperUtil.toLocalDateTime(doc.getDate("generatedDate")));
        receipt.setReceiptDetails(doc.getString("receiptDetails"));
        return receipt;
    }
}
