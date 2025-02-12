/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientApp;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
/**
 *
 * @author ascom
 */
public class SceneController implements Initializable {
    
    private Label label;
    @FXML
    private Button homeButton;
    @FXML
    private Button notificationsButton;
    @FXML
    private Button wishlistButton;
    @FXML
    private Button friendListButton;
    @FXML
    private Button addFriendButton;
    @FXML
    private Button addBalanceButton;
    @FXML
    private TextField amountField;
   
    @FXML
    private Button addPaymentMethodButton;
    @FXML
    private Button confirmBalanceButton;
    @FXML
    private Label balanceLabel;
    @FXML
    private ComboBox<String> paymentMethodComboBox;

    @FXML
    public void initialize() {
        ObservableList<String> paymentMethods = FXCollections.observableArrayList("Credit Card", "Phone Wallet");
        paymentMethodComboBox.setItems(paymentMethods);
    }
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
