package Users;

import Books.Book;
import Jdbc.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class User {

    protected int id;
    protected String name;
    protected String email;
    protected String role;
    protected int maxBorrowingLimit;
    protected int maxBorrowingTimeLimit;
    protected ArrayList<Book> borrowedBooks;
    protected double outstandingFines;

    public User(int id, String name, String email){
        this.id = id;
        this.name = name;
        this.email = email;
        this.borrowedBooks = new ArrayList<>();
        this.outstandingFines = 0.0;
    }

    public int getId() {return id;}
    public String getName() {return name;}
    public String getEmail() {return email;}
    public int getMaxBorrowingLimit() {return maxBorrowingLimit;}
    public int getMaxBorrowingTimeLimit() {return maxBorrowingTimeLimit;}
    public ArrayList<Book> getBorrowedBooks() {return borrowedBooks;}

    public abstract void setMaxBorrowingLimit(int maxBorrowingLimit);
    public abstract void setMaxBorrowingTimeLimit(int maxBorrowingTimeLimit);

    public void addBorrowedBook(Book book) {this.borrowedBooks.add(book);}

    protected void printBorrowList(ArrayList<Book> borrowedBooks){
        for (Book book : borrowedBooks) {
            System.out.println("Title: " + book.getTitle() + " | Author: " + book.getAuthor() + " | ISBN: " + book.getIsbn() + " | Reserved: " + book.isReserve() + " | OnLoan: " + book.isOnLoan() );
        }
    }

    protected void addFine(double amount){outstandingFines+=amount;}

    protected void finePayment(double amount){outstandingFines-=Math.min(amount, outstandingFines);}

    protected void borrowBook(String isbn){
        String query = "UPDATE books SET isOnLoan = '1' WHERE isbn = ?;";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, isbn);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0)
                System.out.println("Book with ISBN " + isbn + " is on Loan");
            else
                System.out.println("No such book found.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
