/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientApp;

import Utilities.UserInfo;
import Utilities.Utilities;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import Utilities.*;
import java.io.IOException;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author osama
 */
public class AddbalanceController implements Initializable {

    Utilities u = new Utilities();
    UserInfo user;
    @FXML
    private TextField amountField;
    @FXML
    private Button confirmBalanceButton;
    @FXML
    private Label balanceLabel;
    @FXML
    private ChoiceBox<String> PaymenChoice;

    @FXML
    private void handleConfirmBalanceButtonAction(ActionEvent event) {
        int amount = 0;
        String textField = amountField.getText();
        JSONObject request = new JSONObject();
        JSONObject respond = new JSONObject();
        int user_balance = user.getUser_balance();
        try {
            if (Integer.parseInt(amountField.getText()) < 10000) {
                amount = Integer.parseInt(amountField.getText());
            }
            int total = user_balance + amount;
            if (amount > 0) {
                if (total < 10000) {
                    request.put("header", "add balance");
                    request.put("value", amount);
                    request.put("user id", user.getUser_id());

                    ServerAccess SA = new ServerAccess();
                    SA.ServerInit();
                    SA.ServerWrite(request);
                    respond = SA.ServerRead();
                    user.setUser_balance(respond.getInt("value"));
                    user_balance = user.getUser_balance();
                    System.out.println(user_balance);
                    balanceLabel.setText("Current Balance: $" + user_balance);
                    SA.ServerKill();
                    user.setUser_balance(total);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.close();
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    String title = stage.getTitle();
                    System.out.println(title);
                    switch (title) {
                        case "WishList":
                            u.switchToWishListScene(event, user);
                            break;
                        case "Notifications":
                            u.switchToNotificationScene(event, user);

                            break;
                        case "Item":
                            u.switchToItemsScene(event, user);

                            break;
                        case "Friendrequest":
                            u.switchToFriendrequestScene(event, user);

                            break;
                        case "FriendList":
                            u.switchToFriendListScene(event, user);

                            break;
                        case "ContributePopup":
                            u.switchToContributePopupScene(event, user);

                            break;
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "you cant have balance >10000 you can add " + (10000 - user_balance), "Balance Error", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(null, "plz enter value between 0 and 10000", "Balance Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            if (textField == null) {
                JOptionPane.showMessageDialog(null, "enter a number before clicking confirm", "Balance Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "plz enter value between 0 and 10000", "Balance Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (user != null) {
            updateUI();
        }

    }

    private void contributionlistfn(ActionEvent event) {
        u.switchToContributePopupScene(event, user);

    }

    private void addbalancefn(ActionEvent event) {
        u.switchToAddbalanceScene(event, user);

    }

    private void notificationfn(ActionEvent event) {
        u.switchToNotificationScene(event, user);

    }

    private void logoutfn(ActionEvent event) {
        Utilities.ChangeScene("SignIn.fxml", event);

    }

    private void friendrequestbtn(ActionEvent event) {

        u.switchToFriendrequestScene(event, user);
    }

    public void refreshWish(ActionEvent event) throws IOException {
        u.switchToWishListScene(event, user);
    }

    public void friendlistbtn(ActionEvent event) throws IOException {
        u.switchToFriendListScene(event, user);
    }

    public void itemsBtn(ActionEvent event) throws IOException {
        u.switchToItemsScene(event, user);

    }

    public void setData(UserInfo user2) {
        this.user = user2;
        updateUI();  // Now update UI after user is set
    }

    private void updateUI() {
        balanceLabel.setText("Current Balance: $" + user.getUser_balance());

    }

}
