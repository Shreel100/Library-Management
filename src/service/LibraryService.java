package service;

import dao.DBManager;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class LibraryService {

    Date date = new Date();
    Calendar calendar = Calendar.getInstance();

    public static Book getBook(String isbn){
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

    public User getUser(String email){
        String query = "SELECT * FROM users.users WHERE email = ?";
        User user = null;

        try(Connection conn = DBManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UUID id = UUID.fromString(rs.getString("id"));
                    String name = rs.getString("name");
                    int outstandingFines = rs.getInt("outstandingFines");
                    String userType = rs.getString("userType");
                    switch (userType.toLowerCase()) {
                        case "student":
                            user = new Student(name, email);
                            break;
                        case "faculty":
                            user = new Faculty(name,email);
                            break;
                        case "librarian":
                            user = new Librarian(name,email);
                            break;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    protected void issueBook(String email, String isbn) {
        String updateQuery = "UPDATE books SET isOnLoan = 1 WHERE isbn = ? AND isOnLoan = 0";
        String selectQuery = "SELECT * FROM books WHERE isbn = ?";
        String insertQuery = "INSERT INTO users.borrowed_books (email, isbn, title, author, issue_date, return_date) VALUES (?, ?, ?, ?, ?, ?)";

        Book tempbook = getBook(isbn);
        User user = getUser(email);
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        calendar.setTime(sqlDate);
        calendar.add(Calendar.DAY_OF_YEAR, user.getMaxBorrowTimeLimit());
        java.sql.Date dueDate = new java.sql.Date(calendar.getTime().getTime());

        try (Connection conn = DBManager.getConnection()) {

            // Step 1: Update
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setString(1, isbn);
                int rowsUpdated = updateStmt.executeUpdate();

                if (rowsUpdated == 0) {
                    System.out.println("No such book found.");
                    return;
                }

                System.out.println("Book with ISBN " + isbn + " is now on loan.");
            }

            // Step 2: Fetch updated row
            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                selectStmt.setString(1, isbn);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    while (rs.next()) {
                        String title = rs.getString("title");
                        String author = rs.getString("author");
                        boolean isOnLoan = rs.getBoolean("isOnLoan");
                        boolean isReserved = rs.getBoolean("isReserved");


                        // Use appropriate constructor
                        Book book = new Book(title, author, isbn, isOnLoan, isReserved);

                        // Optionally do something with the book object
                        System.out.println("Fetched: " + book.getTitle());
                    }
                }
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, email);
                insertStmt.setString(2, isbn);
                insertStmt.setString(3, tempbook.getTitle());
                insertStmt.setString(4, tempbook.getAuthor());
                insertStmt.setDate(5, sqlDate);
                insertStmt.setDate(6, dueDate);
                insertStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void returnBook(String email, String isbn){
        String updateQuery = "UPDATE books SET isOnLoan = 0 WHERE isbn = ? AND isOnLoan = 1";
        String fetchQuery = "SELECT * FROM users.borrowed_books WHERE isbn=?";
        String deleteQuery = "DELETE FROM users.borrowed_books WHERE isbn=?";

        try (Connection conn = DBManager.getConnection()) {

            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setString(1, isbn);
                int rowsUpdated = updateStmt.executeUpdate();

                if (rowsUpdated == 0) {
                    System.out.println("No such book found.");
                    return;
                }

                System.out.println("Book with ISBN " + isbn + " is back in library.");
            }

            try (PreparedStatement fetchStmt = conn.prepareStatement(fetchQuery)){
                fetchStmt.setString(1, isbn);
                ResultSet resultSet = fetchStmt.executeQuery();

                while (resultSet.next()) {
                    // Extract data from the result set
                    LocalDate currentDate = LocalDate.now();
                    java.sql.Date returnDate = resultSet.getDate("return_date");
                    LocalDate returnLocalDate = returnDate.toLocalDate();

                    if (returnLocalDate.isBefore(currentDate)) {
                        long daysOverdue = ChronoUnit.DAYS.between(returnLocalDate, currentDate);
                        double finePerDay = 0.50; // adjust this value as needed
                        double fine = daysOverdue * finePerDay;
                        updateFine(fine, email);
                        System.out.println("The fine is $" + fine);
                    }

                    System.out.println("Return Date: " + returnLocalDate);
                }
            }

            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)){
                deleteStmt.setString(1, isbn);
                deleteStmt.executeUpdate();
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void addUser(String email, String name, String userType){

        User user = null;
        switch (userType.toLowerCase()) {
            case "student":
                user = new Student(name, email);
                break;
            case "faculty":
                user = new Faculty(name,email);
                break;
            case "librarian":
                user = new Librarian(name,email);
                break;
        }

        String query = "INSERT INTO users.users (id, name, email, outstandingFines, userType) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getId());
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setDouble(4, user.getOutstandingFines());
            stmt.setString(5, userType);

            int rowsInserted = stmt.executeUpdate();
            System.out.println(rowsInserted + " User added.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUser(String email){
        String query = "DELETE FROM users.users WHERE email=?;";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

            int rowsInserted = stmt.executeUpdate();
            System.out.println(rowsInserted + " User removed from DB.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateFine(double amount, String email){
        String selectQuery = "SELECT * FROM users.users WHERE email = ?";
        String updateQuery = "UPDATE users.users SET outstandingFines = ? WHERE email = ?";

        try (Connection conn = DBManager.getConnection()) {

            // Step 2: Fetch updated row
            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                selectStmt.setString(1, email);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    while (rs.next()) {
                        double fines = rs.getDouble("outstandingFines");
                        double finalAmount = fines+amount;

                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setDouble(1, finalAmount);
                            updateStmt.setString(2, email);
                            int rowsUpdated = updateStmt.executeUpdate();

                            if (rowsUpdated == 0) {
                                System.out.println("No User found.");
                                return;
                            }

                            System.out.println("Fines updated for User: " + email);
                        }

                        System.out.println("Outstanding Fine for " + email + ": "+ finalAmount);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LibraryService l = new LibraryService();
        l.returnBook("shreel.patel@gmail.com", "978-1-07-155663-4");
    }
}
