/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientApp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author osama
 */
public class Utilities {

    public static void ChangeScene(String url,ActionEvent event) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    
                    Parent root = FXMLLoader.load(getClass().getResource(url));

                    Scene scene = new Scene(root);
                        
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
        /*public static void ChangeScene(String url,ActionEvent event) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    
                    Parent root = FXMLLoader.load(getClass().getResource(url));

                    Scene scene = new Scene(root);
                        
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }*/

}