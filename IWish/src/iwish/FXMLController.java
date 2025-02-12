/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwish;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author ascom
 */
public class FXMLController implements Initializable {

    @FXML
    private TableView<?> wishlisttable;
    @FXML
    private TableColumn<?, ?> itemNameColumn;
    @FXML
    private TableColumn<?, ?> priceColumn;
    @FXML
    private TableColumn<?, ?> categoryColumn;
    @FXML
    private Button itemsbtn;
    @FXML
    private Button MyWishlistbtn;
    @FXML
    private Button updatebtn;
    @FXML
    private Button deletebtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void MyWishListBtn(ActionEvent event) {
    }
    
}
