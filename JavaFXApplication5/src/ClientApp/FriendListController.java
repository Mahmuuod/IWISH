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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
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
    private Button WishList_btn;
    @FXML
    private Button AddFriend_btn;
    @FXML
    private Button ContributionList_btn;
    @FXML
    private Button AddBalance_btn;
    @FXML
    private Button Notifications_btn;
    @FXML
    private Button Logout_btn;
    @FXML
    private Label balance_lbl;
    @FXML
    private Label username_lbl;
    @FXML
    private ListView<FriendInfo> friends_list;
    @FXML
    private Button remove_friend_btn;
    @FXML
    private TableView<FriendWishInfo> friend_wishlist_table;
    @FXML
    private TableColumn<FriendWishInfo, ?> item_name_col;
    @FXML
    private TableColumn<FriendWishInfo, ?> item_price_col;
    @FXML
    private TableColumn<FriendWishInfo, ?> item_collected_col;
    @FXML
    private TableColumn<FriendWishInfo, Void> item_action_col;
    @FXML
    private TableColumn<FriendWishInfo, ?> item_date_col;
    @FXML
    private Tab info_tab;
    @FXML
    private TextArea friend_info_textarea;
    @FXML
    private Tab wishlist_tab;
    private int currentUserId = 2;

    private FriendInfo selectedFriend;

    @FXML
    private void handleFriendListButtonAction(ActionEvent event) {
        Utilities.ChangeScene("FriendList.fxml", event);

    }
    
    @FXML
    private void handleNotificationsButtonAction(ActionEvent event) {
        
        Utilities.ChangeScene("Notifications.fxml", event);
        

        
    }
    
    private void setupTableColumns() {

        // binds objects to rows where each attribute is bound to a column
        item_date_col.setCellValueFactory(new PropertyValueFactory<>("wish_date"));
        item_name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        item_price_col.setCellValueFactory(new PropertyValueFactory<>("price"));
        item_collected_col.setCellValueFactory(new PropertyValueFactory<>("collected"));
        item_action_col.setCellValueFactory(new PropertyValueFactory<>("action"));

        centerAlignColumn(item_date_col);

        centerAlignColumn(item_name_col);

        centerAlignColumn(item_price_col);

        centerAlignColumn(item_collected_col);

        centerAlignColumn(item_action_col);

    }
