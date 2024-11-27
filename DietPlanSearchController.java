package presentation;

import business.models.DietPlan;
import business.services.DietPlanService;
import business.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.List;

public class DietPlanSearchController {
	
    @FXML
    private Button addPlanButton;
    
    @FXML
    private Button removePlanButton;
    
    @FXML
    private Button selectPlanButton;
	
    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> resultsListView;

    private final DietPlanService dietPlanService = new DietPlanService();
    private final UserService userService = new UserService();
    
    public void initialize() {
        // Hide or show the buttons based on user role
        if (userService.isCurrentUserAdmin()) {
            // If user is admin, hide the "Select" button
            selectPlanButton.setVisible(false);
            
            // Show "Add" and "Remove" buttons only for admins
            addPlanButton.setVisible(true);
            removePlanButton.setVisible(true);
        } else {
            // If user is not admin, hide the "Add" and "Remove" buttons
            addPlanButton.setVisible(false);
            removePlanButton.setVisible(false);
            
            // Show "Select" button only for non-admin users
            selectPlanButton.setVisible(true);
        }
    }
    
    @FXML
    public void handleSearch() {
        String searchTerm = searchField.getText();

        if (searchTerm.isEmpty()) {
            resultsListView.getItems().clear();
            resultsListView.getItems().add("Please enter a search term.");
            return;
        }

        List<DietPlan> results = dietPlanService.searchDietPlans(searchTerm);

        resultsListView.getItems().clear();
        if (results.isEmpty()) {
            resultsListView.getItems().add("No diet plans found.");
        } else {
            for (DietPlan dietPlan : results) {
                resultsListView.getItems().add(dietPlan.getId() + ": " + dietPlan.getName());
            }
        }
    }

    @FXML
    public void handleSelectPlan() {
        // Get the selected item from the list view
        String selectedPlan = resultsListView.getSelectionModel().getSelectedItem();

        if (selectedPlan == null) {
            showAlert("No Selection", "Please select a diet plan from the list.");
            return;
        }

        // Extract diet plan ID from the selected item
        String dietPlanId = extractDietPlanId(selectedPlan);

        if (dietPlanId == null) {
            showAlert("Invalid Selection", "Failed to extract the selected diet plan ID.");
            return;
        }

        // Get the current user ID
        int currentUserId = userService.getCurrentUserId();

        // Update the user's diet plan
        boolean isUpdated = dietPlanService.updateUserDietPlan(currentUserId, dietPlanId);

        if (isUpdated) {
            showAlert("Success", "Your diet plan has been updated to " + selectedPlan + ".");
        } else {
            showAlert("Error", "Failed to update your diet plan. Please try again.");
        }
    }
    
    private String extractDietPlanId(String selectedPlan) {
        // Assume the selectedPlan is in the format "ID: Plan Name"
        if (selectedPlan.contains(":")) {
            return selectedPlan.split(":")[0].trim(); // Extract ID before ":"
        }
        return null; // Return null if format is invalid
    }



    @FXML
    public void handleAddPlan() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/AddDietPlan.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("User Dasboard");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) searchField.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            //showAlert("Error", "An error occurred while loading the scene.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRemovePlan() {
        if (!userService.isCurrentUserAdmin()) {
            showAlert("Permission Denied", "Only admins can delete a diet plan.");
            return;
        }

        String selectedPlan = resultsListView.getSelectionModel().getSelectedItem();
        if (selectedPlan == null) {
            showAlert("No Selection", "Please select a plan to delete.");
            return;
        }

        String dietPlanId = selectedPlan.split(":")[0].trim(); // Extract ID from the selected item
        boolean isDeleted = dietPlanService.deleteDietPlan(dietPlanId);

        if (isDeleted) {
            showAlert("Success", "Diet plan has been successfully deleted.");
            resultsListView.getItems().remove(selectedPlan);
        } else {
            showAlert("Error", "Failed to delete the diet plan.");
        }
    }
    
    public void handleGoBack() {
    	
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
            Stage currentStage = (Stage) searchField.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            //showAlert("Error", "An error occurred while loading the scene.");
            e.printStackTrace();
        }
       
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
