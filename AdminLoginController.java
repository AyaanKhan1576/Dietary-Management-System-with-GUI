package presentation;

import business.models.User;
import business.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class AdminLoginController implements LoginController, DashboardLoader {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private final UserService userService = new UserService();

    @Override
    public void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();  // Get password from the field

        // Fetch user by email from the database
        User user = userService.login(email);
        userService.addLoginLog(Integer.valueOf(user.getId()));

        // Validate the user and password
        if (user != null && userService.validatePassword(user, password)) {
            // Use equalsIgnoreCase for case-insensitive role comparison
            if (user.getRole().equalsIgnoreCase("Admin")) {
                loadDashboard("AdminDashboard.fxml", "Admin Dashboard");
            } else {
                showAlert("Login Failed", "The account does not have Admin privileges.");
            }
        } else {
            showAlert("Login Failed", "Invalid credentials. Please check your email and password.");
        }
    }

    @Override
    public void loadDashboard(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) emailField.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "An error occurred while loading the Admin Dashboard.");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    


}
