/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientApp;
 
import Utilities.ServerAccess;
import Utilities.UserInfo;
import Utilities.Utilities;
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

    @FXML
    private Label balance_lbl;
    @FXML
    private Label username_lbl;
    @FXML
    private TableView<NotificationInfo> notifi_table;
    @FXML
    private TableColumn<NotificationInfo, ?> notifi_col;
    @FXML
    private ScrollBar notifi_scroll;
    @FXML
    private Button FriendList_btn;
    @FXML
    private TableColumn<NotificationInfo, ?> date_col;
    private int currentUserId = UserInfo.getUser().getUser_id();
    @FXML
    private Button NotificatioList_btn;
    
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
        setupTableColumns();
        JSONObject data = new JSONObject();
        ServerAccess SA = new ServerAccess();
        data.put("header", "show notifications");
        data.put("user_id", currentUserId);

        SA.ServerInit();
        SA.ServerWrite(data);
        JSONObject response = SA.ServerRead();
        loadNotificationsList(response);
    }

}