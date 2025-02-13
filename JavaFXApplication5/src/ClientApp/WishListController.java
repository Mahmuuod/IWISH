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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;


public class WishListController implements Initializable {
ServerAccess SA=new ServerAccess();


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
    private TableView<WishInfo> wishlisttable;
    @FXML
    private Button Deletebtn;
    @FXML
    private Button items;
    @FXML
    private TableColumn<WishInfo, String> ItemName;
    @FXML
    private TableColumn<WishInfo, Integer> PriceCol;
    @FXML
    private TableColumn<WishInfo, Integer> LeftCol;
    @FXML
    private Button refreshbtn;
    @FXML
    private TableColumn<WishInfo, Date> wishdateCol;
    private UserInfo userData;
  
 @FXML
 public void deleteWish (ActionEvent event) throws IOException {
   WishInfo selectedWish = wishlisttable.getSelectionModel().getSelectedItem();
    if (selectedWish != null) 
    {
        JSONObject request = new JSONObject();
        request.put("header", "deletewish");
        request.put("wishid",selectedWish.getWish_id());
        request.put("userid", UserInfo.getUser().getUser_id());
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

    @FXML
private void friendrequestbtn(ActionEvent event) {

            switchToFriendRequestScene(event,UserInfo.getInstance());
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
 private void switchToFriendRequestScene(ActionEvent event, UserInfo loggedInUser) {
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
}
    @Override
public void initialize(URL url, ResourceBundle rb) {
        
ObservableList<WishInfo> wishesTable=FXCollections.observableArrayList();
ItemName.setCellValueFactory(new PropertyValueFactory<>("Item_Name"));
PriceCol.setCellValueFactory(new PropertyValueFactory<>("Item_Price"));
wishdateCol.setCellValueFactory(new PropertyValueFactory<>("WISH_DATE"));
LeftCol.setCellValueFactory(new PropertyValueFactory<>("contribution_amount"));
//WishId.setCellValueFactory(new PropertyValueFactory<>("wish_id") );
        
        
        int user_id=UserInfo.getUser().getUser_id();
        JSONObject request=new JSONObject();
        request.put("header", "wishlist");
        request.put("user_id", UserInfo.getUser().getUser_id());
        SA.ServerInit();
        SA.ServerWrite(request);
        System.out.println(request);

        JSONObject wishes=SA.ServerRead();
        System.out.println(wishes);

    
   
   if(wishes.getString("header").equals("wishlist"))
   {
       JSONArray wishesArray = new JSONArray(wishes.getJSONArray("wishes"));
       // wishesTable
        for (int i = 0; i < wishesArray.length(); i++) {
            JSONObject wish = wishesArray.getJSONObject(i);
            System.out.println(wish);
            String Item_Name = wish.getString("Item_Name");
            String WISH_DATE = wish.getString("WISH_DATE");
            int Item_Price = wish.getInt("Item_Price");
            int contribution_amount = wish.getInt("contribution_amount");
            int wish_id=wish.getInt("wish_id");
             info=new WishInfo(Item_Name,Item_Price,Date.valueOf(WISH_DATE),contribution_amount,wish_id);
            System.out.println(info.toString());
            wishesTable.add(info);
           
        }

       
       
        wishlisttable.setItems(wishesTable);
           SA.ServerKill(); 
   }
    }    
    WishInfo info;
    
 public void setUserData(UserInfo user) {
    this.userData = user;
    System.out.println("Requester ID (Logged-in user): " + userData.getUser_id());
   
}
}
