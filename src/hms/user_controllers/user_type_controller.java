package hms.user_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Node;

public class user_type_controller {

    @FXML
    private Button handleUserButton;

    @FXML
    private Button handleStaffButton;

    @FXML
    private void handleUserButton(ActionEvent event) {
        // Add navigation logic for user page
        System.out.println("User button clicked");

        // Add your logic to load the user page here
        // For example, you can load a new FXML file for the user page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hms/user_fxml/login_page.fxml"));
            Parent newSceneRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(newSceneRoot);

            stage.setScene(newScene);
            stage.show();

        } catch (Exception e) {
            // Handle the exception (e.g., show an error message or log it)
            System.err.println("Error loading login page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleStaffButton(ActionEvent event) {
        // Add navigation logic for staff page
        System.out.println("Staff button clicked");

        // Add your logic to load the staff page here
        // For example, you can load a new FXML file for the staff page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hms/user_fxml/login_page.fxml"));
            Parent newSceneRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(newSceneRoot);

            stage.setScene(newScene);
            stage.show();

        } catch (Exception e) {
            // Handle the exception (e.g., show an error message or log it)
            System.err.println("Error loading login page: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
