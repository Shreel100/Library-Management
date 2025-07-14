package Users;

import Jdbc.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Librarian extends User{

    public Librarian(int id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public void setMaxBorrowingLimit(int maxBorrowingLimit) {
        this.maxBorrowingLimit = 5;
    }

    @Override
    public void setMaxBorrowingTimeLimit(int maxBorrowingTimeLimit) {
        this.maxBorrowingTimeLimit = 28;
    }


}
