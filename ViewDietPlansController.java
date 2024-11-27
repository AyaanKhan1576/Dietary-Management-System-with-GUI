package presentation;

import business.models.DietPlan;
import business.models.DietPlanViewModel;
import business.models.FoodItem;
import business.services.DietPlanService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


import java.util.List;

public class ViewDietPlansController {

    @FXML
    private TableView<DietPlanViewModel> dietPlansTable;

    @FXML
    private TableColumn<DietPlanViewModel, String> dietPlanNameColumn;

    @FXML
    private TableColumn<DietPlanViewModel, String> foodItemDetailsColumn;

    private final DietPlanService dietPlanService = new DietPlanService();
    
    public void handleGoBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/AdminDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Admin Dasboard");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) dietPlansTable.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            //showAlert("Error", "An error occurred while loading the scene.");
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Initialize columns for the diet plans table
        dietPlanNameColumn.setCellValueFactory(new PropertyValueFactory<>("dietPlanName"));
        foodItemDetailsColumn.setCellValueFactory(new PropertyValueFactory<>("foodItemDetails"));

        // Load data into the diet plans table
        loadDietPlans();
    }

    private void loadDietPlans() {
        List<DietPlan> dietPlans = dietPlanService.getAllDietPlansWithFoodItems();

        // Convert DietPlan and FoodItems to DietPlanViewModel for display
        List<DietPlanViewModel> dietPlanViewModels = FXCollections.observableArrayList();
        for (DietPlan dietPlan : dietPlans) {
            StringBuilder foodItemsDetails = new StringBuilder();
            for (FoodItem foodItem : dietPlan.getFoodItems()) {
                foodItemsDetails.append(foodItem.getName())
                        .append(" (")
                        .append(foodItem.getCalories())
                        .append(" calories), ");
            }

            // Remove trailing comma and space
            if (foodItemsDetails.length() > 0) {
                foodItemsDetails.setLength(foodItemsDetails.length() - 2);
            }

            dietPlanViewModels.add(new DietPlanViewModel(dietPlan.getName(), foodItemsDetails.toString()));
        }

        // Set data to the table
        dietPlansTable.setItems(FXCollections.observableArrayList(dietPlanViewModels));
    }
}
