package trial;

import java.time.LocalDate;
import java.sql.Date;
import java.util.Calendar;

public class trial {

    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        LocalDate newLocalDate = localDate.plusDays(5);
        java.sql.Date sqlDate = java.sql.Date.valueOf(newLocalDate);
        System.out.println(sqlDate);
    }

}