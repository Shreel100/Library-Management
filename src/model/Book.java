package model;

public class Book {

    String title;
    String author;
    String isbn;
    boolean isReserved;
    boolean isOnLoan;

    public Book(String title, String author, String isbn){
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isReserved = false;
        this.isOnLoan = false;
    }

    public Book(String title, String author, String isbn, boolean isOnLoan, boolean isReserve) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isOnLoan = isOnLoan;
        this.isReserved = isReserve;
    }

    public String getTitle() {return title;}
    public String getAuthor() {return author;}
    public String getIsbn() {return isbn;}
    public boolean isReserved() {return isReserved;}
    public boolean isOnLoan() {return isOnLoan;}

    public void setOnLoan(boolean onLoan) {isOnLoan = onLoan;}

    public void setReserved(boolean reserved) {isReserved = reserved;}

}
