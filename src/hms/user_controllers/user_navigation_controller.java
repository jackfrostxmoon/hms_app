package hms.user_controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class user_navigation_controller {

    // All the FXML components are declared here

    @FXML
    private Button handleDashboardbutton;

    @FXML
    private Button handleServicebutton;

    @FXML
    private Button handleAmenitiesbutton;

    @FXML
    private Button handleLogoutbutton;

    @FXML
    private Label realtime;

    @FXML
    private Label username;

    private static Integer loggedInUserID = null; // Add this field

    // Create an instance of the database controller
    private final database_controller dbController = new database_controller();

    // Static method to set logged in user ID
    public static void setLoggedInUserID(int id) {
        loggedInUserID = id;

    }

    // Static method to get logged in user ID
    public static int getLoggedInUserID() {
        return (loggedInUserID == null) ? -1 : loggedInUserID; // Return -1 if null
    }

    @FXML
    public void initialize() {
        startClock();
        setUsername();
    }

    // Start the clock feature
    private void startClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(0), _ -> {

            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            String formattedDateTime = currentTime.format(formatter);
            updateLabel("Date & Time: " + formattedDateTime);

        }), new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private void updateLabel(String formattedDateTime) {
        realtime.setText(formattedDateTime);
    }

    // Method to fetch and display username when user logs in
    private void setUsername() {
        if (loggedInUserID == null) {
            username.setText("Guest");
            return;
        }

        // Call the database controller to get the username
        String usernameFromDB = getUserNameFromDatabase(loggedInUserID);

        if (usernameFromDB != null) {
            // Set the username label to display the welcome message
            username.setText("Welcome, " + usernameFromDB + "!");
        } else {
            // Set the username label to "Guest" if user is not found
            username.setText("Guest");
        }
    }

    // Helper method to get the username from the database
    private String getUserNameFromDatabase(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet result = null;
        String userName = null;

        // SQL query to get the username of the current user
        String query = "SELECT name FROM users WHERE id = ?";

        try {
            conn = dbController.getDatabaseConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            result = pstmt.executeQuery();

            if (result.next()) {
                userName = result.getString("name");
            }

        } catch (SQLException e) {
            // If there is any exception, print the stack trace
            e.printStackTrace();
        } finally {
            // Ensure resources are closed in the finally block
            try {
                if (result != null)
                    result.close();
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                dbController.closeDatabaseConnection(conn);
            }
        }
        return userName;
    }

    // Handle the dashboard button click event
    @FXML
    private void handleDashboardbutton(ActionEvent event) {
        loadNewScene((Node) event.getSource(), "/hms/user_fxml/dashboard_user_page.fxml");
    }

    // Handle the service button click event
    @FXML
    private void handleServicebutton(ActionEvent event) {
        loadNewScene((Node) event.getSource(), "/hms/user_fxml/service_request_page.fxml");
    }

    // Handle the amenities button click event
    @FXML
    private void handleAmenitiesbutton(ActionEvent event) {
        loadNewScene((Node) event.getSource(), "/hms/user_fxml/amenities_request_page.fxml");
    }

    // Handle the logout button click event
    @FXML
    private void handleLogoutbutton(ActionEvent event) {
        loadNewScene((Node) event.getSource(), "/hms/user_fxml/user_type_page.fxml");
    }

    // Load a new scene using the given event and FXML path
    protected void loadNewScene(Node node, String fxmlPath) {
        try {
            if (node == null) {
                throw new IllegalArgumentException("Node cannot be null");
            }

            Scene currentScene = node.getScene();
            if (currentScene == null) {
                throw new IllegalStateException("Scene is not yet initialized");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) currentScene.getWindow();
            if (stage == null) {
                throw new IllegalStateException("Window is not yet initialized");
            }

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.err.println("Error loading scene: " + e.getMessage());
            e.printStackTrace();

            // Show error alert to user
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load the requested page. Please try again.");
            alert.showAndWait();
        }
    }
}
