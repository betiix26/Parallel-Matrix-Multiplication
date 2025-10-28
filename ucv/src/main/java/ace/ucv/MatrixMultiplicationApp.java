package ace.ucv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MatrixMultiplicationApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // load the FXML file for the UI
            Parent root = FXMLLoader.load(getClass().getResource("/main_view.fxml"));


            // set the title and scene
            primaryStage.setTitle("Matrix Multiplication App");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            // log the exception if the FXML file could not be loaded
            e.printStackTrace();
            System.err.println("Error loading the FXML file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // launch the JavaFX application
        launch(args);
    }
}
