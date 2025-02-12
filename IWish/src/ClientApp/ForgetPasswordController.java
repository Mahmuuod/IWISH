/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientApp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author ascom
 */
public class ForgetPasswordController implements Initializable {

    @FXML
    private TextField EmailTa;
    @FXML
    private PasswordField PasswordTa;
    @FXML
    private Button ChangePassword;
    @FXML
    private Button SignUpButton;
    @FXML
    private PasswordField retypePasswordTa;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleChangePasswordButtonAction(ActionEvent event) throws IOException {
        JSONObject data = new JSONObject();
        ServerAccess SA = new ServerAccess();
        if (EmailTa.getText().equals("")||PasswordTa.getText().equals("") ||retypePasswordTa.getText().equals("") ){
        JOptionPane.showMessageDialog(null, "Email or New Password cannot be Empty! ", "Error", JOptionPane.ERROR_MESSAGE);}
        else if (!PasswordTa.getText().equals( retypePasswordTa.getText())) {
            JOptionPane.showMessageDialog(null, "Passwords don't Match each other ", "Error", JOptionPane.ERROR_MESSAGE);
        }else{
        data.put("header", "change password");
        data.put("Email", EmailTa.getText());
        data.put("Password", PasswordTa.getText());

        SA.ServerInit();
        SA.ServerWrite(data);
        //System.out.println(data);
        JSONObject response = SA.ServerRead();
        //System.out.println(response);
        if(response.getString("header").equals("not exists")){
            JOptionPane.showMessageDialog(null, "the Email you Entered does not exist ", "Error", JOptionPane.ERROR_MESSAGE);
        }else{
                JOptionPane.showMessageDialog(null, "your Password is Changed Succefully ", "Password Changed", JOptionPane.INFORMATION_MESSAGE);
        }
        }
    }

    @FXML
    private void handleSignInButtonAction(ActionEvent event) throws IOException {
        
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientApp/SignIn.fxml"));
        Parent root = loader.load();
        SignInController controller = loader.getController();
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    
}
