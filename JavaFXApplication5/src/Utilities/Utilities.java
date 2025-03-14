/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import ClientApp.AddbalanceController;
import ClientApp.ContributePopupController;
import ClientApp.FriendListController;
import ClientApp.FriendrequestController;
import ClientApp.ItemController;
import ClientApp.NotificationsController;
import ClientApp.SignInController;
import ClientApp.WishListController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author osama
 */
public class Utilities {

    public static void ChangeScene(String url, ActionEvent event) {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    Parent root = FXMLLoader.load(getClass().getResource("/ClientApp/" + url));

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

    public static void ChangeScene(String url, MouseEvent event) {
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

    public void switchToWishListScene(ActionEvent event, UserInfo loggedInUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientApp/WishList.fxml"));
            Parent root = loader.load();

            // Pass user data to FriendrequestController
            WishListController controller = loader.getController();
            controller.setData(loggedInUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("WishList");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToNotificationScene(ActionEvent event, UserInfo loggedInUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientApp/Notifications.fxml"));
            Parent root = loader.load();

            // Pass user data to FriendrequestController
            NotificationsController controller = loader.getController();
            controller.setData(loggedInUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Notifications");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToItemsScene(ActionEvent event, UserInfo loggedInUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientApp/Item.fxml"));
            Parent root = loader.load();

            // Pass user data to FriendrequestController
            ItemController controller = loader.getController();
            controller.setData(loggedInUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Item");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToFriendrequestScene(ActionEvent event, UserInfo loggedInUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientApp/Friendrequest.fxml"));
            Parent root = loader.load();

            // Pass user data to FriendrequestController
            FriendrequestController controller = loader.getController();
            controller.setData(loggedInUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Friendrequest");

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToFriendListScene(ActionEvent event, UserInfo loggedInUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientApp/FriendList.fxml"));
            Parent root = loader.load();

            // Pass user data to FriendrequestController
            FriendListController controller = loader.getController();
            controller.setData(loggedInUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("FriendList");

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToContributePopupScene(ActionEvent event, UserInfo loggedInUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientApp/ContributePopup.fxml"));
            Parent root = loader.load();

            // Pass user data to FriendrequestController
            ContributePopupController controller = loader.getController();
            controller.setData(loggedInUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("ContributePopup");

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToAddbalanceScene(ActionEvent event, UserInfo loggedInUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientApp/Addbalance.fxml"));
            Parent root = loader.load();

            // Pass user data to AddbalanceController
            AddbalanceController controller = loader.getController();
            controller.setData(loggedInUser); // Set the data BEFORE showing the stage

            // Create and configure the stage
            Stage stage = new Stage();
            stage.setTitle("Addbalance");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Ensure this window is modal
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void switchToFriendrequestScene(MouseEvent event, UserInfo loggedInUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientApp/Friendrequest.fxml"));
            Parent root = loader.load();

            // Pass user data to FriendrequestController
            FriendrequestController controller = loader.getController();
            controller.setData(loggedInUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Friendrequest");

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
