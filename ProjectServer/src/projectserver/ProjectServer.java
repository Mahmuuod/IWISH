/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectserver;

import DAL.Contribution;
import DAL.NotificationInfo;
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
                                ArrayList<String> wishes = req.getFriendWishes(Friend_id);
                                respond = new JSONObject();
                                JSONArray wishesArray = new JSONArray();

                                for (String wish : wishes) {
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
                            JSONObject user = new JSONObject(utility.getUsrData(request.getInt("friend_id")));
                            String userName = user.getString("First_name") +" "+ user.getString("Last_name");
                            ArrayList<Integer> recieverIds = req.getContributors(wish_id);
                            int recieverId = request.getInt("friend_id");

                            try {
                                maxNotificationId = DBA.getNotificationMAXID();
                            } catch (SQLException ex) {
                                Logger.getLogger(HandleClients.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            NotificationInfo notificationToContributors = new NotificationInfo(maxNotificationId,
                                    "Great news! The wish for " + itemName + " by" + userName + " has been fully funded and is now completed. Thank you for your generous contribution!",
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
                            
                            String notificationBody = "Congratulations! Your wish for " + itemName + " has been fully funded thanks to your friends: ";
                            for (int i : recieverIds){
                                JSONObject friend = new JSONObject(utility.getUsrData(i));
                                String friendName = friend.getString("First_name") +" "+ user.getString("Last_name");
                                notificationBody+= friendName+ "\n";
                            }
                            NotificationInfo notificationToWishOwner = new NotificationInfo(maxNotificationId,
                                    notificationBody,
                                    "N", "Wish Completion");
                            if (req.insertNotification(notificationToContributors, recieverId)) {
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
