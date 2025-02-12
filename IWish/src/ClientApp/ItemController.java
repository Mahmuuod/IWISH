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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ascom
 */
public class ItemController implements Initializable {

    @FXML
    private Button itemsbtn;
    @FXML
    private Button MyWishlistbtn;
    @FXML
    private AnchorPane Item_id;
    @FXML
    private TableView<?> itemTable;
    @FXML
    private TableColumn<?, ?> ItemId;
    @FXML
    private TableColumn<?, ?> Name;
    @FXML
    private TableColumn<?, ?> Price;
    @FXML
    private TableColumn<?, ?> Category;
    @FXML
    private Button AddItembtn;

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

    @FXML
    private void addItemToWishList(ActionEvent event) {
    }
    
}
