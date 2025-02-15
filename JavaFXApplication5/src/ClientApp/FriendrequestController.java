package ClientApp;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author ascom
 */
public class FriendrequestController implements Initializable {
    Utilities u=new Utilities();
    UserInfo user;
    @FXML
    private Button wishlistbtn;
    @FXML
    private Button friendrequestbtn;
    @FXML
    private Button friendlistbtn;
    @FXML
    private Button addbalancebtn;
    @FXML
    private Button notificationbtn;
    @FXML
    private Button logoutbtn;
     @FXML
    private Button acceptbtn;
      @FXML
    private Button declinebtn;
    @FXML
    private TableView<UserInfo> addfriendtable;
    @FXML
    private TableColumn<UserInfo, String> addfriendtableUsernameColumn;
    @FXML
    private TableColumn<UserInfo, String> addfriendtableEmailColumn;
     @FXML
    private TableView<UserInfo> pendinglist;
    @FXML
    private TableColumn<UserInfo, String> pendingNameColumn;
    @FXML
    private TableColumn<UserInfo, String> pendingEmailColumn;
    @FXML
    private TableColumn<UserInfo, String> addfriendtableBirthDateColumn;
    @FXML
    private TextField searchlabel;
    @FXML
    private Button searchbtn;
    @FXML
    private Button addfriendbtn;
    private ObservableList<UserInfo> searchResults = FXCollections.observableArrayList();
    private ObservableList<UserInfo> pendingRequests = FXCollections.observableArrayList();
    @FXML
    private Label username_lbl;
    @FXML
    private Label balance_lbl;
    @FXML
    private Button refreshbtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

       if(user!=null)
           updateUI();
          
    }
