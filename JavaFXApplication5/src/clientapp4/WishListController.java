/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientapp4;

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
 * @author osama
 */
public class WishListController implements Initializable {

    @FXML
    private Button wishlistbtn;
    @FXML
    private Button friendrequestbtn;
    @FXML
    private Button friendlistbtn;
    @FXML
    private Button contributionlistbtn;
    @FXML
    private Button addbalancebtn;
    @FXML
    private Button notificationbtn;
    @FXML
    private Button logoutbtn;
    @FXML
    private Button items;
    @FXML
    private TableView<?> wishlisttable;
    @FXML
    private TableColumn<?, ?> ItemName;
    @FXML
    private TableColumn<?, ?> PriceCol;
    @FXML
    private TableColumn<?, ?> LeftCol;
    @FXML
    private Button Deletebtn;
    @FXML
    private Button refreshbtn;
    @FXML
    private TableColumn<?, ?> wishidCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void itemsBtn(ActionEvent event) {
    }


    @FXML
    private void deleteWish(ActionEvent event) {
    }

    @FXML
    private void refreshWish(ActionEvent event) {
    }
    
}
