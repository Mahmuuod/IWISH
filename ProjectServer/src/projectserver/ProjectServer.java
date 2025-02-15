/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectserver;

import DAL.*;
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
                    int User_id;
                    JSONObject request = new JSONObject(msg);
                    String command = request.getString("header");
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
                            } else {
                                respond = new JSONObject();
                                respond.put("header", "not exists");
                                System.out.println(respond.toString());
                                ps.println(respond.toString());

                            }
                            respond.clear();
                            break;
                        case "sign up":
                            UserInfo user = new UserInfo(req.getMaxId(), request.getString("First_name"), request.getString("Last_name"),
                                    request.getString("Username"), String.valueOf(request.get("Password")), Date.valueOf(request.getString("Birthdate")),
                                    request.getString("Email"), String.valueOf(request.get("Phone")), String.valueOf(request.get("Bank_card")), request.getInt("User_balance"));
                            boolean state = req.signUp(user);
                            respond = new JSONObject();
                            if (state) {
                                respond.put("header", "added");
                            } else {
                                respond.put("header", "duplicated");
                            }

                            ps.println(respond.toString());
                            respond.clear();
                            break;
                        case "add balance":
                            int balance = request.getInt("value");
                            int id = request.getInt("user id");

                            int result = req.updateBalance(id, balance);
                            respond = new JSONObject();
                            respond.put("header", "added");
                            respond.put("value", result);
                            ps.println(respond.toString());
                            respond.clear();
                            break;
                        /*    public WishInfo(int wish_id, int User_id, int ITEM_ID, Timestamp Wish_Time, String Status, Date WISH_DATE) {
        this.wish_id = wish_id;
        this.User_id = User_id;
        this.ITEM_ID = ITEM_ID;
        this.Wish_Time = Wish_Time;
        this.Status = Status;
        this.WISH_DATE = WISH_DATE;
    }*/
                        case "wishlist":
                            User_id = request.getInt("user_id");
                            JSONArray wishes = req.wishlisht(User_id);
                            System.out.println(wishes);
                            respond = new JSONObject();
                            respond.put("header", "wishlist");
                            respond.put("wishes", wishes);
                            System.out.println(respond);
                            ps.println(respond.toString());
                            respond.clear();
                            break;
                        case "items":
                            JSONArray items = req.ItemsList();
                            System.out.println(items);
                            respond = new JSONObject();
                            respond.put("header", "items");
                            respond.put("items", items);
                            System.out.println(respond);
                            ps.println(respond.toString());
                            respond.clear();
                            break;
                        case "add item":
                            req.insertWish(request);
                            break;
                        case "deletewish":
                            req.deleteWish(request);
                            ps.println("{header:deleted}");
                            break;
                        case "change password":
                            String email = request.getString("Email");
                            String newPassword = request.getString("Password");
                            respond = new JSONObject();
                            if (req.checkEmail(email)) {
                                if (DBA.changePassword(email, newPassword)) {

                                    respond.put("header", "email changed");
                                    System.out.println(respond.toString());
                                    ps.println(respond.toString());
                                    respond.clear();
                                }
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

                            JSONArray users = req.searchUser(userID, query);

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
                                } else if (DBA.isFriendRequestExists(requester_id, receiver_id)) {
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
                                boolean success = DBA.acceptFriendRequest(requester_id, receiver_id, receiver_name);
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

                             User_id = request.getInt("user_id");
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
                        case "remove friend":
                            /* sends response containing:
                            {
                              "header": "friend removed"
                            }
                            OR
                            { "header": "friend does not exist" }
                             */

                            int User_id1 = request.getInt("user_id");
                            int Friend_id1 = request.getInt("friend_id");//user_id as the user_id is aleady taken in "show friend list"

                            if (req.removeFriend(User_id1, Friend_id1)) {
                                respond = new JSONObject();
                                respond.put("header", "friend removed");

                            } else {
                                respond = new JSONObject();
                                respond.put("header", "friend does not exist");
                            }

                            System.out.println(respond.toString());
                            ps.println(respond.toString());
                            respond.clear();
                            break;
                        case "show friend wishlist":
                            /* sends response containing friend list as:
                            {
                              "header": "friend wishlist",
                              "wishes": 
                                [{"friend_id" : id, "wish_id": "wid", "wish_date": "wdate", "name": "name", "price": "100", "collected":"50", "status":"New"},
                                {{"friend_id" : id, "wish_id": "wid", "wish_date": "wdate", "name": "name", "price": "200", "collected":"50", "status":"New"}]
                            
                            OR
                            { "header": "no wishes" }
                             */

                            int Friend_id = request.getInt("friend_id");
                            if (req.showFriendList(Friend_id)) {
                                ArrayList<String> wishes2 = req.getFriendWishes(Friend_id);
                                respond = new JSONObject();
                                JSONArray wishesArray = new JSONArray();

                                for (String wish : wishes2) {
                                    JSONObject wish_as_json = new JSONObject(wish);
                                    JSONObject wishObject = new JSONObject();
                                    wishObject.put("friend_id", wish_as_json.getInt("Friend_id"));
                                    wishObject.put("wish_id", wish_as_json.getInt("Wish_id"));
                                    wishObject.put("wish_date", wish_as_json.getString("Wish_date"));
                                    wishObject.put("name", wish_as_json.getString("Name"));
                                    wishObject.put("price", wish_as_json.getDouble("Price"));
                                    wishObject.put("collected", wish_as_json.getDouble("Collected"));
                                    wishObject.put("status", wish_as_json.getString("Status"));
                                    wishesArray.put(wishObject);
                                }

                                respond.put("header", "friend wishlist");
                                respond.put("wishlist", wishesArray);

                            } else {
                                respond = new JSONObject();
                                respond.put("header", "no wishes");
                            }

                            System.out.println(respond.toString());
                            ps.println(respond.toString());
                            respond.clear();
                            break;
                        case "contribute":

                            /* sends response containing:
                            {
                            "header": "contribution added"
                            }
                            OR
                            { "header": "contribution duplicated" }
                             */
                            respond = new JSONObject();
                            if (req.canContribute(request.getInt("contributor_id"), request.getDouble("contribution_amount"))) {
                                int maxContributionId = 0;
                                try {
                                    maxContributionId = DBA.getContributionMAXID();
                                } catch (SQLException ex) {
                                    Logger.getLogger(HandleClients.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                Contribution contribution = new Contribution(maxContributionId, request.getInt("wish_id"),
                                        request.getDouble("contribution_amount"), request.getInt("contributor_id"));
                                if (req.addContribution(contribution)) {
                                    respond.put("header", "contribution added");

                                } else {
                                    respond.put("header", "contribution duplicated");
                                }
                            } else {
                                respond.put("header", "not enough balance");
                            }
                            System.out.println(respond.toString());
                            ps.println(respond.toString());
                            respond.clear();
                            break;
                        case "wish completed":

                            /* sends response containing:
                            {
                            "header": "notification sent"
                            }
                            OR
                            { "header": "notification duplicated" }
                             */
                            respond = new JSONObject();
                            // send to contributors
                            int maxNotificationId = 0;
                            int wish_id = request.getInt("wish_id");
                            String itemName = req.getItemName(wish_id);
                            JSONObject user2 = new JSONObject(utility.getUsrData(request.getInt("friend_id")));
                            String userName = user2.getString("First_name") +" "+ user2.getString("Last_name");
                            ArrayList<Integer> recieverIds = req.getContributors(wish_id);
                            int recieverId = request.getInt("friend_id");

                            try {
                                maxNotificationId = DBA.getNotificationMAXID();
                            } catch (SQLException ex) {
                                Logger.getLogger(HandleClients.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            NotificationInfo notificationToContributors = new NotificationInfo(maxNotificationId,
                                    "Great news! The wish for " + itemName + " by " + userName + " has been fully funded and is now completed. Thank you for your generous contribution!",
                                    "N", "Wish Completion");
                            if (req.insertNotification(notificationToContributors, recieverIds)) {
                                respond.put("header", "notification sent to contributors");

                            } else {
                                respond.put("header", "notification to contributors duplicated");
                            }
                            
                            System.out.println(respond.toString());
                            ps.println(respond.toString());
                            respond.clear();
                            
                            // send to wish owner
                            
                            maxNotificationId++;
                            
                            String notificationBody = "Congratulations! Your wish for " + itemName + " has been fully funded thanks to ";
                            for (int i : recieverIds){
                                JSONObject friend = new JSONObject(utility.getUsrData(i));
                                String friendName = friend.getString("First_name") +" "+ friend.getString("Last_name");
                                notificationBody+= friendName+ " and ";
                            }
                            notificationBody=notificationBody.substring(1, notificationBody.length()-4);
                            NotificationInfo notificationToWishOwner = new NotificationInfo(maxNotificationId,
                                    notificationBody,
                                    "N", "Wish Completion");
                            if (req.insertNotification(notificationToWishOwner, recieverId)) {
                                respond.put("header", "notification sent to wish owner");

                            } else {
                                respond.put("header", "notification to wish owner duplicated");
                            }
                            System.out.println(respond.toString());
                            ps.println(respond.toString());
                            respond.clear();
                            break;
                        case "show notifications":
                            /* sends response containing friend list as:
                            {
                              "header": "notification list",
                              "notifications": 
                                [{"notification_id" : id, "context": "context", "isread": "isread", "notification_date": "notification_date"},
                                {"notification_id" : id, "context": "context", "isread": "isread", "notification_date": "notification_date"}]
                            
                            OR
                            { "header": "no notifications" }
                             */

                            int User_id2 = request.getInt("user_id");
                            if (req.checkNotifications(User_id2)) {
                                ArrayList<String> notifications = req.getUsrNotifications(User_id2);
                                
                                respond = new JSONObject();
                                JSONArray notificationArray = new JSONArray();

                                for (String notification : notifications) {
                                        System.out.println(notification);
                                    JSONObject notification_as_json = new JSONObject(notification);
                                    JSONObject notificationObject = new JSONObject();
                                    notificationObject.put("notification_id", notification_as_json.getInt("Notification_id"));
                                    notificationObject.put("context", notification_as_json.getString("Context"));
                                    notificationObject.put("isread", notification_as_json.getString("IsRead"));
                                    notificationObject.put("notification_date", notification_as_json.getString("Notification_date"));
                                    notificationArray.put(notificationObject);
                                }

                                respond.put("header", "notification list");
                                respond.put("notifications", notificationArray);

                            } else {
                                respond = new JSONObject();
                                respond.put("header", "no notifications");
                            }

                            System.out.println(respond.toString());
                            ps.println(respond.toString());
                            respond.clear();
                            break;
                            
                         case "search friend":
                            String friendquery = request.getString("query");
                            int user_id = request.getInt("userID");

                            ArrayList<String> friends = req.searchFriends(user_id, friendquery);
                            respond = new JSONObject();
                            JSONArray friendsArray = new JSONArray();
                            if (friends.size() > 0) {
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

                                respond.put("header", "friend search result");
                                respond.put("friends", friendsArray);
                            } else {
                                respond = new JSONObject();
                                respond.put("header", "no friends found");
                            }

                            System.out.println("Sending response to client: " + respond.toString()); // Debugging

                            ps.println(respond.toString());
                            respond.clear();
                            break;
                                                    case "search item":
                            String query3 = request.getString("query");

                            JSONArray items2 = req.searchItems(query3);

                            respond = new JSONObject();
                            if (items2.length() > 0) {
                                respond.put("header", "item search result");
                                respond.put("items", items2);
                            } else {
                                respond.put("header", "no items found");
                            }

                            System.out.println("Sending response to client: " + respond.toString()); // Debugging

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
