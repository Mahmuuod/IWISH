/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientApp;

import Utilities.FriendInfo;
import Utilities.FriendWishInfo;
import Utilities.ServerAccess;
import Utilities.UserInfo;
import Utilities.Utilities;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private int currentUserId = UserInfo.getUser().getUser_id();

    private FriendInfo selectedFriend;
    @FXML
    private TextField searchlabel;
    @FXML
    private Button searchbtn;
    
    
    
    @FXML
    private void addbalancefn(ActionEvent event) {
        Utilities.ChangeScene("Addbalance.fxml",event);

    }

    @FXML
    private void notificationfn(ActionEvent event) {
       Utilities.ChangeScene("Notifications.fxml",event);

    }

    @FXML
    private void logoutfn(ActionEvent event) {
      Utilities.ChangeScene("SignIn.fxml",event);

    }
            @FXML
    private void friendrequestbtn(ActionEvent event) {

                Utilities.ChangeScene("Friendrequest.fxml",event);
    }

    @FXML
   public void refreshWish(ActionEvent event) throws IOException {
       Utilities.ChangeScene("WishList.fxml", event);
   }
    @FXML
   public void friendlistbtn(ActionEvent event) throws IOException {
       Utilities.ChangeScene("FriendList.fxml", event);
   }
    @FXML
   public void itemsBtn (ActionEvent event) throws IOException {
       Utilities.ChangeScene("Item.fxml", event);

   }
    @FXML
    private void handleSearchBtn(ActionEvent event) {
        if (!searchlabel.getText().trim().isEmpty()) {
            JSONObject data = new JSONObject();
            ServerAccess SA = new ServerAccess();

            data.put("header", "search friend");
            data.put("query", searchlabel.getText());
            data.put("userID", UserInfo.getUser().getUser_id());

            System.out.println("Sending request: " + data);

            SA.ServerInit();
            SA.ServerWrite(data);
            JSONObject response = SA.ServerReadSearch();
            JSONObject responseData = response.getJSONObject("response");
            System.out.println("Full Server Response: " + responseData.toString());

            loadFriendsList(responseData);

        }
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
                    Utilities.ChangeScene("Friendrequest.fxml", event);
                }
            });
            return;
        } else if (response.getString("header").equals("friendlist") || response.getString("header").equals("friend search result")) {

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
        } else {
            System.out.println("No friends to show!");
            friendObjects.clear();
            friends_list.refresh();
        }
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

            // Set the cell factory for the action column
            item_action_col.setCellFactory(tc -> new TableCell<FriendWishInfo, Void>() {
                private final Button contributeButton = new Button("Contribute");
                private final Label completedLabel = new Label("Completed");

                {
                    contributeButton.setOnAction(event -> {
                        FriendWishInfo wish = getTableView().getItems().get(getIndex());
                        handleContributeAction(wish);
                    });

                    completedLabel.setStyle("-fx-alignment: CENTER; -fx-text-fill: green; -fx-font-weight: bold;");
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                        setGraphic(null);
                    } else {
                        FriendWishInfo wish = (FriendWishInfo) getTableRow().getItem();
                        if (wish.getCollected() >= wish.getPrice()) {
                            // Show "Completed" label if the wish is fully funded
                            setGraphic(completedLabel);
                        } else {
                            // Show "Contribute" button if the wish is not yet completed
                            setGraphic(contributeButton);
                        }
                        setStyle("-fx-alignment: CENTER;");
                    }
                }
            });

        } else {
            friend_wishlist_table.setItems(FXCollections.observableArrayList()); // Clear table if no items
            System.out.println("No wishlist found.");
        }
    }

    private void handleContributeAction(FriendWishInfo wish) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ContributePopup.fxml"));
            Parent root = loader.load();

            ContributePopupController controller = loader.getController();
            controller.setWish(wish);

            controller.setOnContributeCallback(() -> {
                double contributionAmount = Double.parseDouble(controller.getAmountField().getText());
                processContribution(wish, contributionAmount);
            });

            
            Stage stage = new Stage();
            stage.setTitle("Contribute to Wish");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); 
            stage.showAndWait(); 

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to open the contribution window.");
        }
    }

    private void processContribution(FriendWishInfo wish, double contributionAmount) {
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
            // Update the collected amount
            wish.setCollected(wish.getCollected() + contributionAmount);

            // Refresh the table to reflect the updated state
            friend_wishlist_table.refresh();

            // If the wish is fully funded, notify the server
            if (wish.getCollected() >= wish.getPrice()) {
                sendWishCompletedRequest(wish);
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
