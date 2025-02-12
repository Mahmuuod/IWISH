/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientApp;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.json.JSONObject;
import ClientApp.ServerAccess.*;
import java.sql.Date;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author osama
 */
public class SignInController implements Initializable {
 @FXML
     private TextField UserNameTa;
    @FXML
    private TextField PasswordTa;

    @FXML
    private TextField EmailTa;
    @FXML
    private Button ChangePassword;
    @FXML
    private Button SignUpButton;
    @FXML
    private PasswordField retypePasswordTa;

     @FXML
    private void handleSignInButtonAction(ActionEvent event) {
        JSONObject data = new JSONObject();
        ServerAccess SA = new ServerAccess();
        data.put("header", "sign in");
        data.put("Username", UserNameTa.getText());
        data.put("Password", PasswordTa.getText());

        SA.ServerInit();
        SA.ServerWrite(data);
        // System.out.println(data);
        JSONObject response = SA.ServerRead();
        //System.out.println(response);

        if (response.getString("header").equals("user exists")) {
            SA.SetUserData(response);
            UserInfo loggedInUser = new UserInfo(
            response.getInt("User_id"),
            response.getString("First_name"),
            response.getString("Last_name"),
            response.getString("Username"),
            response.getString("Password"),
            Date.valueOf(response.getString("Birthdate")),
            response.getString("Email"),
            response.getString("Phone"),
            String.valueOf(response.getLong("Bank_card")),
            response.getInt("User_balance"));
            switchToFriendRequestScene(event, loggedInUser);
            //SA.ServerKill();

        } else {
            JOptionPane.showMessageDialog(null, "User doesn't exists", "Connection Error", JOptionPane.ERROR_MESSAGE);

        }

    }
    private void switchToFriendRequestScene(ActionEvent event, UserInfo loggedInUser) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Friendrequest.fxml"));
        Parent root = loader.load();

        // Pass user data to FriendrequestController
        FriendrequestController controller = loader.getController();
        controller.setUserData(loggedInUser);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    @FXML
    private void handleSignUpButtonAction(ActionEvent event) {

        Utilities.ChangeScene("SignUp.fxml", event);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    @FXML
     public void handleForgetPassword(ActionEvent event) throws IOException {

       FXMLLoader loader = new FXMLLoader(getClass().getResource("ForgetPassword.fxml"));
        Parent root = loader.load();
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    
    }

}