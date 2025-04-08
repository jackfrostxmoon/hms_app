package hms.user_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType; // Import AlertType

public class amenities_controller extends user_navigation_controller {

    // --- FXML Fields for THIS page (Amenities Request) ---
    @FXML
    private Button tissueButton;
    @FXML
    private Button towelButton;
    @FXML
    private Button soapButton;

    // --- FXML Fields for inherited navigation elements (handled by parent) ---
    // No need to re-declare @FXML for dashboardButton, servicesButton, etc.
    // if user_navigation_controller handles them and this class extends it.

    // Create an instance of the database controller
    private final database_controller dbController = new database_controller();

    // Handle the Tissue button click event
    @FXML
    private void handleTissueButton(ActionEvent event) {
        handleAmenityRequest("Tissue");
        System.out.println("Tissue button clicked");
    }

    // Handle the Towel button click event
    @FXML
    private void handleTowelButton(ActionEvent event) {
        handleAmenityRequest("Towel");
        System.out.println("Towel button clicked");
    }

    // Handle the Soap button click event
    @FXML
    private void handleSoapButton(ActionEvent event) {
        handleAmenityRequest("Soap");
        System.out.println("Soap button clicked");
    }

    // Method to handle the amenity request logic
    private void handleAmenityRequest(String amenityType) {
        if (amenityType != null && !amenityType.isEmpty()) {
            // Save amenity request to database
            saveAmenityRequestToDatabase(amenityType);

            // Display confirmation alert
            displayRequestAlert(amenityType);
        } else {
            // Use the inherited showAlert method if it's protected/public
            // Or define it locally if needed
            showAlert("Error", "Invalid amenity type requested.");
        }
    }

    // Method to save amenity request to the database
    private void saveAmenityRequestToDatabase(String amenityType) {
        // Get the logged-in user ID using the inherited static method
        int userId = getLoggedInUserID();

        // Check if a user is logged in
        if (userId != -1) {
            // Attempt to save the amenity request
            // You'll need a corresponding method in database_controller
            boolean success = dbController.saveAmenityRequest(userId, amenityType); // Assuming this method exists

            if (success) {
                showAlert("Success", amenityType + " request submitted successfully.");
            } else {
                showAlert("Error", "Failed to submit " + amenityType + " request.");
            }
        } else {
            showAlert("Error", "No user is logged in. Cannot submit request.");
        }
    }

    // Method to display amenity request confirmation alert
    private void displayRequestAlert(String amenityType) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Amenity Request");
        alert.setHeaderText(null);
        alert.setContentText(amenityType + " request received. We will deliver it shortly.");
        alert.showAndWait();
    }

    // Define the showAlert method to display alerts
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // No need to redefine getLoggedInUserID() as it's inherited
}
