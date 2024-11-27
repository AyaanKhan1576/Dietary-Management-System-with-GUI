package data.repositories;

import business.models.User;
import business.factories.UserFactory;
import data.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    public User findUserByEmail(String email) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String query = "SELECT * FROM Users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String role = resultSet.getString("role");
                String password=resultSet.getString("password");
                return UserFactory.createUser(role, id, name, email, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
