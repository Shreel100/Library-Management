package dao;

import model.Book;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookDAO {

    private UserDAO userDAO = new UserDAO();

    public Book getBook(String isbn, Connection conn){
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
                boolean isOnLoan = resultSet.getBoolean("isOnLoan");
                boolean isReserved = resultSet.getBoolean("isReserved");
                Book book = new Book(title, author, isbnFromDB, isOnLoan, isReserved);
                System.out.println("Title: " + title + "| Author: " + author + "| ISBN: " + isbnFromDB + "| isOnLoan: " + isOnLoan + "| isReserved: " + isReserved);
                return book;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addBook(String title, String author, String isbn) {
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

    public void removeBook(String isbn){
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

    public void setBookOnLoan(String isbn, boolean isOnLoan, Connection conn) throws SQLException {
        String updateQuery = "UPDATE books SET isOnLoan = ? WHERE isbn = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setBoolean(1, isOnLoan);
            stmt.setString(2, isbn);
            stmt.executeUpdate();
        }
    }

    public void updateFine(double amount, double outstandingFine, String email,Connection conn) throws SQLException {
        String updateQuery = "UPDATE books SET outstandingFines = ? WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setDouble(1, outstandingFine+amount);
            stmt.setString(2, email);
            stmt.executeUpdate();
        }
    }

    public void setBookOffLoan(String isbn, boolean isOnLoan, Connection conn) throws SQLException {
        String updateQuery = "UPDATE books SET isOnLoan = ? WHERE isbn = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setBoolean(1, isOnLoan);
            stmt.setString(2, isbn);
            stmt.executeUpdate();
        }
    }

//    public void setBookOnLoan(String isbn, boolean isOnLoan, Connection conn) throws SQLException {
//        String updateQuery = "UPDATE books SET isOnLoan = ? WHERE isbn = ?";
//        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
//            stmt.setBoolean(1, isOnLoan);
//            stmt.setString(2, isbn);
//            stmt.executeUpdate();
//        }
//    }
//
//    public void markAsReturned(String isbn){
//        String updateQuery = "UPDATE books SET isOnLoan = 0 WHERE isbn = ? AND isOnLoan = 1";
//        String fetchQuery = "SELECT * FROM users.borrowed_books WHERE isbn=?";
//        String deleteQuery = "DELETE FROM users.borrowed_books WHERE isbn=?";
//
//        try (Connection conn = DBManager.getConnection()) {
//            conn.setAutoCommit(false);
//
//            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
//                updateStmt.setString(1, isbn);
//                int rowsUpdated = updateStmt.executeUpdate();
//
//                if (rowsUpdated == 0) {
//                    System.out.println("No such book found.");
//                    return;
//                }
//
//                System.out.println("Book with ISBN " + isbn + " is back in library.");
//            }
//
//            try (PreparedStatement fetchStmt = conn.prepareStatement(fetchQuery)){
//                fetchStmt.setString(1, isbn);
//                ResultSet resultSet = fetchStmt.executeQuery();
//
//                while (resultSet.next()) {
//                    // Extract data from the result set
//                    LocalDate currentDate = LocalDate.now();
//                    java.sql.Date returnDate = resultSet.getDate("return_date");
//                    LocalDate returnLocalDate = returnDate.toLocalDate();
//
//                    if (returnLocalDate.isBefore(currentDate)) {
//                        long daysOverdue = ChronoUnit.DAYS.between(returnLocalDate, currentDate);
//                        double finePerDay = 0.50; // adjust this value as needed
//                        double fine = daysOverdue * finePerDay;
////                        updateFine(fine, email, conn);
//                    }
//
//                    System.out.println("Return Date: " + returnLocalDate);
//                }
//            }
//
//            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)){
//                deleteStmt.setString(1, isbn);
//                deleteStmt.executeUpdate();
//            }
//            conn.commit();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

}