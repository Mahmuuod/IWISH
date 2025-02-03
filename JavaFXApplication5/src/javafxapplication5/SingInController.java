/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication5;

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

/**
 * FXML Controller class
 *
 * @author osama
 */
public class SingInController implements Initializable {

    @FXML
    private TextArea UserNameTa;
    @FXML
    private TextArea PasswordTa;

    String Respond = null;
    JSONObject data = new JSONObject();
    @FXML
    private Button SignInButton;
    @FXML
    private Hyperlink SignUpLink;

    @FXML
    private void handleSignInButtonAction(ActionEvent event) {
        Stage stage = new Stage();

        data.put("header", "sign in");
        data.put("Username", UserNameTa.getText());
        data.put("Password", PasswordTa.getText());

        try {
            s = new Socket("localhost", 5005);
            dis = new DataInputStream(s.getInputStream());
            ps = new PrintStream(s.getOutputStream());
            ps.println(data.toString());
            new Thread(new Runnable() {
                public void run() {

                    try {
                        Respond = dis.readLine();
                        data.clear();
                        data = new JSONObject(Respond);
                        System.out.println(Respond);
                        if (data.getString("header").equals("user exists")) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                                        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

                                        Scene scene = new Scene(root);

                                        stage.setScene(scene);
                                        stage.show();
                                    } catch (IOException ex) {
                                        Logger.getLogger(SingInController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }

                            });

                        } else {
                            JOptionPane.showMessageDialog(null, "User isnt exists", "Connection Error", JOptionPane.ERROR_MESSAGE);

                        }
                    } catch (IOException ex) {
                        Logger.getLogger(SingInController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }).start();

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    PrintStream ps;
    DataInputStream dis;
    Socket s;
}
