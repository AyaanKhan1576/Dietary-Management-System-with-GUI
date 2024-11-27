package business.services;

import data.DatabaseConnection;
import data.repositories.MealRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import business.models.Meal;

public class MealService {
    private final MealRepository mealRepository = new MealRepository();

    public List<Meal> getAllMeals() {
        return mealRepository.fetchAllMeals();
    }

    // Method to add a meal to the database
    public boolean addMeal(String name, String type, int calories, String description) {
        String query = "INSERT INTO Meals (name, type, calories, description) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set parameters for the query
            statement.setString(1, name);
            statement.setString(2, type);
            statement.setInt(3, calories);
            statement.setString(4, description);

            // Execute the query
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if insertion is successful
        } catch (SQLException e) {
            System.err.println("Error adding meal to database:");
            e.printStackTrace();
        }
        return false; // Return false if insertion fails
    }
}
