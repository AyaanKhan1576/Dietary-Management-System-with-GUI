package data.repositories;

import business.models.DietPlan;
import business.models.FoodItem;
import data.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DietPlanRepository {

	// Fetch all diet plans along with their associated food items
    public List<DietPlan> fetchAllDietPlansWithFoodItems() {
        Map<String, DietPlan> dietPlanMap = new HashMap<>();  // Use String for diet_plan_id as per your DB
        String query = """
                SELECT dp.id AS diet_plan_id, dp.name AS diet_plan_name, 
                       fi.id AS food_item_id, fi.name AS food_item_name, fi.calories AS food_item_calories
                FROM dietplans dp
                LEFT JOIN dietplanfooditems dpf ON dp.id = dpf.dietplanid
                LEFT JOIN fooditems fi ON dpf.fooditemid = fi.id
                ORDER BY dp.id;
                """; // The SQL query to fetch diet plans and food items

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                // Fetch diet plan details
                String dietPlanId = resultSet.getString("diet_plan_id");  // diet_plan_id is a string
                String dietPlanName = resultSet.getString("diet_plan_name");

                // If the diet plan does not already exist in the map, create it
                DietPlan dietPlan = dietPlanMap.computeIfAbsent(dietPlanId,
                    id -> new DietPlan(id, dietPlanName, new ArrayList<>()));

                // Add food items to the diet plan if available
                String foodItemId = resultSet.getString("food_item_id");
                if (!resultSet.wasNull()) { // Ensure food item exists
                    String foodItemName = resultSet.getString("food_item_name");
                    int foodItemCalories = resultSet.getInt("food_item_calories");
                    dietPlan.getFoodItems().add(new FoodItem(foodItemId, foodItemName, foodItemCalories));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching diet plans with food items:");
            e.printStackTrace();
        }
        
        System.out.println("Fetched Diet Plans: " + dietPlanMap.size());

        return new ArrayList<>(dietPlanMap.values());
    }
    
    
    public DietPlan fetchDietPlanForUser(int userId) {
        String query = """
                SELECT dp.id AS diet_plan_id, dp.name AS diet_plan_name, 
                       fi.id AS food_item_id, fi.name AS food_item_name, fi.calories AS food_item_calories
                FROM dietplans dp
                INNER JOIN userdietplans udp ON dp.id = udp.diet_plan_id
                LEFT JOIN dietplanfooditems dpf ON dp.id = dpf.dietplanid
                LEFT JOIN fooditems fi ON dpf.fooditemid = fi.id
                WHERE udp.user_id = ?;
                """;

        DietPlan dietPlan = null;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);  // Set the user ID
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    if (dietPlan == null) {
                        // Initialize the diet plan object on the first row
                        dietPlan = new DietPlan(
                                resultSet.getString("diet_plan_id"),
                                resultSet.getString("diet_plan_name"),
                                new ArrayList<>()
                        );
                    }

                    // Add food items to the diet plan if available
                    String foodItemId = resultSet.getString("food_item_id");
                    if (!resultSet.wasNull()) {
                        String foodItemName = resultSet.getString("food_item_name");
                        int foodItemCalories = resultSet.getInt("food_item_calories");
                        dietPlan.getFoodItems().add(new FoodItem(foodItemId, foodItemName, foodItemCalories));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dietPlan;
    }
    
    
    public List<DietPlan> searchDietPlansByName(String searchTerm) {
        List<DietPlan> dietPlans = new ArrayList<>();
        String query = """
            SELECT id, name 
            FROM DietPlans 
            WHERE LOWER(name) LIKE LOWER(?);
        """;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, "%" + searchTerm + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String name = resultSet.getString("name");
                    dietPlans.add(new DietPlan(id, name, new ArrayList<>()));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error searching for diet plans: " + e.getMessage());
        }

        return dietPlans;
    }


    public boolean updateUserDietPlan(int userId, String dietPlanId) {
        // Validate user_id before proceeding
        if (!userExists(userId)) {
            System.err.println("Error: User ID " + userId + " does not exist in the Users table.");
            return false;
        }

        String selectQuery = """
            SELECT COUNT(*) AS count
            FROM UserDietPlans
            WHERE user_id = ?;
        """;

        String insertQuery = """
            INSERT INTO UserDietPlans (user_id, diet_plan_id, assigned_date)
            VALUES (?, ?, CURRENT_DATE);
        """;

        String updateQuery = """
            UPDATE UserDietPlans
            SET diet_plan_id = ?, assigned_date = CURRENT_DATE
            WHERE user_id = ?;
        """;

        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            // Check if the user already has a diet plan registered
            boolean hasDietPlan;
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                selectStatement.setInt(1, userId);
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    resultSet.next();
                    hasDietPlan = resultSet.getInt("count") > 0;
                }
            }

            // If the user has a diet plan, update it; otherwise, insert a new record
            try (PreparedStatement statement = connection.prepareStatement(hasDietPlan ? updateQuery : insertQuery)) {
                if (hasDietPlan) {
                    statement.setString(1, dietPlanId);
                    statement.setInt(2, userId);
                } else {
                    statement.setInt(1, userId);
                    statement.setString(2, dietPlanId);
                }

                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error updating or inserting user diet plan: " + e.getMessage());
            return false;
        }
    }

    // Helper method to check if a user exists
    private boolean userExists(int userId) {
        String query = "SELECT COUNT(*) FROM Users WHERE id = ?;";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking user existence: " + e.getMessage());
            return false;
        }
    }



    public void insertDietPlan(String name) {
        String query = "SELECT COUNT(*) FROM DietPlans";  // SQL query to count the rows in DietPlans table
        int count = 0;
        	try (Connection connection = DatabaseConnection.getInstance().getConnection();
                 PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    count = resultSet.getInt(1);  // Get the count from the result set
                }

            } catch (SQLException e) {
                System.err.println("Error counting diet plans: " + e.getMessage());
            }
                   
        String id= "D0"+String.valueOf(count+1);
        String query2 = "INSERT INTO DietPlans (name,id) VALUES (?,?);";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement1 = connection.prepareStatement(query2)) {

            statement1.setString(1, name);
            statement1.setString(2, id);
            int rowsAffected = statement1.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Diet plan added successfully.");
            } else {
                System.out.println("Failed to add diet plan.");
            }

        } catch (SQLException e) {
            System.err.println("Error inserting diet plan: " + e.getMessage());
        }
    }

    public boolean deleteDietPlan(String dietPlanId) {
        String query = "DELETE FROM DietPlans WHERE id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, dietPlanId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
