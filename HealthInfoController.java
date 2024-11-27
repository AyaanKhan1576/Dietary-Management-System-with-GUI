package presentation;

import java.io.IOException;

import business.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HealthInfoController {

    private int userId;

    @FXML
    private TextField heightField;

    @FXML
    private TextField weightField;

    @FXML
    private TextField ageField;

    @FXML
    private ChoiceBox<String> goalField;

    @FXML
    private ChoiceBox<String> allergiesChoiceBox;

    @FXML
    private ChoiceBox<String> diseasesChoiceBox;

    private final UserService userService = new UserService();


    @FXML
    public void initialize() {
        // Populate the ChoiceBoxes
    	userId= userService.getCurrentUserId();
    	
        goalField.getItems().addAll("Lose Weight", "Gain Muscle", "Maintain Health", "Other");
        allergiesChoiceBox.getItems().addAll("None", "Peanuts", "Shellfish", "Dairy", "Gluten", "Other");
        diseasesChoiceBox.getItems().addAll("None", "Diabetes", "Hypertension", "Heart Disease", "Asthma", "Other");
    }

    @FXML
    public void handleSubmit() {
        String height = heightField.getText();
        String weight = weightField.getText();
        String age = ageField.getText();
        String goal = goalField.getValue();
        String allergies = allergiesChoiceBox.getValue(); // Getting the selected allergy
        String diseases = diseasesChoiceBox.getValue();  // Getting the selected disease

        // Validate input fields
        if (height.isEmpty() || weight.isEmpty() || age.isEmpty() || goal == null || allergies == null || diseases == null) {
            showAlert("Error", "Height, Weight, Age, Goal, Allergies, and Diseases are mandatory fields. Please fill them out.");
            return;
        }

        if (getUserId() == 0) {
            showAlert("Error", "Invalid user ID. Please ensure you are logged in.");
            return;
        }
        
        try {
            // Ensure height, weight, and age are valid numeric values
            double heightValue = Double.parseDouble(height);
            double weightValue = Double.parseDouble(weight);
            int ageValue = Integer.parseInt(age);

            // Log or process the data (for now, print the data)
            System.out.println("Height: " + heightValue);
            System.out.println("Weight: " + weightValue);
            System.out.println("Age: " + ageValue);
            System.out.println("Goal: " + goal);
            System.out.println("Allergies: " + allergies);
            System.out.println("Diseases: " + diseases);

            // Save health info using the service
            boolean success = userService.saveHealthInfo(getUserId(), heightValue, weightValue, ageValue, allergies, diseases, goal);

            // If the health info was saved successfully
            if (success) {
            	Stage currentStage = (Stage) heightField.getScene().getWindow();
                currentStage.close();
            } else {
                showAlert("Error", "There was an issue saving your health information.");
            }

        } catch (NumberFormatException e) {
            // Handle invalid number format errors for height, weight, or age
            showAlert("Error", "Please enter valid numeric values for height, weight, and age.");
        }
    }



    // Setter for userId (to pass the user ID from the signup process)
    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Getter for userId
    public int getUserId() {
        return userId;
    }

    public void handleIgnore() {
    	Stage currentStage = (Stage) heightField.getScene().getWindow();
        currentStage.close();
    }
    
    // Method to show alerts (success or error messages)
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
