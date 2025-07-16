package dao;

import model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class BorrowedBookDAO {

    public void insertBorrowRecord(String email, Book book, LocalDate issueDate, LocalDate returnDate, Connection conn) throws SQLException {
        String insertQuery = "INSERT INTO users.borrowed_books (email, isbn, title, author, issue_date, return_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setString(1, email);
            stmt.setString(2, book.getIsbn());
            stmt.setString(3, book.getTitle());
            stmt.setString(4, book.getAuthor());
            stmt.setDate(5, java.sql.Date.valueOf(issueDate));
            stmt.setDate(6, java.sql.Date.valueOf(returnDate));
            stmt.executeUpdate();
        }
    }

    public void dropBorrowRecord(String isbn, Connection conn) throws SQLException {
        String deleteQuery = "DELETE FROM users.borrowed_books WHERE isbn = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
            stmt.setString(1, isbn);
            stmt.executeUpdate();
        }
    }

    public java.sql.Date getReturnDate(String isbn, Connection conn) throws SQLException {
        String fetchQuery = "SELECT return_date FROM users.borrowed_books WHERE isbn = ?";
        try (PreparedStatement fetchStmt = conn.prepareStatement(fetchQuery)) {
            fetchStmt.setString(1, isbn);
            ResultSet resultSet = fetchStmt.executeQuery();

            if (resultSet.next()) {
                java.sql.Date returnDate = resultSet.getDate("return_date");
                return returnDate;
            }
            return null;
        }
    }
}
