package presentation;

import business.services.FoodItemService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
//import business.services.FoodService;

public class AddFoodListController {

    @FXML
    private TextField foodNameField;
    @FXML
    private TextField caloriesField;
    @FXML
    private TextArea foodDescriptionField;
    
    private final FoodItemService foodItemService = new FoodItemService();
    @FXML
    public void handleAddFood() {
        String foodName = foodNameField.getText();
        String calories = caloriesField.getText();
        
        if (foodName.isEmpty() ||  calories.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        try {
            int calorieValue = Integer.parseInt(calories);
            boolean isAdded = foodItemService.addFoodItem(foodName, calorieValue);

            if (isAdded) {
                showAlert("Success", "Food added successfully.");
            } else {
                showAlert("Error", "Failed to add food item.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid calorie value.");
        }
    }

    @FXML
    public void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/AdminDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Admin Dasboard");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) foodNameField.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            //showAlert("Error", "An error occurred while loading the scene.");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
