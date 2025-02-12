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
    
     public boolean checkEmail(String email)
    {
        boolean exists=false;
        try {
             exists=DBA.checkEmail(email);
        } catch (SQLException ex) {
            Logger.getLogger(HandleRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
        return exists;
    }
 public JSONArray searchUser(int userID,String query) {
    System.out.println("Searching for users: " + query); // Debugging
    
    JSONArray usersArray = new JSONArray();
    try {
        ArrayList<UserInfo> users = DBA.searchUsers(userID,query); 
        System.out.println("Users found in database: " + users.size()); // Debugging

        for (UserInfo user : users) {
            JSONObject userJson = new JSONObject();
            userJson.put("User_id", user.getUser_id());
            userJson.put("Username", user.getUsername());
            userJson.put("Email", user.getEmail());
            userJson.put("Birthdate", user.getBirthdate().toString());
            usersArray.put(userJson);
        }
    } catch (SQLException ex) {
        Logger.getLogger(HandleRequests.class.getName()).log(Level.SEVERE, null, ex);
    }
    return usersArray;
}
 
  public boolean removeFriend(int User_id, int Friend_id) /* 
            returns true if the friendship was deleted
     */ {
        boolean deleted = false;
        try {
            if (DBA.removeFriend(User_id, Friend_id)) {
                deleted = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(HandleRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
        return deleted;

    }

    public ArrayList<String> getFriendWishes(int Friend_id) {

        ArrayList<String> wishes_as_string = new ArrayList<>();
        try {
            ArrayList<FriendWishInfo> wishes = DBA.getFriendWishes(Friend_id);
            //System.out.println("no of friends "+friends.size());
            for (FriendWishInfo wish : wishes) {
                wishes_as_string.add(wish.toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProjectServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return wishes_as_string;
    }

    public boolean addContribution(Contribution contribution) /* 
            returns true if the contribution was added 
     */ {
        boolean added = false;
        try {
            if (DBA.contributeToWish(contribution) > 0) {
                added = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(HandleRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
        return added;

    }

    public boolean canContribute(int user_id, double countribution_amount) {
        boolean eligible = false;
        try {
            eligible = DBA.checkUserBalance(user_id, countribution_amount);
            {

            }
        } catch (SQLException ex) {
            Logger.getLogger(ProjectServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return eligible;
    }

    public ArrayList<Integer> getContributors(int Wish_id) {

        ArrayList<Integer> contributors = new ArrayList<>();
        try {
            contributors = DBA.getContributors(Wish_id);

        } catch (SQLException ex) {
            Logger.getLogger(ProjectServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contributors;
    }

    public String getItemName(int wish_id) {
        String itemName = "";
        try {
            itemName = DBA.getWishItem(wish_id);
            {

            }
        } catch (SQLException ex) {
            Logger.getLogger(ProjectServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itemName;
    }

    public boolean insertNotification(NotificationInfo notification, int recieverId) {
        boolean result = false;
        try {

            result = DBA.insertNotification(notification, recieverId);

        } catch (SQLException ex) {
            Logger.getLogger(ProjectServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public boolean insertNotification(NotificationInfo notification, ArrayList<Integer> receiverIds) {
        boolean result = false;
        try {

            result = DBA.insertNotification(notification, receiverIds);

        } catch (SQLException ex) {
            Logger.getLogger(ProjectServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public boolean checkNotifications(int User_id) /* 
            returns true if user has notifications 
     */ {
        boolean exists = false;
        try {
            if (DBA.getUserNotifications(User_id).size() > 0) {
                exists = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(HandleRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
        return exists;

    }
    
    public ArrayList<String> getUsrNotifications(int User_id) {

        ArrayList<String> notification_as_string = new ArrayList<>();
        try {
            ArrayList<NotificationInfo> notifications = DBA.getUserNotifications(User_id);
            //System.out.println("no of friends "+friends.size());
            for (NotificationInfo notification : notifications) {
                notification_as_string.add(notification.toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProjectServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notification_as_string;
    }
 


}