// Method to apply center alignment

    private <T> void centerAlignColumn(TableColumn<FriendWishInfo, T> column) {
        column.setCellFactory(tc -> new TableCell<FriendWishInfo, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Center-align text inside the cell
                    setText(item.toString());
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
    }

    private void loadFriendsList(JSONObject response) {
        ObservableList<FriendInfo> friendObjects = FXCollections.observableArrayList();

        if (response.getString("header").equals("no friends")) {
            // Create a dummy FriendInfo object for the "no friends" message
            FriendInfo noFriendMessage = new FriendInfo(-1, "You don't have friends yet.", "Click to add friends.", "", null, "");
            friendObjects.add(noFriendMessage);

            friends_list.setItems(friendObjects);

            // Set a custom cell factory to display the message
            friends_list.setCellFactory(lv -> new ListCell<FriendInfo>() {
                @Override
                protected void updateItem(FriendInfo item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getFirst_name() + " " + item.getLast_name());
                    }
                }
            });

            // Handle click on the "no friends" message
            friends_list.setOnMouseClicked(event -> {
                if (friends_list.getSelectionModel().getSelectedItem() != null
                        && friends_list.getSelectionModel().getSelectedItem().getFriend_id() == -1) {
                    Utilities.ChangeScene("AddFriend.fxml", event);
                }
            });
            return;
        }

        JSONArray friendsArray = response.getJSONArray("friends");

        for (int i = 0; i < friendsArray.length(); i++) {
            JSONObject friendJson = friendsArray.getJSONObject(i);
            FriendInfo friend = new FriendInfo(
                    friendJson.getInt("friend_id"),
                    friendJson.getString("firstname"),
                    friendJson.getString("lastname"),
                    friendJson.getString("username"),
                    Date.valueOf(friendJson.getString("birthdate")),
                    friendJson.getString("email")
            );
            friendObjects.add(friend);
        }

        friends_list.setItems(friendObjects);

        // Set a custom cell factory to display the friend's name
        friends_list.setCellFactory(lv -> new ListCell<FriendInfo>() {
            @Override
            protected void updateItem(FriendInfo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFirst_name() + " " + item.getLast_name());
                }
            }
        });

        // Handle selection
        friends_list.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.getFriend_id() != -1) { // Ignore the "no friends" message
                selectedFriend = newVal;
                handleSelectFriend(selectedFriend);
                handleShowFriendWishlist(selectedFriend);
            }
        });
    }

    private void reloadFriendsList() {
        JSONObject data = new JSONObject();
        ServerAccess SA = new ServerAccess();
        data.put("header", "show friendlist");
        data.put("user_id", currentUserId);

        SA.ServerInit();
        SA.ServerWrite(data);
        System.out.println(data);
        JSONObject response = SA.ServerRead();
        loadFriendsList(response);

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

            // Get the current list, remove selected friend, and update the ListView
            ObservableList<FriendInfo> friends = friends_list.getItems();
            friends.remove(selectedFriend);
            friends_list.setItems(FXCollections.observableArrayList(friends)); // Refresh list

            // Clear selection, friend info and wishlist
            friends_list.getSelectionModel().clearSelection();
            selectedFriend = null;
            friend_info_textarea.clear();
            friend_wishlist_table.setItems(FXCollections.observableArrayList());

            reloadFriendsList();

            JSONObject response = SA.ServerRead();
        } else {
            System.out.println("No friend selected!");
        }

    }

    private void handleSelectFriend(FriendInfo friend) {
        if (friend != null) {
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

    private void handleShowFriendWishlist(FriendInfo friend) {
        if (friend == null) {
            return;
        }
        JSONObject data = new JSONObject();
        ServerAccess SA = new ServerAccess();
        data.put("header", "show friend wishlist");
        data.put("friend_id", friend.getFriend_id());

        SA.ServerInit();
        SA.ServerWrite(data);
        JSONObject response = SA.ServerRead();

        if (response.getString("header").equals("friend wishlist")) {
            JSONArray wishlistArray = response.getJSONArray("wishlist");
            ObservableList<FriendWishInfo> wishlistItems = FXCollections.observableArrayList();

            for (int i = 0; i < wishlistArray.length(); i++) {
                JSONObject itemJson = wishlistArray.getJSONObject(i);
                FriendWishInfo item = new FriendWishInfo(
                        itemJson.getInt("friend_id"),
                        itemJson.getInt("wish_id"),
                        Date.valueOf(itemJson.getString("wish_date")),
                        itemJson.getString("name"),
                        itemJson.getDouble("price"),
                        itemJson.getDouble("collected"),
                        itemJson.getString("status"));
                wishlistItems.add(item);
            }

            friend_wishlist_table.setItems(wishlistItems);

            // Add "Contribute" button in the action column
            item_action_col.setCellFactory(tc -> new TableCell<FriendWishInfo, Void>() {
                private final Button contributeButton = new Button("Contribute");

                {
                    contributeButton.setOnAction(event -> {
                        FriendWishInfo wish = getTableView().getItems().get(getIndex());
                        handleContributeAction(wish);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(contributeButton);
                        // Center-align the button inside the cell
                        setStyle("-fx-alignment: CENTER;");
                    }
                }
            });

        } else {
            friend_wishlist_table.setItems(FXCollections.observableArrayList()); // Clear table if no items
            System.out.println("No wishlist found.");
        }

        // Switch to wishlist tab
        //wishlist_tab.getTabPane().getSelectionModel().select(wishlist_tab);
    }

    private void handleContributeAction(FriendWishInfo wish) {
        double remainingAmount = wish.getPrice() - wish.getCollected();
        if (remainingAmount <= 0) {
            updateActionColumnForCompletedWish(wish);
            return;
        }

        double contributionAmount;
        try {
            contributionAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter contribution amount:"));
            if (contributionAmount <= 0) {
                JOptionPane.showMessageDialog(null, "Invalid amount!");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number!");
            return;
        }

        // if the contribution less than the remaining amount then only reduce the remaining amount from balance 
        if (contributionAmount > remainingAmount) {
            contributionAmount = remainingAmount;
        }

        JSONObject contributionData = new JSONObject();
        contributionData.put("header", "contribute");
        contributionData.put("wish_id", wish.getWish_id());
        contributionData.put("contribution_amount", contributionAmount);
        contributionData.put("contributor_id", currentUserId);

        ServerAccess SA = new ServerAccess();
        SA.ServerInit();
        SA.ServerWrite(contributionData);
        System.out.println("Sent contribution: " + contributionData);

        JSONObject response = SA.ServerRead();

        if (response.getString("header").equals("contribution added")) {

            wish.setCollected(wish.getCollected() + contributionAmount);
            friend_wishlist_table.refresh();

            // If wish is fully funded, notify the server and replace the button with label
            if (wish.getCollected() >= wish.getPrice()) {
                sendWishCompletedRequest(wish);
                
                updateActionColumnForCompletedWish(wish);
            }

        } else if (response.getString("header").equals("not enough balance")) {
            JOptionPane.showMessageDialog(null, "Not enough balance! Add balance and try again.");
        } else {
            JOptionPane.showMessageDialog(null, "Contribution failed. Try again!");
        }
    }

    private void sendWishCompletedRequest(FriendWishInfo wish) {
        JSONObject data = new JSONObject();
        data.put("header", "wish completed");
        data.put("wish_id", wish.getWish_id());
        data.put("friend_id", wish.getFriend_id());

        ServerAccess SA = new ServerAccess();
        SA.ServerInit();
        SA.ServerWrite(data);
        System.out.println("Sent wish completion request: " + data);

        JSONObject response = SA.ServerRead();

    }

    private void updateActionColumnForCompletedWish(FriendWishInfo wish) {
        item_action_col.setCellFactory(tc -> new TableCell<FriendWishInfo, Void>() {
            private final Label completedLabel = new Label("Completed");

            {
                completedLabel.setStyle("-fx-alignment: CENTER; -fx-text-fill: green; -fx-font-weight: bold;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || wish.getCollected() < wish.getPrice()) {
                    setGraphic(null);
                } else {
                    setGraphic(completedLabel);
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });

        friend_wishlist_table.refresh();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        JSONObject data = new JSONObject();
        ServerAccess SA = new ServerAccess();
        data.put("header", "show friendlist");
        data.put("user_id", currentUserId);

        SA.ServerInit();
        SA.ServerWrite(data);
        System.out.println(data);
        JSONObject response = SA.ServerRead();
        loadFriendsList(response);
    }

}
