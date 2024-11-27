package business.services;

import business.models.FoodItem;
import business.models.FoodItemLog;
import data.DatabaseConnection;
import data.repositories.FoodItemRepository;

import java.sql.*;
import java.util.List;

public class FoodItemService {
	
	private final FoodItemRepository foodItemRepository = new FoodItemRepository();


    public List<FoodItemLog> getUserFoodLogsForToday(int userId) {
        return foodItemRepository.fetchUserFoodLogsForToday(userId);
    }
    
    public int getTotalCaloriesConsumedToday(int userId) {
        return foodItemRepository.getTotalCaloriesForToday(userId);
    }
    
    public boolean addFoodToLog(String string) {
        String sql = "INSERT INTO MealLog (logID) VALUES (?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	

            pstmt.setString(1, string);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean addFoodItem(String name, int calories) {
        // Get the next ID in the sequence (e.g., F003)
        String id = foodItemRepository.getNextFoodItemId();

        // Create a new FoodItem object
        FoodItem foodItem = new FoodItem(id, name, calories);

        // Save to the database
        return foodItemRepository.addFoodItem(foodItem);
    }
    
    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.getAllFoodItems();
    }
    
 
    
    public List<FoodItem> searchFoodItems(String name, String id, Integer minCalories, Integer maxCalories) {
        return foodItemRepository.searchFoodItems(name, id, minCalories, maxCalories);
    }
    
    public Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }

}
