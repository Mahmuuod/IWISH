package ClientApp;

import Utilities.ItemDTO;
import Utilities.ItemsInfo;
import Utilities.ServerAccess;
import Utilities.UserInfo;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;


public class ItemController implements Initializable {
ServerAccess SA=new ServerAccess();
    @FXML
    private Button itemsbtn;
    @FXML
    private Button MyWishlistbtn;

    @FXML
    private Button AddItembtn;

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

    private void fetchItems() {
        
    }

    @FXML
    public void addItemToWishList() {
        ItemsInfo selectedItem = itemTable.getSelectionModel().getSelectedItem();
        if(selectedItem!=null)
        {
            JSONObject Request=new JSONObject();
            Request.put("header", "add item");
            Request.put("Item_id", selectedItem.getItemId());
            Request.put("User_id", UserInfo.getUser().getUser_id());    
                SA.ServerInit();
                SA.ServerWrite(Request);
                //JSONObject msg=SA.ServerRead();
                System.out.println(Request);
                SA.ServerKill();
        }
       
    }

    @FXML
    public void MyWishListBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("WishList.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    }

    private ObservableList<ItemsInfo> Itemsdata = FXCollections.observableArrayList();

    public void initialize(URL location, ResourceBundle resources) {
        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        Category.setCellValueFactory(new PropertyValueFactory<>("Category"));
        Price.setCellValueFactory(new PropertyValueFactory<>("Price"));
        
        int user_id=UserInfo.getUser().getUser_id();
        JSONObject request=new JSONObject();
        request.put("header", "items");
        request.put("user_id", UserInfo.getUser().getUser_id());
        SA.ServerInit();
        SA.ServerWrite(request);
       // System.out.println(request);

        JSONObject items=SA.ServerRead();
      //  System.out.println(items);

    
   
   if(items.getString("header").equals("items"))
   {
       JSONArray itemssArray = new JSONArray(items.getJSONArray("items"));
       // wishesTable
        for (int i = 0; i < itemssArray.length(); i++) {
            JSONObject item = itemssArray.getJSONObject(i);
            //System.out.println(item);
            String Item_Name = item.getString("Name");
            String Category = item.getString("Category");
            int Item_Price = item.getInt("Price");
            int item_id=item.getInt("Item_id");
             info=new ItemsInfo(item_id,Item_Name,Item_Price,Category);
          //  System.out.println(info.toString());
            Itemsdata.add(info);
           
        }

       
       
        itemTable.setItems(Itemsdata);
           SA.ServerKill(); 
   }
    }    
    ItemsInfo info;
}
