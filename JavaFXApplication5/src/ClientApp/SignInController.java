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
import java.sql.Date;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author osama
 */
public class SignInController implements Initializable {
Utilities u=new Utilities();
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
        if (UserNameTa.getText().equals("") || PasswordTa.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Enter Username and Password", "Sign In Error", JOptionPane.ERROR_MESSAGE);

        } else {
            data.put("header", "sign in");
            data.put("Username", UserNameTa.getText());
            data.put("Password", PasswordTa.getText());

            SA.ServerInit();
            SA.ServerWrite(data);
            // System.out.println(data);
            JSONObject response = SA.ServerRead();
            //System.out.println(response);
//    public UserInfo(int User_id, String First_name, String Last_name, String Username, String Password, Date Birthdate, String Email, String Phone, String Bank_card, int User_balance) {
            if (response.getString("header").equals("user exists")) {
                user=(new UserInfo(response.getInt("User_id"), response.getString("First_name"), response.getString("Last_name"), response.getString("Username"),
                        String.valueOf(response.get("Password")), Date.valueOf(response.getString("Birthdate")), response.getString("Email"),
                        String.valueOf(response.get("Phone")),
                        String.valueOf(response.get("Bank_card")),
                        response.getInt("User_balance")
                ));
                u.switchToWishListScene(event, user);

            } else {
                JOptionPane.showMessageDialog(null, "User doesn't exists", "Sign In Error", JOptionPane.ERROR_MESSAGE);

            }
        }

    }
    
 UserInfo user=new UserInfo();

 
 /* public JSONObject ServerReadSearch() {
            final  CountDownLatch latch = new CountDownLatch(1);
            JSONObject jsonResponse = new JSONObject();
        
            t = new Thread(new Runnable() {
                public void run() {

                    try {
                        String msg = dis.readLine();
                        System.out.println(msg);
                        JsonData = new JSONObject(msg);
                          if (msg != null) {
                jsonResponse.put("response", new JSONObject(msg));
            }

                    }catch(SocketException sa)
                    {
                JOptionPane.showMessageDialog(null, "Please try to re-open the app", "Server Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);

                    }
                    catch (IOException ex) {
                        Logger.getLogger(ProjectClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    finally {
                    latch.countDown(); 
                       }
                }
            });
            
            t.start();
    try {
        latch.await(); // Wait for the thread to finish
    } catch (InterruptedException ex) {
        Logger.getLogger(ProjectClient.class.getName()).log(Level.SEVERE, null, ex);
    }
        return jsonResponse;
    }*/

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
