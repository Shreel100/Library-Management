package Books;

import Jdbc.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Inventory {

    public void addBookToDB(String title, String author, String isbn) {
        String query = "INSERT INTO books (title, author, isbn, isOnLoan, isReserved) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setString(3, isbn);
            stmt.setBoolean(4, false); // isOnLoan
            stmt.setBoolean(5, false); // isReserved

            int rowsInserted = stmt.executeUpdate();
            System.out.println(rowsInserted + " book(s) added.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeBookFromDB(String isbn){
        String query = "DELETE FROM books WHERE isbn = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, isbn);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0)
                System.out.println("Book with ISBN " + isbn + " is removed from database");
            else
                System.out.println("No such book found.");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void searchBookInDB(String isbn){
        String query = "SELECT * FROM books WHERE isbn = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, isbn);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                // Extract data from the result set
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String isbnFromDB = resultSet.getString("isbn");
                int isOnLoan = resultSet.getInt("isOnLoan");
                int isReserved = resultSet.getInt("isReserved");
                System.out.println("Title: " + title + "| Author: " + author + "| ISBN: " + isbnFromDB + "| isOnLoan: " + isOnLoan + "| isReserved: " + isReserved);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}