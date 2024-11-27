package presentation;

import business.services.MealService;
import business.services.UserService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

public class AddMealsController {

    @FXML
    private TextField mealNameField;

    @FXML
    private ChoiceBox<String> mealTypeChoiceBox;

    @FXML
    private TextField mealCaloriesField;

    @FXML
    private TextArea mealDescriptionField;
    
    private final MealService mealService = new MealService();
    
    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        // Add items to the ChoiceBox
        mealTypeChoiceBox.setItems(FXCollections.observableArrayList("Breakfast", "Lunch", "Dinner", "Snack"));
    }

    @FXML
    public void handleAddMeal() {
        // Get input values
        String name = mealNameField.getText();
        String type = mealTypeChoiceBox.getValue();
        String caloriesInput = mealCaloriesField.getText();
        String description = mealDescriptionField.getText();

        // Validate input
        if (name.isEmpty() || type == null || caloriesInput.isEmpty()) {
            showAlert("Error", "Please fill in all required fields: Name, Type, and Calories.");
            return;
        }

        try {
            int calories = Integer.parseInt(caloriesInput); // Convert calories to integer

            // Call the service to add the meal to the database
            boolean success = mealService.addMeal(name, type, calories, description);

            if (success) {
                showAlert("Success", "Meal added successfully!");
                clearFields(); // Clear the fields after successful addition
            } else {
                showAlert("Error", "Failed to add the meal. Please try again.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number for calories.");
        }
    }

    public void handleBackToDashboard() {
    	
    	String fxmlstring;
    	
    	if (userService.isCurrentUserAdmin()) {
    		fxmlstring="/resources/AdminDashboard.fxml";
    	}
    	else {
    		fxmlstring="/resources/UserDashboard.fxml";
    	}
    	
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlstring));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("User Dasboard");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) mealNameField.getScene().getWindow();
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
    
    private void clearFields() {
        mealNameField.clear();
        mealTypeChoiceBox.setValue(null);
        mealCaloriesField.clear();
        mealDescriptionField.clear();
    }
}
