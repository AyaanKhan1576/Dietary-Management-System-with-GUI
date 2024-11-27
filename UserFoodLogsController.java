package presentation;

import business.models.FoodItemLog;
import business.models.MealLog;
import business.services.FoodItemService;
import business.services.MealLogService;
import business.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class UserFoodLogsController {

    @FXML
    private ListView<String> foodLogListView;

    @FXML
    private ListView<String> mealLogListView;
    
    @FXML
    private Text calorieCountText;
    

    private final FoodItemService foodItemLogService = new FoodItemService();
    private final MealLogService mealLogService = new MealLogService();
    private final UserService userService = new UserService();
    public int userId = userService.getCurrentUserId();

    @FXML
    public void initialize() {
        loadLogs();
        int foodLogCalories = foodItemLogService.getTotalCaloriesConsumedToday(userId);
        int mealLogCalories = mealLogService.getTotalCaloriesConsumedToday(userId);
        int totalCal = foodLogCalories + mealLogCalories;
        
        calorieCountText.setText("Calorie Count: "+ String.valueOf(totalCal));
        
    }

    private void loadLogs() {
        int userId = userService.getCurrentUserId();
        loadFoodLogs(userId);
        loadMealLogs(userId);
    }

    private void loadFoodLogs(int userId) {
        List<FoodItemLog> foodLogs = foodItemLogService.getUserFoodLogsForToday(userId);
        foodLogListView.getItems().clear();

        for (FoodItemLog log : foodLogs) {
            String logEntry = String.format("Food: %s, Calories: %d, Time: %s",
                    log.getFoodId(), log.getTotalCalories(), log.getLogTime().toLocalTime());
            foodLogListView.getItems().add(logEntry);
        }
    }

    private void loadMealLogs(int userId) {
        List<MealLog> mealLogs = mealLogService.fetchUserMealLogsForToday(userId);
        mealLogListView.getItems().clear();

        for (MealLog log : mealLogs) {
            String logEntry = String.format("Meal: %s, Time: %s", log.getMealName(), log.getLoggedAt().toLocalTime());
            mealLogListView.getItems().add(logEntry);
        }
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
            Stage currentStage = (Stage) mealLogListView.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            //showAlert("Error", "An error occurred while loading the scene.");
            e.printStackTrace();
        }
    }
}
