/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectserver;


import DAL.*;
import DBA.DBA;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import projectserver.Utilities;

/**
 *
 * @author osama
 */
public class HandleRequests {
    
    public boolean signIn(String Username, String Password)
    {
        boolean exists=false;
        try {
             exists=DBA.userCheck(Username, Password);
        } catch (SQLException ex) {
            Logger.getLogger(HandleRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
        return exists;
    }
    public boolean signUp(UserInfo user)
    {
boolean state=true;
        try {
            DBA.newUser(user);
        } catch (SQLException ex) {
            state=false;
        }
        return state;
    }
    
    public boolean showFriendList(int User_id)
    /* 
            returns true if user has friends 
    */
    {
        boolean exists=false;
        try {
            if (DBA.getUserFriends(User_id).size()> 0){
                exists=true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(HandleRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
        return exists;

    }
    
    public ArrayList<String> getUsrFriends(int User_id) { 

        ArrayList<String> friends_as_string = new ArrayList<>();
        try {
            ArrayList<FriendInfo> friends = DBA.getUserFriends(User_id);
            for (FriendInfo friend : friends) {
                friends_as_string.add(friend.toString()); 
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProjectServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return friends_as_string;
    }
    public int getMaxId() 
    {
        int max_id=-1;
        try {
            max_id=DBA.getUsersMAXID();
        } catch (SQLException ex) {
            Logger.getLogger(HandleRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
        return max_id;
    }
    public int updateBalance(int id,int balance)
    {
        int result=-1;
        try {
             result = DBA.updateBalance(balance,id);
            System.out.println(result);
            if(result < 0)
            {
                return -2;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(HandleRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public JSONArray wishlisht(int user_id)
    {
        JSONArray wishesArray = new JSONArray();
        try {
            ArrayList<WishInfo> wishes=DBA.getWishData(user_id);
            
                for (WishInfo wish : wishes) {
                    JSONObject wish_as_json = new JSONObject(wish.toString());
                    wishesArray.put(wish_as_json);
                                }
                
        } catch (SQLException ex) {
            Logger.getLogger(HandleRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
        return wishesArray;
    }

    public JSONArray ItemsList()
    {
        JSONArray itemsArray = new JSONArray();
        try {
            ArrayList<ItemsInfo> items=DBA.getItemsData();
            
                for (ItemsInfo item : items) {
                    JSONObject item_as_json = new JSONObject(item.toString());
                    itemsArray.put(item_as_json);
                                }
                
        } catch (SQLException ex) {
            Logger.getLogger(HandleRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itemsArray;
    }
    public void insertWish(JSONObject wish)
    {
        try {
            int wish_id=DBA.getWishMAXID();
            WishInfo wishadd=new WishInfo(wish_id,wish.getInt("User_id"),wish.getInt("Item_id"));
            DBA.setWish(wishadd);
        } catch (SQLException ex) {
            Logger.getLogger(HandleRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    public void deleteWish(JSONObject wish)
    {
        try {
            int wish_id=wish.getInt("wishid");
            int user_id=wish.getInt("userid");
            DBA.DeleteWish(wish_id,user_id);
        } catch (SQLException ex) {
            Logger.getLogger(HandleRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    //{Item_id:5, Name:Backpack, Price:100.0, Category:Fashion}
}
