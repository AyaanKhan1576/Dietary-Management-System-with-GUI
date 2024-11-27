package presentation;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class HomeScreenController {

    @FXML
    private Button userLoginButton;

    @FXML
    private Button adminLoginButton;
    
    @FXML
    private Button userSignupButton;
    
    @FXML
    private Button exitButton;
    
    @FXML
    private void handleExit() {
        Stage currentStage = (Stage) userLoginButton.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    private void handleUserLogin() {
        navigateTo("/resources/UserLogin.fxml", "User Login");
    }

    @FXML
    private void handleAdminLogin() {
        navigateTo("/resources/AdminLogin.fxml", "Admin Login");
    }
    
    @FXML
    private void handleSignup() {
        navigateTo("/resources/SignUp.fxml", "User SignUp");
    }
    
    

    private void navigateTo(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) userLoginButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
