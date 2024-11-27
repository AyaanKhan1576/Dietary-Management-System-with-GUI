package presentation;

import business.models.FoodItem;
import business.services.FoodItemService;
import business.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;

import java.util.List;

public class SearchFoodItemsController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField idField;

    @FXML
    private TextField minCaloriesField;

    @FXML
    private TextField maxCaloriesField;

    @FXML
    private TableView<FoodItem> foodItemTable;

    @FXML
    private TableColumn<FoodItem, String> idColumn;

    @FXML
    private TableColumn<FoodItem, String> nameColumn;

    @FXML
    private TableColumn<FoodItem, Integer> caloriesColumn;

    private final FoodItemService foodItemService = new FoodItemService();
    private final UserService userService=new UserService();
    @FXML
    public void initialize() {
        // Set up the columns to display data from the FoodItem object
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        caloriesColumn.setCellValueFactory(cellData -> cellData.getValue().caloriesProperty().asObject());
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
            Stage currentStage = (Stage) idField.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            //showAlert("Error", "An error occurred while loading the scene.");
            e.printStackTrace();
        }
       
    }
    
    @FXML
    public void handleSearchFoodItems() {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        Integer minCalories = null;
        Integer maxCalories = null;

        try {
            if (!minCaloriesField.getText().isEmpty()) {
                minCalories = Integer.parseInt(minCaloriesField.getText().trim());
            }
        } catch (NumberFormatException e) {
            // Handle invalid input for minCalories
            minCalories = null;
        }

        try {
            if (!maxCaloriesField.getText().isEmpty()) {
                maxCalories = Integer.parseInt(maxCaloriesField.getText().trim());
            }
        } catch (NumberFormatException e) {
            // Handle invalid input for maxCalories
            maxCalories = null;
        }

        List<FoodItem> searchResults = foodItemService.searchFoodItems(name, id, minCalories, maxCalories);

        if (searchResults.isEmpty()) {
            System.out.println("No food items found");
        }
        
        if (searchResults.isEmpty()) {
            System.out.println("No food items found");
        } else {
            System.out.println("Found " + searchResults.size() + " items");
        }

        foodItemTable.getItems().clear();
        foodItemTable.setItems(FXCollections.observableArrayList(searchResults));


    }

}
