package service;

import dao.BookDAO;
import dao.BorrowedBookDAO;
import dao.DBManager;
import dao.UserDAO;
import model.Book;
import model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class trial {

    private BookDAO bookDAO = new BookDAO();
    private UserDAO userDAO = new UserDAO();
    private BorrowedBookDAO borrowedBookDAO = new BorrowedBookDAO();

    protected void issueBook(String email, String isbn) {

        try (Connection conn = DBManager.getConnection()) {
            Book book = bookDAO.getBook(isbn);
            User user = userDAO.getUser(email, conn);
            LocalDate issueDate = LocalDate.now();
            LocalDate returnDate = issueDate.plusDays(user.getMaxBorrowTimeLimit());
            conn.setAutoCommit(false);
            bookDAO.setBookOnLoan(isbn, true, conn);
            borrowedBookDAO.insertBorrowRecord(email, book, issueDate, returnDate, conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void returnBook(String email, String isbn){

        try (Connection conn = DBManager.getConnection()) {
            conn.setAutoCommit(false);
            bookDAO.setBookOffLoan(isbn,false,conn);
            double outstandingFine = userDAO.outstandingFine(email, conn);
            LocalDate currentDate = LocalDate.now();
            java.sql.Date returnDate = borrowedBookDAO.getReturnDate(isbn, conn);
            LocalDate returnLocalDate = returnDate.toLocalDate();
            conn.setAutoCommit(false);
            if (returnLocalDate.isBefore(currentDate)) {
                long daysOverdue = ChronoUnit.DAYS.between(returnLocalDate, currentDate);
                double finePerDay = 0.50; // adjust this value as needed
                double fine = daysOverdue * finePerDay;
                bookDAO.updateFine(fine, outstandingFine, email, conn);
            }
            borrowedBookDAO.dropBorrowRecord(isbn, conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        trial t = new trial();
        t.issueBook("shreel.patel@gmail.com", "978-0-15-258994-3");
    }

}
