package utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.bson.Document;

public class DocumentMapperUtil {
    
    private static final DateTimeFormatter ISO_DATETIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    private static final DateTimeFormatter ISO_DATE_FORMATTER = DateTimeFormatter.ISO_DATE;
    
    // LocalDateTime to Date conversion
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    // Date to LocalDateTime conversion
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    // LocalDate to Date conversion
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    // Date to LocalDate conversion
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // Get current timestamp
    public static Date now() {
        return Date.from(Instant.now());
    }

    // Epoch milliseconds to Date
    public static Date fromEpochMilli(long epochMilli) {
        return new Date(epochMilli);
    }

    // Date to epoch milliseconds
    public static long toEpochMilli(Date date) {
        if (date == null) {
            return 0L;
        }
        return date.getTime();
    }
    
    /**
     * Safely get a Date from a Document field that might be stored as either Date or String
     * Handles MongoDB BSON Date objects and ISO-formatted date strings
     */
    public static Date safeGetDate(Document doc, String fieldName) {
        if (doc == null || fieldName == null) {
            return null;
        }
        
        Object value = doc.get(fieldName);
        if (value == null) {
            return null;
        }
        
        // If it's already a Date object, return it
        if (value instanceof Date) {
            return (Date) value;
        }
        
        // If it's a String, parse it
        if (value instanceof String) {
            try {
                String dateString = (String) value;
                LocalDateTime ldt = LocalDateTime.parse(dateString, ISO_DATETIME_FORMATTER);
                return toDate(ldt);
            } catch (Exception e) {
                System.err.println("Failed to parse date string: " + value + " for field: " + fieldName);
                return null;
            }
        }
        
        return null;
    }
    
    /**
     * Safely get a LocalDateTime from a Document field that might be stored as either Date or String
     */
    public static LocalDateTime safeGetLocalDateTime(Document doc, String fieldName) {
        Date date = safeGetDate(doc, fieldName);
        return toLocalDateTime(date);
    }
    
    /**
     * Safely get a LocalDate from a Document field that might be stored as either Date or String
     */
    public static LocalDate safeGetLocalDate(Document doc, String fieldName) {
        Date date = safeGetDate(doc, fieldName);
        return toLocalDate(date);
    }
}
