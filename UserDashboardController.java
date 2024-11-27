package presentation;

import business.models.User;
import business.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class UserDashboardController implements DashboardLoader {

    @FXML
    private Button addMealsButton;

    @FXML
    private Button backToDashboardButton;
    
    @FXML
    private Text titleText;
    
    private UserService userService = new UserService();

    @Override
    public void loadDashboard(String fxmlFile, String title) {
  
    }

    @FXML
    public void initialize() {
    	int id = userService.getCurrentUserId();
    	User user = userService.getUserById(id);
    	titleText.setText("Welcome Back, "+user.getName());
    }

    @FXML
    public void handleAddMeals() {
        loadNewScene("AddMeals.fxml", "Add Meals");
    }
    
    public void handleLogMeal() {
    	loadNewScene("MealLog.fxml", "Log Meals");

    }
    
    public void handleLogFoodItem() {

    	loadNewScene("FoodItemLog.fxml", "Log Food");

    }
    
    public void handleViewDietPlan() {

    	loadNewScene("DietPlanView.fxml", "Log Meal");

    }
    
    public void handleUserFoodLogs() {

    	loadNewScene("UserFoodLogs.fxml", "Log Meal");

    }
    
    public void handleSearchDietPlan() {
    	loadNewScene("DietPlanSearch.fxml", "Search Diet Plan");
    }
    
    public void handleLogOut() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/HomeScreen.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Home Screen");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) addMealsButton.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            //showAlert("Error", "An error occurred while loading the scene.");
            e.printStackTrace();
        }
    }

    private void loadNewScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/" + fxmlFile));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) addMealsButton.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            showAlert("Error", "An error occurred while loading the scene.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBackToDashboard() {
        // Logic to return to the dashboard or main menu
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/UserDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("User Dashboard");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) backToDashboardButton.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            showAlert("Error", "Failed to load the User Dashboard.");
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
