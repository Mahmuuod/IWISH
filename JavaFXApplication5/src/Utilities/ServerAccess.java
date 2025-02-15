/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import ClientApp.ProjectClient;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.json.JSONObject;
import Utilities.UserInfo.*;
import java.net.SocketException;
import java.sql.Date;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.json.JSONArray;

/**
 *
 * @author sakr
 */
public class ServerAccess {

    private  PrintStream ps;
    private  DataInputStream dis;
    private  JSONObject JsonData;
    private  Thread t;
    private  Socket s;
    private UserInfo userData;
    public ServerAccess() {
    }

    public  void ServerInit() {
        try {
            s = new Socket("localhost", 5005);
            ps = new PrintStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());
            JsonData = new JSONObject();
        }catch(SocketException sa)
                    {
                JOptionPane.showMessageDialog(null, "Please try to re-open the app", "Server Error", JOptionPane.ERROR_MESSAGE);
                                System.exit(0);
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

                    }catch(SocketException sa)
                    {
                JOptionPane.showMessageDialog(null, "Please try to re-open the app", "Server Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);

                    }
                    catch (IOException ex) {
                        Logger.getLogger(ProjectClient.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(ProjectClient.class.getName()).log(Level.SEVERE, null, ex);
    }
        return jsonResponse;
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
                        Logger.getLogger(ProjectClient.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(ProjectClient.class.getName()).log(Level.SEVERE, null, ex);
    }
        return JsonData;
    }
    
    public  void ServerWrite(JSONObject msg)
    {
        if(ps!=null)
        ps.println(msg.toString());

    }
   public  void ServerWrite(JSONArray msg)
    {
                if(ps!=null)
        ps.println(msg.toString());
    }
    
    
     public void SetUserData(JSONObject data) {
         /* this function takes jsonobject and creates the user instance  */
         
        userData=new UserInfo(data.getInt("User_id"),data.getString("First_name"),data.getString("Last_name"),
         data.getString("Username"),String.valueOf(data.get("Password")),Date.valueOf(data.getString("Birthdate")),
        data.getString("Email"),String.valueOf(data.getInt("Phone")),String.valueOf(data.getInt("Bank_card")) ,data.getInt("User_balance"));     
        
     }
          public UserInfo GetUserData() {
        return    userData;
        
     }
}

