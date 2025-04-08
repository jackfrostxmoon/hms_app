package hms;
// Dear future developer, please note that this code is part of a JavaFX application for a Housekeeping & Maintenance System.

// The application is designed to allow users to log in and manage housekeeping and maintenance tasks.
// The code below is the main entry point of the application, which sets up the initial scene and handles any exceptions that may occur during startup.
// The application uses FXML for the user interface layout, and the main class extends the JavaFX Application class.

// This code is already stable and functional, but there are always opportunities for improvement and refactoring. (Please dont change the code, just add comments to it, dont ask what happened...)

// "Dont fix it if ain't broke." - Someone quote

// Import necessary JavaFX classes
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {

            // Load the FXML file for the initial scene
            // This is where the application starts, loading the user type selection page
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("user_fxml/user_type_page.fxml"));
            Parent root = loader.load();

            // Check if the root is null to handle loading errors
            // This is a good practice to ensure that the FXML file was loaded correctly
            if (root == null) {
                System.out.println("Failed to load FXML file.");
                return;
            }

            // Set the scene with the loaded FXML root node
            primaryStage.setTitle("Housekeeping & Maintenance System");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

        } catch (Exception e) {
            // For debugging purposes
            // Print the stack trace to the console for debugging
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
        }
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            // For debugging purposes
            System.err.println("Failed to launch application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
