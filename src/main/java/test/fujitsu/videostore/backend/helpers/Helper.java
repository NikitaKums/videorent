package test.fujitsu.videostore.backend.helpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Helper {
    // Notice! In code the date format pattern was said to be "dd-MM-YY" whereas word document's pattern "dd.MM.YYYY".
    // Using word document date pattern.
    public static String FormatDate(LocalDate date){
        return DateTimeFormatter.ofPattern("dd.MM.YYYY").format(date);
    }
}
