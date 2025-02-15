package ClientApp;

import Utilities.ItemDTO;
import Utilities.ItemsInfo;
import Utilities.ServerAccess;
import Utilities.UserInfo;
import Utilities.Utilities;
import Utilities.WishInfo;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

public class ItemController implements Initializable {

    Utilities u = new Utilities();
    UserInfo user;
    ServerAccess SA = new ServerAccess();
    @FXML
    private Button MyWishlistbtn;


    private TableColumn<ItemsInfo, String> nameColumn;

    @FXML
    private TableView<ItemsInfo> itemTable;
    @FXML
    private AnchorPane Item_id;
    @FXML
    private TableColumn<ItemsInfo, String> Name;
    @FXML
    private TableColumn<ItemsInfo, String> Category;
    @FXML
    private TableColumn<ItemsInfo, Integer> Price;
    @FXML
    private TextField searchlabel;
    @FXML
    private Button searchbtn;
    @FXML
    private Button RefreshBtn;
    @FXML
    private Button AddItembtn1;

    private void fetchItems(JSONObject items) {
        if (items.getString("header").equals("items")) {
            JSONArray itemssArray = new JSONArray(items.getJSONArray("items"));
            // wishesTable
            for (int i = 0; i < itemssArray.length(); i++) {
                JSONObject item = itemssArray.getJSONObject(i);
                //System.out.println(item);
                String Item_Name = item.getString("Name");
                String Category = item.getString("Category");
                int Item_Price = item.getInt("Price");
                int item_id = item.getInt("Item_id");
                info = new ItemsInfo(item_id, Item_Name, Item_Price, Category);
                //  System.out.println(info.toString());
                Itemsdata.add(info);

            }

            itemTable.setItems(Itemsdata);
            SA.ServerKill();
        }

    }

    @FXML
    public void addItemToWishList() {
        ItemsInfo selectedItem = itemTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            JSONObject Request = new JSONObject();
            Request.put("header", "add item");
            Request.put("Item_id", selectedItem.getItemId());
            Request.put("User_id", user.getUser_id());
            SA.ServerInit();
            SA.ServerWrite(Request);
            //JSONObject msg=SA.ServerRead();
            System.out.println(Request);
            SA.ServerKill();
        }

    }

    @FXML
    public void MyWishListBtn(ActionEvent event) throws IOException {
        u.switchToWishListScene(event, user);

    }

    @FXML
    public void handleSearchBtn(ActionEvent event) throws IOException {
        if (!searchlabel.getText().trim().isEmpty()) {
            Itemsdata.clear();
            JSONObject data = new JSONObject();
            ServerAccess SA = new ServerAccess();

            data.put("header", "search item");
            data.put("query", searchlabel.getText());

            System.out.println("Sending request: " + data);

            SA.ServerInit();
            SA.ServerWrite(data);
            JSONObject response = SA.ServerReadSearch();

            System.out.println("Full Server Response: " + response.toString());

            if (response.has("response")) {
                JSONObject responseData = response.getJSONObject("response");
                if (responseData.getString("header").equals("item search result")) {
                    JSONArray itemssArray = new JSONArray(responseData.getJSONArray("items"));
                    // wishesTable
                    for (int i = 0; i < itemssArray.length(); i++) {
                        JSONObject item = itemssArray.getJSONObject(i);
                        //System.out.println(item);
                        String Item_Name = item.getString("Name");
                        String Category = item.getString("Category");
                        int Item_Price = item.getInt("Price");
                        int item_id = item.getInt("Item_id");
                        info = new ItemsInfo(item_id, Item_Name, Item_Price, Category);
                        //  System.out.println(info.toString());
                        Itemsdata.add(info);

                    }

                    itemTable.setItems(Itemsdata);
                    itemTable.refresh();
                } else {
                    System.out.println("No items found!");
                    Itemsdata.clear();
                    itemTable.refresh();
                }
            } else {
                System.out.println("No 'response' field in server response!");
            }
        }
    }
    private ObservableList<ItemsInfo> Itemsdata = FXCollections.observableArrayList();

    public void initialize(URL location, ResourceBundle resources) {
        if (user != null) {
            updateUI();
        }
    }
    ItemsInfo info;

    public void setData(UserInfo user2) {
        this.user = user2;
        updateUI();  // Now update UI after user is set
    }

    private void updateUI() {
        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        Category.setCellValueFactory(new PropertyValueFactory<>("Category"));
        Price.setCellValueFactory(new PropertyValueFactory<>("Price"));

        int user_id = user.getUser_id();
        JSONObject request = new JSONObject();
        request.put("header", "items");
        request.put("user_id", user.getUser_id());
        SA.ServerInit();
        SA.ServerWrite(request);
        // System.out.println(request);

        JSONObject items = SA.ServerRead();
        //  System.out.println(items);

        fetchItems(items);

        itemTable.setItems(Itemsdata);
        SA.ServerKill();
    }

    @FXML
    private void handleItemsBtn(ActionEvent event) {
        u.switchToItemsScene(event, user);
    }
}
