package presentation;

import business.models.HealthInfo;
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

public class UserLoginController implements LoginController, DashboardLoader {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private final UserService userService = new UserService();

    @Override
    public void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();  // Get password from the field

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Input Error", "Email and password fields cannot be empty.");
            return;
        }

        // Fetch user by email from the database
        User user = userService.login(email);

        if (user == null) {
            showAlert("Login Failed", "User not found. Please check your email.");
            return;
        }

        // Validate the user and password
        if (userService.validatePassword(user, password)) {
            int userId = Integer.parseInt(user.getId());
            userService.addLoginLog(userId);  // Log the user's login

            // Check if the user has health information
            HealthInfo healthInfo = userService.getHealthInfo(userId);
            if (healthInfo == null) {
                // Load the HealthInfo screen if no health information exists
                loadDashboard("/resources/UserDashboard.fxml", "User Dashboard");
                loadDashboard("/resources/HealthInfo.fxml", "Health Information");

            } else {
                if ("User".equalsIgnoreCase(user.getRole())) {
                    loadDashboard("/resources/UserDashboard.fxml", "User Dashboard");
                } else {
                    showAlert("Login Failed", "Invalid user role.");
                }
            }
        } else {
            showAlert("Login Failed", "Invalid email or password.");
        }
    }

    @Override
    public void loadDashboard(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) emailField.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the dashboard.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
