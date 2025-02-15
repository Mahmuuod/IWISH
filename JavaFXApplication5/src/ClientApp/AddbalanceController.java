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
import org.json.JSONObject;
/**
 * FXML Controller class
 *
 * @author osama
 */
public class AddbalanceController implements Initializable {

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
    private Button confirmBalanceButton;
    @FXML
    private Label balanceLabel;
    @FXML
    private ChoiceBox<String> PaymenChoice;

    @FXML
    private void handleConfirmBalanceButtonAction(ActionEvent event) {
                int amount=0;
                String textField=amountField.getText();
                JSONObject request=new JSONObject() ;
                JSONObject respond=new JSONObject() ;
                int user_balance=UserInfo.getUser().getUser_balance();
               try {
            if(Integer.parseInt(amountField.getText())<10000)
            amount=Integer.parseInt(amountField.getText());
            int total =user_balance+amount;
            if(amount>0  )
            {                
                if(total<10000)
                {
                    request.put("header", "add balance");
                    request.put("value",amount );
                    request.put("user id",UserInfo.getUser().getUser_id() );

                    ServerAccess SA=new ServerAccess();
                    SA.ServerInit();
                    SA.ServerWrite(request);
                    respond=SA.ServerRead();
                    UserInfo.getUser().setUser_balance(respond.getInt("value"));
                    user_balance=UserInfo.getUser().getUser_balance();
                    balanceLabel.setText("Current Balance: $"+user_balance);
                    SA.ServerKill();
                    
                }else {  JOptionPane.showMessageDialog(null, "you cant have balance >10000 you can add "+(10000-user_balance), "Balance Error", JOptionPane.ERROR_MESSAGE);}


            }
            else
            {JOptionPane.showMessageDialog(null, "plz enter value between 0 and 10000", "Balance Error", JOptionPane.ERROR_MESSAGE);}
        } catch (NumberFormatException e) {
            if(textField == null)
            JOptionPane.showMessageDialog(null, "enter a number before clicking confirm", "Balance Error", JOptionPane.ERROR_MESSAGE);
            else
            { JOptionPane.showMessageDialog(null, "plz enter value between 0 and 10000", "Balance Error", JOptionPane.ERROR_MESSAGE);
            }
            
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> items = FXCollections.observableArrayList("Credit Card", "Phone Wallet");
        PaymenChoice.setItems(items);
        PaymenChoice.setValue("Credit Card");
        
        balanceLabel.setText("Current Balance: $"+String.valueOf(UserInfo.getUser().getUser_balance()));
        
    }    
    
        @FXML
    private void addbalancefn(ActionEvent event) {
        Utilities.ChangeScene("Addbalance.fxml",event);

    }

    @FXML
    private void notificationfn(ActionEvent event) {
       Utilities.ChangeScene("Notifications.fxml",event);

    }

    @FXML
    private void logoutfn(ActionEvent event) {
      Utilities.ChangeScene("SignIn.fxml",event);

    }
            @FXML
    private void friendrequestbtn(ActionEvent event) {

                Utilities.ChangeScene("Friendrequest.fxml",event);
    }

    @FXML
   public void refreshWish(ActionEvent event) throws IOException {
       Utilities.ChangeScene("WishList.fxml", event);
   }
    @FXML
   public void friendlistbtn(ActionEvent event) throws IOException {
       Utilities.ChangeScene("FriendList.fxml", event);
   }
    @FXML
   public void itemsBtn (ActionEvent event) throws IOException {
       Utilities.ChangeScene("Item.fxml", event);

   }
    
}
