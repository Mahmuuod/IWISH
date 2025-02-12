/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientApp;

import Utilities.ServerAccess;
import Utilities.Utilities;
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
import Utilities.ServerAccess.*;
import Utilities.UserInfo;
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
    private PasswordField PasswordTa;

    @FXML
    private Button SignInButton;
    @FXML
    private Hyperlink forgetPasswordLink;
    @FXML
    private Button SignUpButton;

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
            //Utilities.ChangeScene("Friendrequest.fxml", event);
            SA.ServerKill();
            switchToFriendRequestScene(event,UserInfo.getInstance());

        } else {
            JOptionPane.showMessageDialog(null, "User doesn't exists", "Sign In Error", JOptionPane.ERROR_MESSAGE);

        }

    }
 private void switchToFriendRequestScene(ActionEvent event, UserInfo loggedInUser) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WishList.fxml"));
        Parent root = loader.load();

        // Pass user data to FriendrequestController
        WishListController controller = loader.getController();
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
    private void handleForgetPassword(ActionEvent event) {
        Utilities.ChangeScene("ForgetPassword.fxml", event);

    }

}
