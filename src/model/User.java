package model;

import java.util.ArrayList;
import java.util.UUID;

public abstract class User {

    protected String id;
    protected String name;
    protected String email;
    protected ArrayList<Book> borrowedBooks;
    protected double outstandingFines;
    protected String userType;
    protected int maxBorrowLimit;
    protected int maxBorrowTimeLimit;

    public abstract int getMaxBorrowTimeLimit();
    public abstract int getMaxBorrowLimit();

    public ArrayList<Book> getBorrowedBooks() {return borrowedBooks;}

    public User(String name, String email){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.borrowedBooks = new ArrayList<>();
        this.outstandingFines = 0.0;
    }

    public String getId() {return id;}
    public double getOutstandingFines() {return outstandingFines;}

    protected void printBorrowList(ArrayList<Book> borrowedBooks){
        for (Book book : borrowedBooks) {
            System.out.println("Title: " + book.getTitle() + " | Author: " + book.getAuthor() + " | ISBN: " + book.getIsbn() + " | Reserved: " + book.isReserved() + " | OnLoan: " + book.isOnLoan() );
        }
    }

}