package model;

public class Student extends User {

    public Student(String name, String email) {
        super(name, email);
        this.userType = "Student";
    }

    @Override
    public int getMaxBorrowTimeLimit() {
        return 14;
    }

    @Override
    public int getMaxBorrowLimit() {
        return 3;
    }


}
