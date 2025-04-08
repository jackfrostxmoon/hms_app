package hms.user_controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class login_controller {
    // FXML components for the login page
    @FXML
    private TextField email_textfield;

    @FXML
    private PasswordField password_textfield;

    @FXML
    private Label message;

    // Handle the login button click event
    @FXML
    private void handleLoginbutton(ActionEvent event) {
        if (email_textfield.getText().isEmpty() || password_textfield.getText().isEmpty()) {
            message.setText("Please fill in all fields");
            return;
        }
        // Get the email and password from the text fields
        String email = email_textfield.getText();
        String password = password_textfield.getText();

        // Validate the email and password fields
        database_controller dbController = new database_controller(); // Create an instance!
        try (Connection conn = dbController.getDatabaseConnection()) {
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            // Execute the query to check if the user exists in the database
            ResultSet result = pstmt.executeQuery();

            // If a result is found, the user exists and we can proceed with the login
            // Otherwise, show an error message
            if (result.next()) {
                String role = result.getString("role");
                String name = result.getString("name");
                int userId = result.getInt("id"); // Get the user ID

                // Set the logged in user ID
                user_navigation_controller.setLoggedInUserID(userId);

                // Show success alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login Successful");
                alert.setHeaderText(null);
                alert.setContentText("Welcome! You have successfully logged in as " + name);
                alert.showAndWait();

                // Navigate to dashboard
                try {
                    String dashboardPath;
                    dashboardPath = "admin".equals(role) ? "/hms/staff_fxml/dashboard_staff_page.fxml"
                            : "/hms/user_fxml/dashboard_user_page.fxml";

                    // Load the user dashboard page FXML file
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(dashboardPath));
                    Parent dashboardRoot = loader.load();

                    // Get the current stage from the event source
                    // This is used to change the current scene to the new one
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene dashboardScene = new Scene(dashboardRoot);

                    // Set the new scene to the current stage
                    // This will replace the current scene with the new one
                    stage.setScene(dashboardScene);
                    stage.show();

                } catch (Exception e) {
                    // Handle the exception (e.g., show an error message or log it)
                    // Show an error message in the ui
                    message.setText("Error loading dashboard: " + e.getMessage());
                }
            } else {
                // Handle the exception (e.g., show an error message or log it)
                // Show an error message in the ui
                message.setText("Invalid email or password");
            }

        } catch (Exception e) {
            // Handle the exception (e.g., show an error message or log it)
            // Show an error message in the ui
            message.setText("Login error: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegistrationbutton(ActionEvent event) {
        try {
            // Load the registration page FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hms/user_fxml/registration_page.fxml"));
            Parent newSceneRoot = loader.load();

            // Get the current stage from the event source
            // This is used to change the current scene to the new one
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(newSceneRoot);

            // Set the new scene to the current stage
            // This will replace the current scene with the new one
            stage.setScene(newScene);
            stage.show();

        } catch (Exception e) {
            // Handle the exception (e.g., show an error message or log it)
            message.setText("Error loading registration page: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
