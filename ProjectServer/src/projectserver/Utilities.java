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
import org.json.*;

/**
 *
 * @author osama
 */


// handle try catch 
public class Utilities {

    public String checkGetUsrData(int id) {
        String user = null;
        try {
            if (DBA.userCheck(id)) {
                user = (DBA.getUserData(id).toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProjectServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    public String getUsrData(int id) {
        String user = null;
        try {
            user = (DBA.getUserData(id).toString());
        } catch (SQLException ex) {
            Logger.getLogger(ProjectServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    public String checkGetUsrData(String Username, String Password) {
        String user = null;
        try {
            if (DBA.userCheck(Username, Password)) {
                user = (DBA.getUserData(Username, Password).toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProjectServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    public String getUsrData(String Username, String Password) {
        String user = null;
        try {

            user = (DBA.getUserData(Username, Password).toString());

        } catch (SQLException ex) {
            Logger.getLogger(ProjectServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    
   /* public JSONObject getJSON(String massage)
    {
        JSONObject request = new JSONObject(massage);
        
        
        return request;
    }*/

}
