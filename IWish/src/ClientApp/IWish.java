package ClientApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class IWish extends Application {

    @Override
    public void start(Stage primaryStage) {
    try {
        System.out.println("Attempting to load FXML file...");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientApp/SignIn.fxml"));
        if (loader.getLocation() == null) {
            System.out.println("FXML file not found!");
            return;
        }

        Parent root = loader.load();
        System.out.println("FXML file loaded successfully.");

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("IWish");

        // Force UI to appear
        primaryStage.setWidth(1440);
        primaryStage.setHeight(1024);
        primaryStage.setMaximized(true);
        primaryStage.show();
        
        System.out.println("UI should now be visible.");
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Error loading FXML file.");
    }
}


    public static void main(String[] args) {
        launch(args);
    }
}
