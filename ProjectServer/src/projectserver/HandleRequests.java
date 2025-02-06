/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectserver;

import DAL.FriendInfo;
import DAL.UserInfo;
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
}
