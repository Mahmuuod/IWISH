/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientApp;

import Utilities.UserInfo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import Utilities.UserInfo.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author osama
 */
public class TestController implements Initializable {
UserInfo UserData;
    @FXML
    private TextArea TestTa;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TestTa.setText(UserInfo.getUser().toString());
    }


    
}
