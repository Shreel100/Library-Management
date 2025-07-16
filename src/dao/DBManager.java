package dao;

import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/books";
    private static final String USER = "root";

    private static Connection connection;

    private DBManager(){}

    public static Connection getConnection() throws SQLException {

        if (connection == null || connection.isClosed()) {
            Console console = System.console();

            if (console == null) {
                System.out.println("No console available.");
            }

            char[] password = console.readPassword("Enter your password: ");

            String PASS = new String(password);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            // Clear the password from memory
            java.util.Arrays.fill(password, ' ');
            PASS = null;
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}