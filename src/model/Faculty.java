package model;

public class Faculty extends User {

    public Faculty(String name, String email) {
        super(name, email);
        this.userType = "Faculty";
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