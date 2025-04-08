package hms.user_controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class registration_controller extends database_controller {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField confirmPasswordField;

    @FXML
    private Label messageLabel;

    @FXML
    private CheckBox termsCheckbox;

    @FXML
    private void handleLoginbutton(ActionEvent event) {
        // Logic to handle login button click
        System.out.println("Login button clicked");

        try {
            // Load the registration page FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hms/user_fxml/login_page.fxml"));
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
            // message_label.setText("Error loading registration page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegistration(ActionEvent event) throws IOException {
        // Fetch all the information that user has entered
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Check field has entered
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("Please fill all fields");
            return;
        }

        // Add password length validation
        if (password.length() < 8) {
            messageLabel.setText("Password must be at least 8 characters long!");
            return;
        }

        // Add password confirmation check
        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match!");
            return;
        }
        // Add checkbox validation
        if (!termsCheckbox.isSelected()) {
            messageLabel.setText("Please agree to the terms and conditions!");
            return;
        }

        // Establish database connection
        try (Connection conn = getDatabaseConnection()) { // Use the inherited method!
            String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, 'user')";

            // Prepare SQL statement
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, password);

                // Prepare SQL statement
                int rowsAffected = pstmt.executeUpdate();

                // Check if insertion was successful
                if (rowsAffected > 0) {

                    // Display success alert
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Registration successful!");
                    alert.showAndWait();

                    messageLabel.setText("Registration successful!");
                    // Load dashboard instead of login page
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/hms/user_fxml/login_page.fxml"));
                    Parent dashboardRoot = loader.load();
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(dashboardRoot);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    // Display error alert if registration fails
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Registration failed!");
                    alert.showAndWait();

                    messageLabel.setText("Registration failed!");
                }
            }
        } catch (SQLException e) {
            // Handle database connection errors
            messageLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}