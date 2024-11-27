package presentation;

import business.models.Meal;
import business.services.MealLogService;
import business.services.MealService;
import business.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;

public class MealLogController {

    @FXML
    private ListView<Meal> mealListView;

    @FXML
    private Button logMealButton;

    private final MealService mealService = new MealService();
    private final MealLogService mealLogService = new MealLogService();
    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        loadMeals();
    }

    private void loadMeals() {
        List<Meal> meals = mealService.getAllMeals();
        mealListView.getItems().clear();
        mealListView.getItems().addAll(meals);
    }
    
    @FXML
    public void handleGoBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/UserDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("User Dasboard");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) mealListView.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            //showAlert("Error", "An error occurred while loading the scene.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogMeal() {
        Meal selectedMeal = mealListView.getSelectionModel().getSelectedItem();
        if (selectedMeal == null) {
            showAlert("Error", "Please select a meal to log.");
            return;
        }

        int userId = userService.getCurrentUserId();
        boolean success = mealLogService.logMeal(userId, selectedMeal.getName());

        if (success) {
            showAlert("Success", "Meal logged successfully!");
        } else {
            showAlert("Error", "Failed to log meal.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
