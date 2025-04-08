package hms.user_controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert; // Keep this import
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Alert.AlertType; // Import AlertType

public class services_request_controller extends user_navigation_controller implements Initializable {

    // --- FXML Fields for THIS page (Service Request) ---
    @FXML
    private BorderPane rootPane; // IMPORTANT: Add fx:id="rootPane" to your FXML root
    @FXML
    private Button roomCleaningButton;
    @FXML
    private Button dryClothingButton;
    @FXML
    private Button wetClothingButton;
    @FXML
    private Button airConditionerButton;
    @FXML
    private Button lightButton;
    @FXML
    private Button waterButton;

    // Database controller instance
    private final database_controller dbController = new database_controller();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(); // Call parent initialize

        // Wrap the initialization in Platform.runLater to ensure JavaFX is ready
        Platform.runLater(() -> {
            if (!checkRoomSelectionAndRedirect()) {
                return;
            }
            setupServiceButtonActions();
        });
    }

    private boolean checkRoomSelectionAndRedirect() {
        int userId = getLoggedInUserID();

        if (userId == -1) {
            System.out.println("No user logged in. Cannot access services.");
            // Show Alert directly
            Alert alert = new Alert(AlertType.ERROR); // Use ERROR type
            alert.setTitle("Authentication Error");
            alert.setHeaderText(null);
            alert.setContentText("Please log in first.");
            alert.showAndWait();

            if (rootPane != null) {
                loadNewScene(rootPane, "/hms/user_fxml/login_page.fxml");
                return false;
            } else {
                System.err.println("Error: rootPane is null in checkRoomSelectionAndRedirect (no user).");
                return true;
            }
        }

        boolean hasSelected = dbController.hasUserSelectedRoom(userId);

        if (!hasSelected) {
            System.out.println("User has not selected a room. Redirecting...");
            // Show Alert directly
            Alert alert = new Alert(AlertType.WARNING); // Use WARNING type
            alert.setTitle("Room Selection Required");
            alert.setHeaderText(null);
            alert.setContentText("Please select your room before requesting services.");
            alert.showAndWait();

            if (rootPane != null) {
                loadNewScene(rootPane, "/hms/user_fxml/room_selection_page.fxml");
                return false;
            } else {
                System.err.println("Error: rootPane is null in checkRoomSelectionAndRedirect (no room).");
                return true;
            }
        }

        System.out.println("User has selected a room. Loading service requests.");
        return true;
    }

    private void setupServiceButtonActions() {
        if (roomCleaningButton != null) {
            roomCleaningButton.setOnAction(event -> handleServiceRequest("Room Cleaning", event));
        }
        if (dryClothingButton != null) {
            dryClothingButton.setOnAction(event -> handleServiceRequest("Dry Cleaning", event));
        }
        if (wetClothingButton != null) {
            wetClothingButton.setOnAction(event -> handleServiceRequest("Laundry", event));
        }
        if (airConditionerButton != null) {
            airConditionerButton.setOnAction(event -> handleServiceRequest("Air Conditioner Maintenance", event));
        }
        if (lightButton != null) {
            lightButton.setOnAction(event -> handleServiceRequest("Light Maintenance", event));
        }
        if (waterButton != null) {
            waterButton.setOnAction(event -> handleServiceRequest("Water Issue", event));
        }
    }

    private void handleServiceRequest(String serviceType, ActionEvent event) {
        int userId = getLoggedInUserID();
        if (userId == -1) {
            // Show Alert directly
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Cannot submit request. User not logged in.");
            alert.showAndWait();
            return;
        }

        System.out.println("User " + userId + " requested service: " + serviceType);

        boolean success = dbController.saveServiceRequest(userId, serviceType);

        if (success) {
            // Show Alert directly
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Request Submitted");
            alert.setHeaderText(null);
            alert.setContentText(serviceType + " request submitted successfully.");
            alert.showAndWait();
        } else {
            // Show Alert directly
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to submit " + serviceType + " request. Please try again.");
            alert.showAndWait();
        }
    }

}
