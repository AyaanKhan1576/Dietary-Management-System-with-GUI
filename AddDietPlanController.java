package presentation;

import business.services.DietPlanService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddDietPlanController {

    @FXML
    private TextField dietPlanNameField;

    private final DietPlanService dietPlanService = new DietPlanService();

    // Handle adding a new diet plan
    public void handleAddDietPlan() {
        String dietPlanName = dietPlanNameField.getText();

        if (dietPlanName.isEmpty()) {
            // Show an error message or alert if the name is empty
            System.out.println("Please enter a diet plan name.");
            return;
        }

        // Create the diet plan using the service
        try {
            dietPlanService.addDietPlan(dietPlanName);
            System.out.println("Diet plan added successfully!");
        } catch (Exception e) {
            System.err.println("Error adding diet plan: " + e.getMessage());
        }
    }

    // Go back to the previous page (Admin Dashboard)
    public void handleGoBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/DietPlanSearch.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("User Dasboard");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) dietPlanNameField.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            //showAlert("Error", "An error occurred while loading the scene.");
            e.printStackTrace();
        }
    }
}
