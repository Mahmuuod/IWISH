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
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javax.swing.JOptionPane;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author randa
 */
public class FriendListController implements Initializable {

    @FXML
    private ListView<?> friendsList;
    @FXML
    private TableView<?> wishlistTable;
    @FXML
    private TableColumn<?, ?> actionColumn;
    @FXML
    private Button FriendList_btn;
    
    
    @FXML
    private void handleSignInButtonAction(ActionEvent event) {
        JSONObject data = new JSONObject();
        ServerAccess SA = new ServerAccess();
        data.put("header", "show friendlist");
        data.put("user_id", 1);

        SA.ServerInit();
        SA.ServerWrite(data);
        System.out.println(data);
        JSONObject response = SA.ServerRead();
        //System.out.println(response);

    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
