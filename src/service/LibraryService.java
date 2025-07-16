package service;

import dao.BookDAO;
import dao.BorrowedBookDAO;
import dao.DBManager;
import dao.UserDAO;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LibraryService {

//    private BookDAO bookDAO = new BookDAO();
//    private UserDAO userDAO = new UserDAO();
//    private BorrowedBookDAO borrowedBookDAO = new BorrowedBookDAO();
//
//    protected void issueBook(String email, String isbn) {
//        Book book = bookDAO.getBook(isbn);
//        User user = userDAO.getUser(email);
//        LocalDate issueDate = LocalDate.now();
//        LocalDate returnDate = issueDate.plusDays(user.getMaxBorrowTimeLimit());
//
//        try (Connection conn = DBManager.getConnection()) {
//            conn.setAutoCommit(false);
//            bookDAO.setBookOnLoan(isbn, true, conn);
//            borrowedBookDAO.insertBorrowRecord(email, book, issueDate, returnDate, conn);
//            conn.commit();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    protected void returnBook(String email, String isbn){
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
//                        updateFine(fine, email, conn);
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
//
//    public void updateFine(double amount, String email, Connection conn){
//        String selectQuery = "SELECT * FROM users.users WHERE email = ?";
//        String updateQuery = "UPDATE users.users SET outstandingFines = ? WHERE email = ?";
//
//        // Step 2: Fetch updated row
//        try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
//            selectStmt.setString(1, email);
//            try (ResultSet rs = selectStmt.executeQuery()) {
//                while (rs.next()) {
//                    double fines = rs.getDouble("outstandingFines");
//                    double finalAmount = fines+amount;
//
//                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
//                        updateStmt.setDouble(1, finalAmount);
//                        updateStmt.setString(2, email);
//                        int rowsUpdated = updateStmt.executeUpdate();
//
//                        if (rowsUpdated == 0) {
//                            System.out.println("No User found.");
//                            return;
//                        }
//
//                        System.out.println("Fines updated for User: " + email);
//                    }
//
//                    System.out.println("Outstanding Fine for " + email + ": "+ finalAmount);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        LibraryService l = new LibraryService();
//        l.returnBook("narendra.modi@gmail.com", "978-0-572-31766-9");
//    }
}
