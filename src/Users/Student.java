package Users;

public class Student extends User {

    public Student(int id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public void setMaxBorrowingLimit(int maxBorrowingLimit) {
        this.maxBorrowingLimit = 3;
    }

    @Override
    public void setMaxBorrowingTimeLimit(int maxBorrowingTimeLimit) {
        this.maxBorrowingTimeLimit = 14;
    }

}
