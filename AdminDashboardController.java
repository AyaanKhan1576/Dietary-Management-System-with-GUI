package presentation;

import business.models.User;
import business.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.util.List;

public class AdminDashboardController {

    @FXML
    private Button viewDietPlansButton;
    
    @FXML
    private Text titleText;

    @FXML
    private Button viewUsersButton;

    @FXML
    private Button addFoodListButton;

    @FXML
    private Button addMealsButton;
    
    @FXML
    private Button viewFoodItemsButton;  // New button to view food items
    
    @FXML
    private Button searchFoodItemsButton;
    
    @FXML
    private Button searchDietPlanButton;
    
    @FXML
    private Button removeFoodItemButton; // Button for removing food items

    @FXML
    private Button removeDietPlanButton;
    
    @FXML
    private TableView<User> usersTable;  // The TableView to display users
    
    // Declare TableColumns for name and email
    @FXML
    private TableColumn<User, String> userNameColumn;

    @FXML
    private TableColumn<User, String> userEmailColumn;

    @FXML
    private UserService userService = new UserService(); 
    
    @FXML
    public void initialize() {
    	int id = userService.getCurrentUserId();
    	User user = userService.getUserById(id);
    	titleText.setText("Welcome Back, "+user.getName());
    }
    
    @FXML
    public void handleViewUsers() {
        // Load the users data into the table
        loadUsersIntoTable();
        loadNewScene("AllUserview.fxml", "User Info");
    }

    // Handle the "View Diet Plans" button click
    @FXML
    public void handleViewDietPlans() {
        loadNewScene("ViewDietPlans.fxml", "Diet Plans");
    }

    // Handle the "Add Meals" button click
    @FXML
    public void handleAddMeals() {
        loadNewScene("AddMeals.fxml", "Add Meals");
    }
    
    @FXML
    public void handleViewFoodItems() {
        loadNewScene("ViewFoodItems.fxml", "View Food Items");
    }
    
    @FXML
    public void handleSearchFoodItems() {
        loadNewScene("SearchFoodItems.fxml", "Search Food Items");
    }
    
    @FXML
    public void handleSearchDietPlan() {
    	loadNewScene("DietPlanSearch.fxml", "Search Diet Plan");
    }
    
    @FXML
    public void handleAddFoodList() {
        loadNewScene("AddFoodList.fxml", "Add Food");
    }

    @FXML
    public void handleRemoveDietPlan() {
    	loadNewScene("RemoveDietPlan.fxml", "Remove Diet Plan");
    }
    
    @FXML
    public void handleRemoveFoodItem() {
    	loadNewScene("RemoveFoodItem.fxml", "Remove Food Item");
    }

    
    public void handleLogout() {
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
    
    // Method to load users into the table
    private void loadUsersIntoTable() {
        if (usersTable != null) {
            List<User> users = userService.getAllUsersWithRole("User");
            usersTable.getItems().clear();
            usersTable.getItems().addAll(users);
            
            // Setting the columns to the respective fields in the User object
            userNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            userEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        } else {
            System.out.println("usersTable is still null at loadUsersIntoTable()");
        }
    }
   
    // Method to load new scene (FXML)
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

            // Close the current stage (Admin Dashboard)
            Stage currentStage = (Stage) addMealsButton.getScene().getWindow(); // Use any button's scene to get the window
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
