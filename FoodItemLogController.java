package presentation;

import business.models.FoodItem;
import business.services.FoodItemService;
import data.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class FoodItemLogController {

    @FXML
    private TableView<FoodItem> foodTable;
    @FXML
    private TableColumn<FoodItem, String> idColumn;
    @FXML
    private TableColumn<FoodItem, String> nameColumn;
    @FXML
    private TableColumn<FoodItem, Integer> caloriesColumn;
    @FXML
    private TextField userIdField;
    @FXML
    private Label statusLabel;

    private final FoodItemService foodItemService = new FoodItemService();

    @FXML
    public void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        caloriesColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCalories()).asObject());

        // Load meals into the table
        loadFoodItems();
    }
    
    public void handleGoBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/UserDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("User Dasboard");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) foodTable.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            //showAlert("Error", "An error occurred while loading the scene.");
            e.printStackTrace();
        }
    }

    private void loadFoodItems() {
        List<FoodItem> foodItems = foodItemService.getAllFoodItems();
        ObservableList<FoodItem> foodItemList = FXCollections.observableArrayList(foodItems);
        foodTable.setItems(foodItemList);
    }

    @FXML
    public void logSelectedMeal() {
        FoodItem selectedFoodItem = foodTable.getSelectionModel().getSelectedItem();
        String sql = "SELECT user_id FROM UserLog ORDER BY loginTime DESC LIMIT 1";
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

        if (selectedFoodItem != null) {
            boolean success = logMealToDatabase(String.valueOf(userId), selectedFoodItem);

            if (success) {
                statusLabel.setText("Meal logged successfully!");
            } else {
                statusLabel.setText("Failed to log meal. Please try again.");
            }
        } else {
            statusLabel.setText("Please select a meal to log.");
        }
    }

    private boolean logMealToDatabase(String userId, FoodItem foodItem) {
        try (Connection conn = foodItemService.getConnection()) {
            String query = "INSERT INTO FoodItemLog (user_id, food_id, totalCalories, logTime) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, Integer.parseInt(userId));
                stmt.setString(2, foodItem.getId());
                stmt.setInt(3, foodItem.getCalories());
                stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

                stmt.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
