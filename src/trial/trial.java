package trial;

import java.time.LocalDate;
import java.sql.Date;

public class trial {

    public static void main(String[] args) {
        LocalDate currentDate = LocalDate.now();
        LocalDate dueDate = currentDate.plusDays(14);
        java.sql.Date sqlDueDate = Date.valueOf(dueDate);
        System.out.println(sqlDueDate);
    }

}