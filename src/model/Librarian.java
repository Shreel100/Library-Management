package model;

public class Librarian extends User {

    public Librarian(String name, String email) {
        super(name, email);
        this.userType = "Librarian";
    }

    @Override
    public int getMaxBorrowTimeLimit() {
        return 28;
    }

    @Override
    public int getMaxBorrowLimit() {
        return 5;
    }

}