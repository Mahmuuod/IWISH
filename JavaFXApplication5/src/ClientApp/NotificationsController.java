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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author randa
 */
public class NotificationsController implements Initializable {

    @FXML
    private Label balance_lbl;
    @FXML
    private Label username_lbl;
    @FXML
    private Button markasread_btn;
    @FXML
    private Button delete_btn;
    @FXML
    private Button clearall_btn;
    @FXML
    private TableView<?> notifi_table;
    @FXML
    private TableColumn<?, ?> notifi_col;
    @FXML
    private ScrollBar notifi_scroll;
    @FXML
    private Button FriendList_btn;
    
    
    @FXML
    private void handleFriendListButtonAction(ActionEvent event) {
        
        
        
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    
    
}
