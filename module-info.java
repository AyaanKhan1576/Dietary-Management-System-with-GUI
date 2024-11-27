/**
 * 
 */
/**
 * 
 */

module test5 {
    requires transitive java.sql;
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.base;
    
    exports presentation;
    exports business.models;  // Export other layers if needed
    exports data;
    exports com.example.test; // Export com.example.test for JavaFX access

    // Opening packages for reflection
   opens presentation to javafx.graphics, javafx.fxml;
   opens com.example.test to javafx.fxml;
}
