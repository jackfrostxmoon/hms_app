package hms.user_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class room_controller extends user_navigation_controller {

    // All the FXML components are declared here
    @FXML
    private Button handleSingleRoombutton;

    @FXML
    private Button handleDoubleRoombutton;

    @FXML
    private Button handleTripleRoomButton;

    @FXML
    private Button handleQuadrupleRoombutton;

    // Create an instance of the database controller
    private final database_controller dbController = new database_controller();

    // Handle the Single Room button click event
    @FXML
    private void handleSingleRoombutton(ActionEvent event) {
        handleRoomSelection("Single");
        System.out.println("Single Room button clicked");
    }

    // Handle the Double Room button click event
    @FXML
    private void handleDoubleRoombutton(ActionEvent event) {
        handleRoomSelection("Double");
        System.out.println("Double Room button clicked");
    }

    // Handle the Triple Room button click event
    @FXML
    private void handleTripleRoomButton(ActionEvent event) {
        handleRoomSelection("Triple");
        System.out.println("Triple Room button clicked");
    }

    // Handle the Quadruple Room button click event
    @FXML
    private void handleQuadrupleRoombutton(ActionEvent event) {
        handleRoomSelection("Quadruple");
        System.out.println("Quadruple Room button clicked");
    }

    // Method to handle room selection
    private void handleRoomSelection(String roomType) {
        if (roomType != null && !roomType.isEmpty()) {
            // Save room selection to database
            saveRoomSelectionToDatabase(roomType);

            // Display confirmation alert
            displayRoomSelectionAlert(roomType);
        } else {
            showAlert("Error", "Invalid room type selected.");
        }
    }

    // Method to save room selection to the database
    private void saveRoomSelectionToDatabase(String roomType) {
        // Get the logged-in user ID
        int userId = getLoggedInUserID();

        // Check if a user is logged in
        if (userId != -1) {
            // Attempt to save the room selection
            boolean success = dbController.saveUserRoomSelection(userId, roomType);

            if (success) {
                showAlert("Success", "Room selection saved successfully.");
            } else {
                showAlert("Error", "Failed to save room selection.");
            }
        } else {
            showAlert("Error", "No user is logged in.");
        }
    }

    // Method to display room selection alert
    private void displayRoomSelectionAlert(String roomType) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Room Selection");
        alert.setHeaderText(null);
        alert.setContentText("You have selected a " + roomType + " Room.");
        alert.showAndWait();
    }

    // Method to display an alert message
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Helper method to get the logged-in user ID
    public static int getLoggedInUserID() {
        return user_navigation_controller.getLoggedInUserID();
    }
}
