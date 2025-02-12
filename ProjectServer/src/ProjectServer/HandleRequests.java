/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectserver;

import DAL.UserInfo;
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
 * @author sakr
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

    ArrayList<String> getUsrFriends(int User_id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

    
}