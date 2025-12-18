package utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DocumentMapperUtil {
    
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
}
