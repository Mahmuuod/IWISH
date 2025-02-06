/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientApp;

import java.net.URL;
import java.sql.Date;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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
    @FXML
    private Tab info_tab;
    @FXML
    private TextArea friend_info_textarea; 
    @FXML
    private Tab wishlist_tab;

    private int currentUserId = 2;
    JSONObject response;
    private FriendInfo selectedFriend;
    JSONObject data;

    @FXML
    private void handleFriendListButtonAction(ActionEvent event) {
        JSONObject data = new JSONObject();
        ServerAccess SA = new ServerAccess();
        data.put("header", "show friendlist");
        data.put("user_id", currentUserId);

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
        if (response.getString("header").equals("no friends")) {
            Hyperlink noFriend = new Hyperlink("You don't have friends yet, Click here to add friends");
            //TODO change scene to add friend scene
            //noFriend.setOnAction(event -> handleAddFriendButtonAction());
            ObservableList<Hyperlink> noFrinedsLink = FXCollections.observableArrayList();
            noFrinedsLink.add(noFriend);
            friends_list.setItems(noFrinedsLink);
        } else {

            JSONArray friendsArray = new JSONArray(response.getJSONArray("friends"));
            ObservableList<Hyperlink> friendLinks = FXCollections.observableArrayList();

            for (int i = 0; i < friendsArray.length(); i++) {
                JSONObject friendJson = friendsArray.getJSONObject(i);
                // creating object to store info from JSON
                FriendInfo friend = new FriendInfo(
                        friendJson.getInt("friend_id"),
                        friendJson.getString("firstname"),
                        friendJson.getString("lastname"),
                        friendJson.getString("username"),
                        Date.valueOf(friendJson.getString("birthdate")),
                        friendJson.getString("email")
                );

                String friendName = friendJson.getString("firstname") + " " + friendJson.getString("lastname");

                Hyperlink friendLink = new Hyperlink(friendName);

                // Click event to select friend
                friendLink.setOnAction(event -> {
                    selectedFriend = friend;
                    highlightSelectedFriend(friendLink);
                    handleSelectFriend(selectedFriend);
                });

                friendLinks.add(friendLink);

            }

            friends_list.setItems(friendLinks);
        }
    }

    private void highlightSelectedFriend(Hyperlink selectedLink) {
        for (Hyperlink node : friends_list.getItems()) {
            node.setStyle(""); // Reset previous styles
        }
        selectedLink.setStyle("-fx-font-weight: bold; -fx-text-fill: blue;"); // Highlight selected friend
    }

    private void reloadFriendsList() {
        JSONObject data = new JSONObject();
        ServerAccess SA = new ServerAccess();
        data.put("header", "show friendlist");
        data.put("user_id", currentUserId);

        SA.ServerInit();
        SA.ServerWrite(data);
        System.out.println(data);
        response = SA.ServerRead();
        loadFriendsList();

    }

    @FXML
    private void handleRemoveFriendButtonAction(ActionEvent event) {
        if (selectedFriend != null) {
            JSONObject data = new JSONObject();
            ServerAccess SA = new ServerAccess();
            data.put("header", "remove friend");
            data.put("user_id", currentUserId);
            data.put("friend_id", selectedFriend.getFriend_id());

            SA.ServerInit();
            SA.ServerWrite(data);
            System.out.println(data);
            //remove friend from friendlist
            friends_list.getItems().removeIf(node
                    -> node instanceof Hyperlink && ((Hyperlink) node).getText().equals(selectedFriend.getFirst_name() + " " + selectedFriend.getLast_name())
            );
            selectedFriend = null; // Reset selection
            friend_info_textarea.clear();
            response = SA.ServerRead();
            //reloadFriendsList();
        } else {
            System.out.println("No friend selected!");
        }
        //System.out.println(response);
    }

    private void handleSelectFriend(FriendInfo friend) {
        if (friend != null) {
            // Format friend's information
            String friendInfo = "Name: " + friend.getFirst_name() + " " + friend.getLast_name() + "\n"
                    + "Username: " + friend.getUsername() + "\n"
                    + "Email: " + friend.getEmail() + "\n"
                    + "Birthdate: " + friend.getBirthdate();

            // Set text in the info tab's TextArea
            friend_info_textarea.setText(friendInfo);

            // Switch to info_tab to show details
            info_tab.getTabPane().getSelectionModel().select(info_tab);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }

}
