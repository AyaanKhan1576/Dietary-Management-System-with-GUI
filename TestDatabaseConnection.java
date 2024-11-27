package data;

import data.repositories.FoodItemRepository;
import business.models.FoodItem;

import java.util.List;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        // Test Database Connection
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        if (databaseConnection.getConnection() != null) {
            System.out.println("Database connection established successfully!");
        } else {
            System.err.println("Failed to establish a database connection.");
        }

        // Test FoodItemRepository
        FoodItemRepository foodItemRepository = new FoodItemRepository();
        List<FoodItem> foodItems = foodItemRepository.getAllFoodItems();

        // Print all retrieved food items
        if (foodItems.isEmpty()) {
            System.out.println("No food items found in the database.");
        } else {
            System.out.println("Food Items retrieved from the database:");
            foodItems.forEach(System.out::println);
        }
    }
}
