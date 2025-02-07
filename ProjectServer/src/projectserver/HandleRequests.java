/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectserver;

import DAL.FriendInfo;
import DAL.FriendWishInfo;
import DBA.DBA;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            //System.out.println("no of friends "+friends.size());
            for (FriendInfo friend : friends) {
                friends_as_string.add(friend.toString()); 
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProjectServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return friends_as_string;
    }
    public boolean removeFriend(int User_id, int Friend_id)
    /* 
            returns true if the friendship was deleted
    */
    {
        boolean deleted=false;
        try {
            if (DBA.removeFriend(User_id, Friend_id)){
                deleted=true;
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
}
