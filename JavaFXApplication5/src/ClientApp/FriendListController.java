/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientApp;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 * FXML Controller class
 *
 * @author randa
 */
public class FriendListController implements Initializable {

    @FXML
    private Button FriendList_btn;
    @FXML
    private Label balance_lbl;
    @FXML
    private Label username_lbl;
    @FXML
    private ListView<Hyperlink> friends_list;
    @FXML
    private Button remove_friend_btn;
    @FXML
    private TableView<?> friend_wishlist_table;
    @FXML
    private TableColumn<?, ?> item_name_col;
    @FXML
    private TableColumn<?, ?> item_price_col;
    @FXML
    private TableColumn<?, ?> item_collected_col;
    @FXML
    private TableColumn<?, ?> Item_action_col;
    private int currentUserId = 1;
    JSONObject response;
    
    
    @FXML
    private void handleSignInButtonAction(ActionEvent event) {
        JSONObject data = new JSONObject();
        ServerAccess SA = new ServerAccess();
        data.put("header", "show friendlist");
        data.put("user_id", 1);

        SA.ServerInit();
        SA.ServerWrite(data);
        System.out.println(data);
        response = SA.ServerRead();
        loadFriendsList();
        //System.out.println(response);

    }
    private void setupTableColumns() {
        // binds objects to rows where each attribute is bound to a column
        /*item_name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        item_price_col.setCellValueFactory(new PropertyValueFactory<>("price"));
        item_collected_col.setCellValueFactory(new PropertyValueFactory<>("collected"));
        Item_action_col.setCellValueFactory(new PropertyValueFactory<>("action"));
        */
    }
    private void loadFriendsList() {
        JSONArray friendsArray = new JSONArray(response.getJSONArray("friends"));
        ObservableList<Hyperlink> friendLinks = FXCollections.observableArrayList();
        
        
        for (int i = 0; i < friendsArray.length(); i++) {
            JSONObject friend = friendsArray.getJSONObject(i);
            String friendName = friend.getString("firstname") + " " + friend.getString("lastname");
            int friendId = friend.getInt("friend_id");

            Hyperlink friendLink = new Hyperlink(friendName);
            //friendLink.setOnAction(event -> loadWishlist(friendId));
            
            friendLinks.add(friendLink);
           
        }
        
        friends_list.setItems(friendLinks);
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }    
    
}
