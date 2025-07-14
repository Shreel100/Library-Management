package Books;

public class Book {

    String title;
    String author;
    String isbn;
    boolean isReserve;
    boolean isOnLoan;

    public Book(String title, String author, String isbn){
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isReserve = false;
        this.isOnLoan = false;
    }

    public String getTitle() {return title;}
    public String getAuthor() {return author;}
    public String getIsbn() {return isbn;}
    public boolean isReserve() {return isReserve;}
    public boolean isOnLoan() {return isOnLoan;}

    public void setOnLoan(boolean onLoan) {isOnLoan = onLoan;}

    public void setReserve(boolean reserve) {isReserve = reserve;}

}
