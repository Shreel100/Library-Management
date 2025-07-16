package dao;

import model.Faculty;
import model.Librarian;
import model.Student;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public void addUser(String email, String name, String userType){

        User user = null;
        switch (userType.toLowerCase()) {
            case "student":
                user = new Student(name, email);
                break;
            case "faculty":
                user = new Faculty(name,email);
                break;
            case "librarian":
                user = new Librarian(name,email);
                break;
        }

        String query = "INSERT INTO users.users (id, name, email, outstandingFines, userType) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getId());
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setDouble(4, user.getOutstandingFines());
            stmt.setString(5, userType);

            int rowsInserted = stmt.executeUpdate();
            System.out.println(rowsInserted + " User added.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUser(String email){
        String query = "DELETE FROM users.users WHERE email=?;";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

            int rowsInserted = stmt.executeUpdate();
            System.out.println(rowsInserted + " User removed from DB.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUser(String email, Connection conn) throws SQLException {
        String query = "SELECT * FROM users.users WHERE email = ?";
        User user = null;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String userType = rs.getString("userType");
                    switch (userType.toLowerCase()) {
                        case "student":
                            user = new Student(name, email);
                            break;
                        case "faculty":
                            user = new Faculty(name, email);
                            break;
                        case "librarian":
                            user = new Librarian(name, email);
                            break;
                    }
                }
            }
        }
        return user;
    }

    public double outstandingFine(String email, Connection conn) throws SQLException {
        User user = getUser(email, conn);
        double outStandingFine = user.getOutstandingFines();
        return outStandingFine;
    }

}
