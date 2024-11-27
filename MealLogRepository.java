package data.repositories;

import data.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import business.models.MealLog;

public class MealLogRepository {
	
	

    public boolean logMeal(int userId, String mealName) {
        String query = "INSERT INTO MealLog (user_id, meal_name) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setString(2, mealName);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error logging meal: " + e.getMessage());
        }

        return false;
    }
    
    public List<MealLog> fetchUserMealLogsForToday(int userId) {
        String query = """
            SELECT id, user_id, meal_name, logged_at
            FROM MealLog
            WHERE user_id = ? AND DATE(logged_at) = CURRENT_DATE;
        """;
        List<MealLog> logs = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String mealName = resultSet.getString("meal_name");
                    LocalDateTime loggedAt = resultSet.getTimestamp("logged_at").toLocalDateTime();

                    logs.add(new MealLog(id, userId, mealName, loggedAt));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching meal logs: " + e.getMessage());
        }

        return logs;
    }
    
    public int getTotalCaloriesForToday(int userId) {
        int totalCalories = 0;

        String query = """
            SELECT SUM(me.calories) AS total_calories
            FROM MealLog m
            JOIN Meals me ON m.meal_name = me.name
            WHERE m.user_id = ? AND DATE(m.logged_at) = CURRENT_DATE;
        """;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);  // Set the user ID

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    totalCalories = resultSet.getInt("total_calories");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalCalories;
    }
}
