package presentation;

import business.models.FoodItem;
import business.services.FoodItemService;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class ViewFoodItemsController {

    @FXML
    private TableView<FoodItem> foodItemsTable;  // Table to display food items

    // Table columns for ID, name, and calories
    @FXML
    private TableColumn<FoodItem, String> idColumn;
    @FXML
    private TableColumn<FoodItem, String> nameColumn;
    @FXML
    private TableColumn<FoodItem, Integer> caloriesColumn;

    @FXML
    private Button backButton;  // Back button to go back to the Admin Dashboard

    private FoodItemService foodItemService = new FoodItemService();  // Service to fetch food items

    @FXML
    public void initialize() {
        // Load food items into the table
        loadFoodItemsIntoTable();
    }

    // Load food items into the table
    private void loadFoodItemsIntoTable() {
        List<FoodItem> foodItems = foodItemService.getAllFoodItems();  // Fetch all food items
        foodItemsTable.getItems().clear();  // Clear the current table data
        foodItemsTable.getItems().addAll(foodItems);  // Add food items to the table

        // Set the table columns to the respective fields in the FoodItem object
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("calories"));
    }

    // Handle the "Back" button click
    @FXML
    public void handleBack() {
        loadNewScene("AdminDashboard.fxml", "Admin Dashboard");
    }

    // Method to load a new scene (FXML)
    private void loadNewScene(String fxmlFile, String title) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/" + fxmlFile));
            Parent root = loader.load();

            // Create a new stage for the new scene
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage (View Food Items)
            Stage currentStage = (Stage) backButton.getScene().getWindow(); // Use any button's scene to get the window
            currentStage.close();

        } catch (Exception e) {
            showAlert("Error", "An error occurred while loading the scene.");
            e.printStackTrace();
        }
    }
    
    

    // Method to show alert messages
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
