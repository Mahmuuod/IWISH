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

/**
 * FXML Controller class
 *
 * @author osama
 */
public class SignInController implements Initializable {

    @FXML
    private TextArea UserNameTa;
    @FXML
    private TextArea PasswordTa;

    @FXML
    private Button SignInButton;
    @FXML
    private Hyperlink SignUpLink;

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
            Utilities.ChangeScene("Test.fxml", event);
            SA.ServerKill();

        } else {
            JOptionPane.showMessageDialog(null, "User doesn't exists", "Connection Error", JOptionPane.ERROR_MESSAGE);

        }

    }

    @FXML
    private void handleSignUpButtonAction(ActionEvent event) {

        Utilities.ChangeScene("SignUp.fxml", event);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
