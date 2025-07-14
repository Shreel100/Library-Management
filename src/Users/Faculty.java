package Users;

public class Faculty extends User{

    public Faculty(int id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public void setMaxBorrowingLimit(int maxBorrowingLimit) {
        this.maxBorrowingLimit = 5;
    }

    @Override
    public void setMaxBorrowingTimeLimit(int maxBorrowingTimeLimit) {
        this.maxBorrowingTimeLimit = 28;
    }
    }