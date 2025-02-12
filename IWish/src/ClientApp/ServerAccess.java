/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientApp;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.json.JSONObject;
import ClientApp.UserInfo.*;
import java.sql.Date;

/**
 *
 * @author osama
 */
public class ServerAccess {
 private static ServerAccess instance;  // Singleton instance
    private  PrintStream ps;
    private  DataInputStream dis;
    private  JSONObject JsonData;
    private  Thread t;
    private  Socket s;
    private  UserInfo userData;
    // Private constructor to prevent direct instantiation
    public ServerAccess() {}

    // Public method to get the single instance of ServerAccess
    public static ServerAccess getInstance() {
        if (instance == null) {
            instance = new ServerAccess();
            instance.ServerInit();  // Ensure server connection is initialized
        }
        return instance;
    }

    public  void ServerInit() {
        try {
            s = new Socket("localhost", 5005);
            ps = new PrintStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());
            JsonData = new JSONObject();
        } catch (IOException ex) {
            Logger.getLogger(ServerAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public  void ServerKill() {
        try {
        if (s != null && !s.isClosed()) {
            s.close();
        }
        if (dis != null) {
            dis.close();
        }
        if (ps != null) {
            ps.close();
        }
        if (t != null) {
            t.interrupt(); 
        }
        if (JsonData != null) {
            JsonData.clear();
        }
        } catch (IOException ex) {
            Logger.getLogger(ServerAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public  JSONObject ServerRead() {
        final  CountDownLatch latch = new CountDownLatch(1);

        
            t = new Thread(new Runnable() {
                public void run() {

                    try {
                        String msg = dis.readLine();
                        System.out.println(msg);
                        JsonData = new JSONObject(msg);

                    } catch (IOException ex) {
                        Logger.getLogger(TestController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    finally {
                    latch.countDown(); 
                       }
                }
            });
            
            t.start();
    try {
        latch.await(); // Wait for the thread to finish
    } catch (InterruptedException ex) {
        Logger.getLogger(TestController.class.getName()).log(Level.SEVERE, null, ex);
    }
        return JsonData;
    }
  
    
        public JSONObject ServerReadSearch() {
            final  CountDownLatch latch = new CountDownLatch(1);
            JSONObject jsonResponse = new JSONObject();
        
            t = new Thread(new Runnable() {
                public void run() {

                    try {
                        String msg = dis.readLine();
                        System.out.println(msg);
                        JsonData = new JSONObject(msg);
                          if (msg != null) {
                jsonResponse.put("response", new JSONObject(msg));
            }

                    } catch (IOException ex) {
                        Logger.getLogger(TestController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    finally {
                    latch.countDown(); 
                       }
                }
            });
            
            t.start();
    try {
        latch.await(); // Wait for the thread to finish
    } catch (InterruptedException ex) {
        Logger.getLogger(TestController.class.getName()).log(Level.SEVERE, null, ex);
    }
        return jsonResponse;
    }
    
    
    public  void ServerWrite(JSONObject msg)
    {
        ps.println(msg.toString());
    }
     public void SetUserData(JSONObject data) {
        userData=UserInfo.getUser();
    userData.setUser(data.getInt("User_id"),data.getString("First_name"),data.getString("Last_name"),
         data.getString("Username"),data.getString("Password"),Date.valueOf(data.getString("Birthdate")),
        data.getString("Email"),data.getString("Phone"),String.valueOf(data.getInt("Bank_card")) ,data.getInt("User_balance"));       
        
     }
        public  UserInfo GetUserData() {
        return userData;
    }
          public static UserInfo getUser() {
    return new UserInfo(); // Ensure this returns a valid instance
}

}