/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author randa
 */
public class NotificationsController implements Initializable {

    private Label balance_lbl;
    private Label username_lbl;
    @FXML
    private TableView<NotificationInfo> notifi_table;
    @FXML
    private TableColumn<NotificationInfo, ?> notifi_col;
    @FXML
    private Button FriendList_btn;
    @FXML
    private TableColumn<NotificationInfo, ?> date_col;
    UserInfo user;
    private int currentUserId ;
    @FXML
    private Button NotificatioList_btn;
    Utilities u=new Utilities();
    @FXML
    private Button addbalancebtn;
    @FXML
    private Button friendrequestbtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private Label username_lbl1;
    @FXML
    private Label balance_lbl1;
    @FXML
    private TableColumn<?, ?> pendingNameColumn;
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
    private void setupTableColumns() {

        // binds objects to rows where each attribute is bound to a column
        date_col.setCellValueFactory(new PropertyValueFactory<>("Notification_date"));
        notifi_col.setCellValueFactory(new PropertyValueFactory<>("Context"));

        centerAlignColumn(date_col);

        centerAlignColumn(notifi_col);

    }

    private <T> void centerAlignColumn(TableColumn<NotificationInfo, T> column) {
        column.setCellFactory(tc -> new TableCell<NotificationInfo, T>() {
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

    private void loadNotificationsList(JSONObject response) {
        
        if (response.getString("header").equals("notification list")) {
            JSONArray notificationArray = response.getJSONArray("notifications");
            ObservableList<NotificationInfo> notifications = FXCollections.observableArrayList();

            for (int i = 0; i < notificationArray.length(); i++) {
                JSONObject notificationJson = notificationArray.getJSONObject(i);
                NotificationInfo notification = new NotificationInfo(
                        notificationJson.getInt("notification_id"),
                        notificationJson.getString("context"),
                        Date.valueOf(notificationJson.getString("notification_date")),
                        notificationJson.getString("isread"));
                notifications.add(notification);
            }

            notifi_table.setItems(notifications);

            
        } else {
            notifi_table.setItems(FXCollections.observableArrayList()); // Clear table if no notifications
            System.out.println("No notifications found.");
        }

        
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
if(user!=null)
{
        System.out.println(user.toString());
    updateUI();}
    }
public void setData(UserInfo user2) {
    this.user = user2;
    updateUI();  // Now update UI after user is set
}

    private void updateUI() {
       username_lbl1.setText(user.getUsername());
       balance_lbl1.setText(String.valueOf(user.getUser_balance()));
        setupTableColumns();
        JSONObject data = new JSONObject();
        ServerAccess SA = new ServerAccess();
        data.put("header", "show notifications");
        data.put("user_id", user.getUser_id());

        SA.ServerInit();
        SA.ServerWrite(data);
        JSONObject response = SA.ServerRead();
        loadNotificationsList(response);    }


}