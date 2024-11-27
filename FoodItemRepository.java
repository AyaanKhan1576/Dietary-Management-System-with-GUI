package data.repositories;

import business.models.FoodItem;
import business.models.FoodItemLog;
import data.DatabaseConnection;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class FoodItemRepository {

	
	
    public List<FoodItem> getAllFoodItems() {
        List<FoodItem> foodItems = new ArrayList<>();
        String query = "SELECT * FROM fooditems";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                int calories = resultSet.getInt("calories");
                foodItems.add(new FoodItem(id, name, calories));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodItems;
    }

    public List<FoodItemLog> fetchUserFoodLogsForToday(int userId) {
        String query = """
            SELECT logID, user_id, food_id, totalCalories, logTime
            FROM FoodItemLog
            WHERE user_id = ? AND DATE(logTime) = CURRENT_DATE;
        """;
        List<FoodItemLog> logs = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int logId = resultSet.getInt("logID");
                    String foodId = resultSet.getString("food_id");
                    int totalCalories = resultSet.getInt("totalCalories");
                    LocalDateTime logTime = resultSet.getTimestamp("logTime").toLocalDateTime();

                    logs.add(new FoodItemLog(logId, userId, foodId, totalCalories, logTime));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching food item logs: " + e.getMessage());
        }

        return logs;
    }
    
    // Method to generate the next food item ID (e.g., F003)
    public String getNextFoodItemId() {
        String query = "SELECT MAX(id) AS max_id FROM fooditems";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                String maxId = resultSet.getString("max_id");
                if (maxId != null) {
                    // Increment the number part of the ID (e.g., F003 -> F004)
                    int currentId = Integer.parseInt(maxId.substring(1));
                    int nextId = currentId + 1;
                    return String.format("F%03d", nextId); // e.g., F004
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "F001"; // Default to "F001" if no records exist
    }

    // Method to add a new food item to the database
    public boolean addFoodItem(FoodItem foodItem) {
        String query = "INSERT INTO fooditems (id, name, calories) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, foodItem.getId());
            statement.setString(2, foodItem.getName());
            statement.setInt(3, foodItem.getCalories());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Method to search for food items based on name, id, or calorie range
    public List<FoodItem> searchFoodItems(String name, String id, Integer minCalories, Integer maxCalories) {
        List<FoodItem> foodItems = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM fooditems WHERE 1=1");

        // Check conditions and append OR for matching any of the criteria
        boolean hasCondition = false;

        if (name != null && !name.isEmpty()) {
            queryBuilder.append(" AND name LIKE ?");
            hasCondition = true;
        }
        if (id != null && !id.isEmpty()) {
            if (hasCondition) {
                queryBuilder.append(" AND id = ?");
            } else {
                queryBuilder.append(" AND id = ?");
                hasCondition = true;
            }
        }
        if (minCalories != null) {
            if (hasCondition) {
                queryBuilder.append(" AND calories >= ?");
            } else {
                queryBuilder.append(" AND calories >= ?");
                hasCondition = true;
            }
        }
        if (maxCalories != null) {
            if (hasCondition) {
                queryBuilder.append(" AND calories <= ?");
            } else {
                queryBuilder.append(" AND calories <= ?");
                hasCondition = true;
            }
        }

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {

            int paramIndex = 1;
            if (name != null && !name.isEmpty()) {
                statement.setString(paramIndex++, "%" + name + "%");
            }
            if (id != null && !id.isEmpty()) {
                statement.setString(paramIndex++, id);
            }
            if (minCalories != null) {
                statement.setInt(paramIndex++, minCalories);
            }
            if (maxCalories != null) {
                statement.setInt(paramIndex++, maxCalories);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String foodId = resultSet.getString("id");
                    String foodName = resultSet.getString("name");
                    int foodCalories = resultSet.getInt("calories");
                    foodItems.add(new FoodItem(foodId, foodName, foodCalories));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return foodItems;

    }
    
    public int getTotalCaloriesForToday(int userId) {
        int totalCalories = 0;

        String query = """
            SELECT SUM(fi.calories) AS total_calories
            FROM FoodItemLog fil
            JOIN FoodItems fi ON fil.food_id = fi.id
            WHERE fil.user_id = ? AND DATE(fil.logTime) = CURRENT_DATE;
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


