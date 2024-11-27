package presentation;

import business.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class NewUserSignUpController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private final UserService userService = new UserService();
    private int userId;
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getUserId() {
        return userId;
     }
    
    @FXML
    public void handleCancel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/HomeScreen.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Home Screen");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) passwordField.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            //showAlert("Error", "An error occurred while loading the scene.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSignUp() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        // Validate user input (basic validation, more can be added)
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Signup Failed", "All fields must be filled out.");
            return;
        }

        // Call the signup method in UserService and get the generated ID
         setUserId(userService.signup(name, email, password));

        if (getUserId() > 0) {
            showAlert("Signup Success", "You have successfully signed up! Your User ID is: " + userId);
            // Navigate to the health information page
            navigateToHealthInfoPage();
        } else {
            showAlert("Signup Failed", "An error occurred during signup.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void navigateToHealthInfoPage() {
    	if (getUserId() <= 0) {
            showAlert("Error", "Invalid user ID. Cannot proceed to health information.");
            return;  // Stop navigation if the user ID is invalid
        }
    	
        try {
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/resources/UserDashboard.fxml"));
            Parent root1 = loader1.load();
            Stage stage1 = new Stage();
            stage1.setTitle("User Dashboard");
            stage1.setScene(new Scene(root1));
            stage1.show();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/HealthInfo.fxml"));
            Parent root = loader.load();
            HealthInfoController controller = loader.getController();
            controller.setUserId(getUserId()); 

            Stage stage = new Stage();
            stage.setTitle("Health Information");
            stage.setScene(new Scene(root));
            stage.show();

            // Close current stage (signup form)
            Stage currentStage = (Stage) nameField.getScene().getWindow();
            currentStage.close();
            // Logic to return to the dashboard or main menu
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
