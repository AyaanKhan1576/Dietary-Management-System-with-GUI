package business.services;

import business.models.Admin;
import business.models.HealthInfo;
import business.models.RegularUser;
import business.models.User;
import data.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class UserService {

    public static final String ROLE_ADMIN = "Admin";
    public static final String ROLE_USER = "User";

    public int getCurrentUserId() {
        String sql = "SELECT user_id FROM UserLog ORDER BY logid DESC LIMIT 1";
    	 int userId = -1;
         try (Connection conn =DatabaseConnection.getInstance().getConnection();
              Statement stmt = conn.createStatement();
              ResultSet rs = stmt.executeQuery(sql)) {

             if (rs.next()) {
                 userId = rs.getInt("user_id");
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return userId;
    }

    // Logs in a user by verifying their email
    public User login(String email) {
        String query = "SELECT * FROM Users WHERE email = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String role = resultSet.getString("role");
                String password = resultSet.getString("password");

                if (ROLE_ADMIN.equalsIgnoreCase(role)) {
                    return new Admin(id, name, email, password);
                } else if (ROLE_USER.equalsIgnoreCase(role)) {
                    return new RegularUser(id, name, email, password);
                } else {
                    throw new IllegalArgumentException("Unexpected role: " + role);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error logging in user:");
            e.printStackTrace();
        }
        return null;
    }

    // Validates the password entered by the user
    public boolean validatePassword(User user, String inputPassword) {
        return user.getPassword().equals(inputPassword);
    }

    public boolean addLoginLog(int userId) {
        String sql = "INSERT INTO UserLog (user_id, loginTime) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now()));  // Convert LocalDateTime to Timestamp
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error inserting login log for user ID: " + userId);
            e.printStackTrace();
            return false;
        }
    }

    
    // Registers a new user
    public int signup(String name, String email, String password) {
        if (emailExists(email)) {
            System.out.println("Email already exists: " + email);
            return -2;
        }

        String query = "INSERT INTO Users (name, email, role, password) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, ROLE_USER);
            statement.setString(4, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Error during signup:");
            e.printStackTrace();
        }
        return -1;
    }

    // Checks if an email already exists in the database
    private boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM Users WHERE email = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking email existence:");
            e.printStackTrace();
        }
        return false;
    }

    // Saves health information for a user
    public boolean saveHealthInfo(int userId, double height, double weight, int age, String allergies, String diseases, String goal) {
        String query = "INSERT INTO HealthInfo (user_id, height, weight, age, allergies, diseases, goal) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setDouble(2, height);
            statement.setDouble(3, weight);
            statement.setInt(4, age);
            statement.setString(5, allergies);
            statement.setString(6, diseases);
            statement.setString(7, goal);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saving health information:");
            e.printStackTrace();
        }
        return false;
    }

    // Retrieves all users with the specified role from the database
    public List<User> getAllUsersWithRole(String role) {
        String query = "SELECT * FROM Users WHERE role = ?";
        List<User> users = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, role);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                if ("Admin".equalsIgnoreCase(role)) {
                    users.add(new Admin(id, name, email, password));  // Assuming Admin is a subclass of User
                } else if ("User".equalsIgnoreCase(role)) {
                    users.add(new RegularUser(id, name, email, password));  // Assuming RegularUser is a subclass of User
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    // Retrieves a user by their ID
    public User getUserById(int userId) {
        String query = "SELECT * FROM Users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String role = resultSet.getString("role");
                String password = resultSet.getString("password");

                if (ROLE_ADMIN.equalsIgnoreCase(role)) {
                    return new Admin(id, name, email, password);
                } else if (ROLE_USER.equalsIgnoreCase(role)) {
                    return new RegularUser(id, name, email, password);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user by ID:");
            e.printStackTrace();
        }
        return null;
    }

    // Retrieves health information by user ID
    public HealthInfo getHealthInfo(int userId) {
        String query = "SELECT * FROM HealthInfo WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                double height = resultSet.getDouble("height");
                double weight = resultSet.getDouble("weight");
                int age = resultSet.getInt("age");
                String allergies = resultSet.getString("allergies");
                String diseases = resultSet.getString("diseases");
                String goal = resultSet.getString("goal");

                return new HealthInfo(userId, height, weight, age, allergies, diseases, goal);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching health information:");
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean isCurrentUserAdmin() {
        String sql = "SELECT user_id FROM UserLog ORDER BY logid DESC LIMIT 1";
    	 int userId = -1;
         try (Connection conn =DatabaseConnection.getInstance().getConnection();
              Statement stmt = conn.createStatement();
              ResultSet rs = stmt.executeQuery(sql)) {

             if (rs.next()) {
                 userId = rs.getInt("user_id");
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
    	
        String query = "SELECT role FROM Users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");
                return "Admin".equalsIgnoreCase(role); // Check if the role is "Admin"
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Default to false if no result or error occurs
    }
}
