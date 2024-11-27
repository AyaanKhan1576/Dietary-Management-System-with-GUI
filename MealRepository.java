package data.repositories;

import business.models.Meal;
import data.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealRepository {

    public List<Meal> fetchAllMeals() {
        String query = "SELECT id, name, type, calories, description FROM Meals";
        List<Meal> meals = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");
                int calories = resultSet.getInt("calories");
                String description = resultSet.getString("description");
                meals.add(new Meal(id, name, type, calories, description));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching meals: " + e.getMessage());
        }

        return meals;
    }
}
