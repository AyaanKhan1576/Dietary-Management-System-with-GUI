package com.example.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/HomeScreen.fxml"));

        // Load the FXML into the root element
        Parent root = loader.load();

        // Set the scene with the root element
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        // Set the title for the window
        primaryStage.setTitle("Home Screen");

        // Show the stage (window)
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
