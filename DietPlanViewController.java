package presentation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import business.models.DietPlan;
import business.services.DietPlanService;
import data.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DietPlanViewController {

    @FXML
    private Text messageLabel;

    @FXML
    private TextArea dietPlanDetails;

    private final DietPlanService dietPlanService = new DietPlanService();

    @FXML
    public void initialize() {
        loadCurrentUserDietPlan();
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
            Stage currentStage = (Stage) messageLabel.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            //showAlert("Error", "An error occurred while loading the scene.");
            e.printStackTrace();
        }
    }

    private void loadCurrentUserDietPlan() {
        int currentUserId = getCurrentUserId(); // Replace with actual method to get the logged-in user's ID
        DietPlan dietPlan = dietPlanService.getDietPlanForUser(currentUserId);

        if (dietPlan == null) {
            dietPlanDetails.setText("You currently do not\n have a diet plan.");
        } else {
            StringBuilder details = new StringBuilder();
            details.append("Diet Plan Name: ").append(dietPlan.getName()).append("\n");
            details.append("Total Calories: ").append(dietPlan.getTotalCalories()).append("\n");
            details.append("Food Items:\n");
            dietPlan.getFoodItems().forEach(foodItem ->
                    details.append("  - ").append(foodItem.getName())
                            .append(" (").append(foodItem.getCalories()).append(" calories)\n")
            );
            dietPlanDetails.setText(details.toString());
        }
    }

    private int getCurrentUserId() {
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
        return userId; // Replace with the actual implementation
    }
}
