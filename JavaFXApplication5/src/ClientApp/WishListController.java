/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientApp;

import Utilities.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import ClientApp.SignInController;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WishListController implements Initializable {
Utilities u=new Utilities();
    ServerAccess SA = new ServerAccess();
    @FXML
    private Button friendrequestbtn;
    @FXML
    private Button addbalancebtn;
    @FXML
    private TableView<WishInfo> wishlisttable;
    @FXML
    private Button items;
    private TableColumn<WishInfo, String> ItemName;
    private TableColumn<WishInfo, Integer> PriceCol;
    private TableColumn<WishInfo, Integer> LeftCol;
    @FXML
    private Button refreshbtn;
    private TableColumn<WishInfo, Date> wishdateCol;
    private UserInfo userData;
    private Label balancelbl;
    private Label usernamelbl;
    @FXML
    private TableColumn<?, ?> itemNameColumn;
    @FXML
    private TableColumn<?, ?> priceColumn;
    @FXML
    private TableColumn<?, ?> categoryColumn;
    @FXML
    private TableColumn<?, ?> categoryColumn1;
    @FXML
    private Button deletebtn;
    private Label username_lbl;
    public Label balance_lbl;
    @FXML
    private Button NotificatioList_btn;
    @FXML
    private Button FriendList_btn;
    @FXML
    private Button logoutBtn;
    @FXML
    private Label username_lbl1;
    @FXML
    private Label balance_lbl1;
//big number cant be casted , and handle string in ints
    @FXML
    public void deleteWish(ActionEvent event) throws IOException {
        WishInfo selectedWish = wishlisttable.getSelectionModel().getSelectedItem();
        if (selectedWish != null) {
            JSONObject request = new JSONObject();
            request.put("header", "deletewish");
            request.put("wishid", selectedWish.getWish_id());
            request.put("userid", user.getUser_id());
            System.out.println(selectedWish.getWish_id());
            SA.ServerInit();
            SA.ServerWrite(request);
            SA.ServerRead();
            Utilities.ChangeScene("WishList.fxml", event);
            SA.ServerKill();
        }

        //  request.put("Wish_id", selectedWish.getWishId());
        // JSONObject response = ClientSocket.sendRequest(request);
        // System.out.println("Server Response: " + response.getString("message"));
    }


    /*private void switchToFriendRequestScene(ActionEvent event, UserInfo loggedInUser) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Friendrequest.fxml"));
        Parent root = loader.load();

        // Pass user data to FriendrequestController
        FriendrequestController controller = loader.getController();
        controller.setUserData(loggedInUser);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    if (user != null) {
        updateUI();
    }
    }
    WishInfo info;

    /* public void setUserData(UserInfo user) {
    this.userData = user;
    System.out.println("Requester ID (Logged-in user): " + userData.getUser_id());
   
}*/
    private void contributionlistfn(ActionEvent event) {
        u.switchToContributePopupScene(event, user);

    }

    @FXML
    private void addbalancefn(ActionEvent event) {
        u.switchToAddbalanceScene(event, user);

    }

    @FXML
    private void notificationfn(ActionEvent event) {
        u.switchToNotificationScene(event, user);

    }

    @FXML
    private void logoutfn(ActionEvent event) {
        Utilities.ChangeScene("SignIn.fxml", event);

    }

    @FXML
    private void friendrequestbtn(ActionEvent event) {

        u.switchToFriendrequestScene(event, user);
    }

    @FXML
    public void refreshWish(ActionEvent event) throws IOException {
        u.switchToWishListScene(event, user);
    }

    @FXML
    public void friendlistbtn(ActionEvent event) throws IOException {
        u.switchToFriendListScene(event, user);
    }

    @FXML
    public void itemsBtn(ActionEvent event) throws IOException {
        u.switchToItemsScene(event, user);

    }
UserInfo user;
public void setData(UserInfo user2) {
    this.user = user2;
    updateUI();  // Now update UI after user is set
}

    private void updateUI() {
        username_lbl1.setText(user.getUsername());
        balance_lbl1.setText(String.valueOf(user.getUser_balance()));

        ObservableList<WishInfo> wishesTable = FXCollections.observableArrayList();
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("Item_Name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("Item_Price"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("WISH_DATE"));
        categoryColumn1.setCellValueFactory(new PropertyValueFactory<>("contribution_amount"));
//WishId.setCellValueFactory(new PropertyValueFactory<>("wish_id") );

        wishlisttable.setRowFactory(tv -> new TableRow<WishInfo>() {
            @Override
            protected void updateItem(WishInfo wish, boolean empty) {
                super.updateItem(wish, empty);
                if (empty || wish == null) {
                    setStyle(""); // Reset style for empty rows
                } else {
                    if (wish.getContribution_amount() == wish.getItem_Price()) {
                        setStyle("-fx-background-color: #98FF98;");

                    } else {
                        setStyle(""); // Reset style for empty rows
                    }
                }
            }
        });

        int user_id = user.getUser_id();
        JSONObject request = new JSONObject();
        request.put("header", "wishlist");
        request.put("user_id", user.getUser_id());
        SA.ServerInit();
        SA.ServerWrite(request);
        System.out.println(request);

        JSONObject wishes = SA.ServerRead();
        System.out.println(wishes);

        if (wishes.getString("header").equals("wishlist")) {
            JSONArray wishesArray = new JSONArray(wishes.getJSONArray("wishes"));
            // wishesTable
            for (int i = 0; i < wishesArray.length(); i++) {
                JSONObject wish = wishesArray.getJSONObject(i);
                System.out.println(wish);
                String Item_Name = wish.getString("Item_Name");
                String WISH_DATE = wish.getString("WISH_DATE");
                int Item_Price = wish.getInt("Item_Price");
                int contribution_amount = wish.getInt("contribution_amount");
                int wish_id = wish.getInt("wish_id");
                info = new WishInfo(Item_Name, Item_Price, Date.valueOf(WISH_DATE), contribution_amount, wish_id);
                System.out.println(info.toString());
                wishesTable.add(info);

            }

            wishlisttable.setItems(wishesTable);
            SA.ServerKill();
        }    }
    
    
}
