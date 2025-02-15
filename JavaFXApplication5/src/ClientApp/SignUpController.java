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
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import Utilities.Utilities;
import javafx.scene.control.Button;
import javax.swing.JOptionPane;
import Utilities.ServerAccess;
import Utilities.UserInfo;
import java.sql.Date;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author osama
 */
public class SignUpController implements Initializable {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private DatePicker dobPicker;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField balanceField;
    @FXML
    private TextField CardNumberField;
    @FXML
    private Button SignUpBTN;

    @FXML
    private void handleSignInButtonAction(ActionEvent event) {

        Utilities.ChangeScene("SignIn.fxml", event);
    }

    @FXML
    private void handleSignUpnButtonAction(ActionEvent event) {
        //(passwordField.getText().equals(confirmPasswordField.getText()) 
        JSONObject data;
        JSONObject req;

        if (firstNameField.getText() != null && lastNameField.getText() != null && usernameField.getText() != null
                && emailField.getText() != null && passwordField.getText() != null && confirmPasswordField.getText() != null && dobPicker.getValue() != null
                && phoneField.getText() != null && balanceField.getText() != null && CardNumberField.getText() != null) {
            // String First_name, String Last_name, String Username, String Password, Date Birthdate, String Email, String Phone, String Bank_card, int User_balance)
            if (passwordField.getText().equals(confirmPasswordField.getText())) {
                if (CardNumberField.getText().matches("\\d{15}")) {
                    if (phoneField.getText().matches("^(010|011|012|015)\\d{8}$")) {
                        if (emailField.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                            if (firstNameField.getText().matches("^[a-zA-Z]+$") && lastNameField.getText().matches("^[a-zA-Z]+$")) {

                                try {
                                    if (Integer.parseInt(balanceField.getText()) >= 0 && Integer.parseInt(balanceField.getText()) <= 10000) {
                                        UserInfo user = new UserInfo(firstNameField.getText(), lastNameField.getText(), usernameField.getText(), passwordField.getText(),
                                                Date.valueOf(dobPicker.getValue()), emailField.getText(),
                                                phoneField.getText(), CardNumberField.getText(), Integer.parseInt(balanceField.getText()));
                                        req = new JSONObject(user.toString());
                                        req.put("header", "sign up");
                                        ServerAccess sa = new ServerAccess();
                                        sa.ServerInit();
                                        sa.ServerWrite(req);
                                        data = sa.ServerRead();
                                        if (data.getString("header").equals("added")) {
                                            JOptionPane.showMessageDialog(null, "Sign Up Successfully you can now login", "Signed Up", JOptionPane.INFORMATION_MESSAGE);
                                            Utilities.ChangeScene("SignIn.fxml", event);

                                        } else if (data.getString("header").equals("duplicated")) {
                                            JOptionPane.showMessageDialog(null, "dublicated username or email", "Dublication", JOptionPane.ERROR_MESSAGE);
                                        }
                                        sa.ServerKill();

                                    } else {
                                        JOptionPane.showMessageDialog(null, "Balance must be positive and less than 10000", "Balance Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                } catch (NumberFormatException e) {
                                    if (balanceField.getText() == null) {
                                        JOptionPane.showMessageDialog(null, "enter a number before clicking confirm", "Balance Error", JOptionPane.ERROR_MESSAGE);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "plz enter value between 0 and 10000", "Balance Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }

                            } else {
                                JOptionPane.showMessageDialog(null, "First and Last name must be characters only", "Name Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "email must be in format example@example.example", "email Error", JOptionPane.ERROR_MESSAGE);

                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "your phone must consists of 11 digits and starts with 010 011 012 015", "phone number Error", JOptionPane.ERROR_MESSAGE);

                    }
                } else {
                    JOptionPane.showMessageDialog(null, "your card number must consists of 15-digits", "card number Error", JOptionPane.ERROR_MESSAGE);

                }
            } else {
                JOptionPane.showMessageDialog(null, "password field must match confirm password field", "Password Error", JOptionPane.ERROR_MESSAGE);

            }

        } else {
            JOptionPane.showMessageDialog(null, "all fields are required to sign up", "Connection Error", JOptionPane.ERROR_MESSAGE);

        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
