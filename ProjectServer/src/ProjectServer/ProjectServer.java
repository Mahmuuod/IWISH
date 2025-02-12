/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectserver;

import DAL.UserInfo;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import org.json.*;
import DBA.DBA;
import java.util.ArrayList;
import projectserver.*;

/**
 *
 * @author osama
 */
class HandleClients extends Thread {

    HandleRequests req = new HandleRequests();
    Utilities utility = new Utilities();
    PrintStream ps;
    DataInputStream dis;
    static Vector<HandleClients> usrs = new Vector<HandleClients>();
    

    HandleClients(Socket s) {
        try {
            ps = new PrintStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());
            usrs.add(this);
            start();
        } catch (IOException ex) {
            Logger.getLogger(HandleClients.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                String msg = dis.readLine();
                System.out.println(msg);
                if (msg != null) {
                    JSONObject request = new JSONObject(msg);
                    String command = request.getString("header");
                    System.out.println("Received command: " + command);//Debugging

                    JSONObject respond;
                    switch (command) {
                        case "sign in":
                            String Username = request.getString("Username");
                            String Password = request.getString("Password");

                            if (req.signIn(Username, Password)) {
                                String user = utility.getUsrData(Username, Password);
                                respond = new JSONObject(user);
                                respond.put("header", "user exists");
                                System.out.println(respond.toString());
                                ps.println(respond.toString());
                                respond.clear();
                            } else {
                                respond = new JSONObject();
                                respond.put("header", "not exists");
                                System.out.println(respond.toString());
                                ps.println(respond.toString());
                                respond.clear();
                            }
                            break;
                            case "change password":
                            String email = request.getString("Email");
                            String newPassword = request.getString("Password");
                            respond = new JSONObject();
                            if (req.checkEmail(email)) {
                               if( DBA.changePassword(email, newPassword)){
                                
                                respond.put("header", "email changed");
                                System.out.println(respond.toString());
                                ps.println(respond.toString());
                                respond.clear();}
                            } else {
                                
                                respond.put("header", "not exists");
                                System.out.println(respond.toString());
                                ps.println(respond.toString());
                                respond.clear();
                            }
                            break;
                            
                            case "search user":
                                  String query = request.getString("query");
                                int userID = request.getInt("userID");
                              
                                JSONArray users = req.searchUser(userID,query);

                                respond = new JSONObject();
                                if (users.length() > 0) {
                                    respond.put("header", "search result");
                                    respond.put("users", users);
                                } else {
                                    respond.put("header", "no users found");
                                }

                                System.out.println("Sending response to client: " + respond.toString()); // Debugging

                                ps.println(respond.toString());
                                respond.clear();
                                break;
                                
                                
                            case "send friend request":
                                try {
                                    int requester_id = request.getInt("requester_id");
                                    int receiver_id = request.getInt("receiver_id");
                                    String requester_name = request.optString("requester_name"); 

                                    System.out.println("Received Friend Request from " + requester_id + " to " + receiver_id); // Debugging

                                    respond = new JSONObject();
                                    if (DBA.areFriends(requester_id, receiver_id)) {
                                        respond.put("header", "friend request failed");
                                        respond.put("status", "already friends");
                                    }
                                    else if (DBA.isFriendRequestExists(requester_id, receiver_id)) {
                                        respond.put("header", "friend request failed");
                                        respond.put("status", "duplicate");
                                    } else {
                                        boolean success = DBA.sendFriendRequest(requester_id, receiver_id, requester_name);
                                        respond.put("header", "friend request sent");
                                        respond.put("status", success ? "success" : "failed");
                                    }

                                    System.out.println("Sending response to client: " + respond.toString()); // Debugging
                                    ps.println(respond.toString());

                                } catch (SQLException ex) {
                                    Logger.getLogger(HandleClients.class.getName()).log(Level.SEVERE, null, ex);

                                    JSONObject errorResponse = new JSONObject();
                                    errorResponse.put("header", "error");
                                    errorResponse.put("status", "server error");

                                    ps.println(errorResponse.toString());
                                }
                                break;

                            case "accept friend request":
                                int requester_id = request.getInt("requester_id");
                                int receiver_id = request.getInt("receiver_id");
                                String receiver_name = request.optString("receiver_name");        
                                System.out.println(receiver_id + " accepted friend request sent from " + requester_id); // Debugging

                                respond = new JSONObject();
                                try {
                                    boolean success = DBA.acceptFriendRequest(requester_id, receiver_id,receiver_name);
                                    respond.put("header", "accept friend request");
                                    respond.put("status", success ? "success" : "failed");
                                } catch (SQLException ex) {
                                    Logger.getLogger(HandleClients.class.getName()).log(Level.SEVERE, null, ex);
                                    respond.put("status", "error");
                                }

                                System.out.println("Sending response to client: " + respond.toString()); // Debugging
                                ps.println(respond.toString());
                                respond.clear();
                                break;
                                
                                case "decline friend request":
                                    try {
                                         requester_id = request.getInt("requester_id");
                                         receiver_id = request.getInt("receiver_id");

                                        System.out.println(receiver_id + " declined friend request from " + requester_id); // Debugging

                                        boolean success = DBA.declineFriendRequest(requester_id, receiver_id);

                                        respond = new JSONObject();
                                        respond.put("header", "decline friend request");

                                        if (success) {
                                            respond.put("status", "success");
                                            System.out.println(" Friend request successfully declined.");
                                        } else {
                                            respond.put("status", "error");
                                            System.out.println(" ERROR: Failed to decline friend request.");
                                        }

                                                System.out.println("Sending response to client: " + respond.toString()); // Debugging

                                        ps.println(respond.toString());
                                        ps.flush();
                                    } catch (SQLException ex) {
                                        Logger.getLogger(HandleClients.class.getName()).log(Level.SEVERE, " Database Error in declining friend request", ex);
                                        respond = new JSONObject();
                                        respond.put("header", "decline friend request");
                                        respond.put("status", "error");
                                        respond.put("message", "Database error occurred while declining the friend request.");
                                        ps.println(respond.toString());
                                        ps.flush();
                                    } catch (Exception ex) {
                                        Logger.getLogger(HandleClients.class.getName()).log(Level.SEVERE, " Unexpected Error in declining friend request", ex);
                                        respond = new JSONObject();
                                        respond.put("header", "decline friend request");
                                        respond.put("status", "error");
                                        respond.put("message", "Unexpected error occurred while declining the friend request.");
                                        ps.println(respond.toString());
                                        ps.flush();
                                    }
                                    break;



                            
                        case "fetch pending requests":
                            userID = request.getInt("userID");

                    System.out.println("UserID received in fetch request: " + request.getInt("userID"));// Debugging

                           JSONObject response = new JSONObject();
                           response.put("header", "fetch pending requests");

                           JSONArray pendingUsers = DBA.fetchPendingRequests(userID);
                           response.put("users", pendingUsers);

                           System.out.println("Sending response to client: " + response.toString()); // Debugging
                           ps.println(response.toString());
                           response.clear();
                           break;



                       case "show friendlist":
                            /* sends response containing friend list as:
                            {
                              "header": "friendlist",
                              "friends": 
                                [{"friend_id" : id, "firstname": "fname", "lastname": "lname", "username": "username"},
                                {"friend_id" : id, "firstname": "fname", "lastname": "lname", "username": "username"}]
                            
                            OR
                            { "header": "no friends" }
                             */

                            int User_id = request.getInt("user_id");
                            System.out.println(User_id);
                            if (req.showFriendList(User_id)) {
                                ArrayList<String> friends = req.getUsrFriends(User_id);
                                respond = new JSONObject();
                                JSONArray friendsArray = new JSONArray();

                                for (String friend : friends) {
                                    JSONObject friend_as_json = new JSONObject(friend);
                                    JSONObject friendObject = new JSONObject();
                                    friendObject.put("friend_id", friend_as_json.getInt("Friend_id"));
                                    friendObject.put("firstname", friend_as_json.getString("First_name"));
                                    friendObject.put("lastname", friend_as_json.getString("Last_name"));
                                    friendObject.put("username", friend_as_json.getString("Username"));
                                    friendObject.put("birthdate", friend_as_json.getString("Birthdate"));
                                    friendObject.put("email", friend_as_json.getString("Email"));
                                    friendsArray.put(friendObject);
                                }

                                respond.put("header", "friendlist");
                                respond.put("friends", friendsArray);

                            } else {
                                respond = new JSONObject();
                                respond.put("header", "no friends");
                            }

                            System.out.println(respond.toString());
                            ps.println(respond.toString());
                            respond.clear();
                            break;
                    }

                } else {
                    usrs.remove(this);
                    stop();
                    dis.close();
                }
            } catch (SocketException sc) {
                try {
                    usrs.remove(this);
                    stop();
                    dis.close();
                } catch (IOException ex) {
                    Logger.getLogger(HandleClients.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(HandleClients.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(HandleClients.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
public class ProjectServer {

    ProjectServer() {
        try {
            ServerSocket ss = new ServerSocket(5005);
            while (true) {
                Socket s = ss.accept();
                new HandleClients(s);

            }
        } catch (IOException ex) {
            Logger.getLogger(ProjectServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        new ProjectServer();
        /*Utilities u=new Utilities();
        System.out.println(u.checkGetUsrData(888));*/
    }

}