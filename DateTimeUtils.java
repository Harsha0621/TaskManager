import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtils {

    private static final String dateTimeFormat = "EEE MMM dd HH:mm:ss z yyyy";

    public static Date fromStringToDate(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        ZonedDateTime zdt = ZonedDateTime.parse(dateTimeString, formatter);
        return Date.from(zdt.toInstant());
    }

}