private void fetchPendingRequests() {
    System.out.println("Fetching pending friend requests...");

  
    System.out.println("Requester ID (Logged-in user): " + user.getUser_id());//Debugging   
    JSONObject data = new JSONObject();
    ServerAccess SA = new ServerAccess();
    SA.ServerInit();

    data.put("header", "fetch pending requests");
    data.put("userID", user.getUser_id());

    SA.ServerWrite(data);
    JSONObject response = SA.ServerReadSearch();

    System.out.println("Received pending requests: " + response.toString()); // Debugging

   
        response = response.getJSONObject("response");
   
    if (!response.has("users")) {
        System.out.println("ERROR: No pending requests found in response!");
        return;
    }

    JSONArray usersArray = response.getJSONArray("users");
    
    if (usersArray.isEmpty()) {
        System.out.println("INFO: No pending friend requests.");
        return;
    }

    pendingRequests.clear();

    for (int i = 0; i < usersArray.length(); i++) {
        JSONObject userJson = usersArray.getJSONObject(i);

        String birthdateString = userJson.optString("Birthdate", "2000-01-01").split(" ")[0]; // Remove time
        Date birthdate = Date.valueOf(birthdateString);

        System.out.println("Adding to pending list: " + userJson.toString()); // Debugging

        pendingRequests.add(new UserInfo(
            userJson.getInt("User_id"),
            userJson.optString("Username", ""),
            birthdate,  
            userJson.optString("Email", "")
        ));
    }

    pendinglist.refresh();
}




    @FXML
    private void handleSearchBtn(ActionEvent event) {
        if (!searchlabel.getText().trim().isEmpty()) {
            JSONObject data = new JSONObject();
            ServerAccess SA = new ServerAccess();

            data.put("header", "search user");
            data.put("query", searchlabel.getText());
            data.put("userID", user.getUser_id());

            System.out.println("Sending request: " + data);

            SA.ServerInit();
            SA.ServerWrite(data);
            JSONObject response = SA.ServerReadSearch();

            System.out.println("Full Server Response: " + response.toString());

            if (response.has("response")) {
                JSONObject responseData = response.getJSONObject("response");
                if (responseData.has("users")) {
                    System.out.println("Users found in response!");
                    JSONArray usersArray = responseData.getJSONArray("users");
                    searchResults.clear();

                    for (int i = 0; i < usersArray.length(); i++) {
                        JSONObject userJson = usersArray.getJSONObject(i);
                        System.out.println("Processing User: " + userJson.toString());

                        UserInfo user = new UserInfo(
                            userJson.getInt("User_id"),
                            userJson.optString("Username", ""),
                            Date.valueOf(userJson.optString("Birthdate", "2000-01-01")),
                            userJson.optString("Email", "")
                        );
                        searchResults.add(user);
                    }
                    addfriendtable.refresh();
                } else {
                    System.out.println("No 'users' field in response!");
                    searchResults.clear();
                    addfriendtable.refresh();
                }
            } else {
                System.out.println("No 'response' field in server response!");
            }
        }
    }
   
    
 /*public void setUserData(UserInfo user) {
    this.userData = user;
    System.out.println("Requester ID (Logged-in user): " + userData.getUser_id());
    fetchPendingRequests();
}*/
  @FXML private void handleRefreshBtn(ActionEvent event) {
        
        fetchPendingRequests();
        
    }


    @FXML
    private void handleAddFriendBtn(ActionEvent event) {
        UserInfo selectedUser = addfriendtable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(null, "Please select a user first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JSONObject data = new JSONObject();
        data.put("header", "send friend request");
        data.put("requester_id", user.getUser_id());
        data.put("receiver_id", selectedUser.getUser_id());
        data.put("requester_name", user.getUsername());

        System.out.println("Sending request: " + data);

        ServerAccess SA = new ServerAccess();
        SA.ServerInit();
        SA.ServerWrite(data);
        JSONObject response = SA.ServerRead();

        System.out.println("Server response: " + response);

        String status = response.getString("status");
        if (status.equals("success")) {
            JOptionPane.showMessageDialog(null, "Friend request has been sent successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        if (response.getString("status").equals("duplicate")) {
            JOptionPane.showMessageDialog(null, "The friend request has already been sent!", "Error", JOptionPane.WARNING_MESSAGE);
        }
        if (response.getString("status").equals("already friends")) {
            JOptionPane.showMessageDialog(null, selectedUser.getUsername()+"  is already on your friend list!", "Error", JOptionPane.WARNING_MESSAGE);
        }
        if (status.equals("failed")) {
            JOptionPane.showMessageDialog(null, "Failed to send friend request.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    @FXML
private void handleAcceptBtn(ActionEvent event) {
    UserInfo selectedUser = pendinglist.getSelectionModel().getSelectedItem();
    
    if (selectedUser == null) {
        JOptionPane.showMessageDialog(null, "Please select a pending request first!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

  /*  if (userData == null) {
        System.out.println("ERROR: userData is null!");
        return;
    }*/

    JSONObject data = new JSONObject();
    data.put("header", "accept friend request");
    data.put("receiver_id", user.getUser_id());
    data.put("requester_id", selectedUser.getUser_id());
    data.put("receiver_name", user.getUsername());

    System.out.println(user.getUsername() + " accepted " + selectedUser.getUsername());
    System.out.println("Sending request: " + data.toString()); // Debugging

    ServerAccess SA = new ServerAccess();
    SA.ServerInit();
    SA.ServerWrite(data);
    JSONObject response = SA.ServerRead();

    System.out.println("Server response: " + response);

    if (response.has("status")) {
        String status = response.getString("status");
        if ("success".equals(status)) {
            JOptionPane.showMessageDialog(null, "Friend request has been accepted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            pendingRequests.remove(selectedUser); 
            pendinglist.refresh();
        } else {
            JOptionPane.showMessageDialog(null, "Failed to accept friend request.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

    
     @FXML
private void handleDeclineBtn(ActionEvent event) {
    UserInfo selectedUser = pendinglist.getSelectionModel().getSelectedItem();

    if (selectedUser == null) {
        JOptionPane.showMessageDialog(null, "Please select a request to decline!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    JSONObject data = new JSONObject();
    data.put("header", "decline friend request");
    data.put("requester_id", selectedUser.getUser_id()); 
    data.put("receiver_id", user.getUser_id());

    System.out.println(user.getUsername() + " Declined friend request from " + selectedUser.getUsername());
    System.out.println("Sending request: " + data.toString()); // Debugging

    ServerAccess SA = new ServerAccess();
    SA.ServerInit();
    SA.ServerWrite(data);
    JSONObject response = SA.ServerRead();

    System.out.println("Server response: " + response); // Debugging

    if (response.has("status") && response.getString("status").equals("success")) {
        JOptionPane.showMessageDialog(null, "Friend request declined successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        pendingRequests.remove(selectedUser); 
        pendinglist.refresh();
    } else {
        JOptionPane.showMessageDialog(null, "Failed to decline friend request!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
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

    public void itemsBtn(ActionEvent event) throws IOException {
        u.switchToItemsScene(event, user);

    }

public void setData(UserInfo user2) {
    this.user = user2;
    updateUI();  // Now update UI after user is set
}

    private void updateUI() {
        
        addfriendtableUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("Username"));
        addfriendtableEmailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));
        addfriendtableBirthDateColumn.setCellValueFactory(new PropertyValueFactory<>("Birthdate"));
        addfriendtable.setItems(searchResults);
        pendingNameColumn.setCellValueFactory(new PropertyValueFactory<>("Username"));
        pendingEmailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));
        pendinglist.setItems(pendingRequests); // Bind the list to TableView
        balance_lbl.setText(String.valueOf(user.getUser_balance()));
        username_lbl.setText(String.valueOf(user.getUsername()));
    }

 
     

    
}