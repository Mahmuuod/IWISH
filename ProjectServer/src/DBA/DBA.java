/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBA;

import DAL.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import oracle.jdbc.driver.OracleDriver;
import javafx.scene.control.Alert;
import DAL.UserInfo;
import DAL.WishInfo;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author osama
 */
public class DBA {

    static String connectionString = "jdbc:oracle:thin:@localhost:1521:XE";
    static ArrayList<UserInfo> Users = new ArrayList<UserInfo>();

    public static ArrayList<UserInfo> getAllUsers() throws SQLException {
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("select * from USERS"); //edit
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            UserInfo temp = new UserInfo(rs.getInt("User_id"), rs.getString("First_name"),
                    rs.getString("Last_name"), rs.getString("Username"), rs.getString("Password"), rs.getDate("Birthdate"), rs.getString("Email"),
                    rs.getString("Phone"), rs.getString("Bank_card"), rs.getInt("User_balance"));

            Users.add(temp);
        }
        statement.close();
        con.close();
        return Users;
    }
  public static ArrayList<UserInfo> searchUsers(int userID,String query) throws SQLException {
    System.out.println("Executing search for: " + query); // Debugging
    
    ArrayList<UserInfo> users = new ArrayList<>();
    Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
   
    String sql = "SELECT * FROM Users WHERE (Username LIKE ? OR Email LIKE ? )and user_id != ? ";
    PreparedStatement statement = con.prepareStatement(sql);
    statement.setString(1,  query + "%");
    statement.setString(2,  query + "%");
    statement.setInt(3,userID);

    System.out.println("Executing SQL: " + sql); // Debugging

    ResultSet rs = statement.executeQuery();

    int count = 0; 
    while (rs.next()) {
        count++;
        System.out.println("Found User: " + rs.getString("Username")); // Debugging

        UserInfo user = new UserInfo(
            rs.getInt("User_id"),
            rs.getString("First_name"),
            rs.getString("Last_name"),
            rs.getString("Username"),
            "", 
            rs.getDate("Birthdate"),
            rs.getString("Email"),
            rs.getString("Phone"),
            rs.getString("Bank_card"),
            rs.getInt("User_balance")
        );
        users.add(user);
    }
    

    System.out.println("Total users found: " + count); // Debugging

    statement.close();
    con.close();
    return users;
}
  public static JSONArray fetchPendingRequests(int userID) throws SQLException {
    Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");

    String query = "SELECT U.User_id, U.Username, U.Birthdate, U.Email " +
                   "FROM Users U " +
                   "JOIN FriendRequest F ON U.User_id = F.Requester_id " +
                   "WHERE F.Reciever_id = ? AND F.Status = 'Pending'";

    PreparedStatement statement = con.prepareStatement(query);
    statement.setInt(1, userID);

    System.out.println("Executing Query: " + statement.toString()); // Debugging

    ResultSet rs = statement.executeQuery();
    JSONArray pendingUsers = new JSONArray();

    while (rs.next()) {
        JSONObject user = new JSONObject();
        user.put("User_id", rs.getInt("User_id"));
        user.put("Username", rs.getString("Username"));
        user.put("Birthdate", rs.getString("Birthdate"));
        user.put("Email", rs.getString("Email"));

        System.out.println("Found Pending Request: " + user.toString()); // Debugging

        pendingUsers.put(user);
    }

    rs.close();
    statement.close();
    con.close();

    System.out.println("Final Pending Users JSON: " + pendingUsers.toString()); // Debugging

    return pendingUsers;
}


     public static boolean sendFriendRequest(int requester_id, int receiver_id, String requester_name) throws SQLException {
    boolean isDone = false;
    Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");

    try {
        con.setAutoCommit(false);


        PreparedStatement statement = con.prepareStatement(
            "INSERT INTO FriendRequest (Request_id, Requester_id, Reciever_id, Status) VALUES (FriendRequest_seq.NEXTVAL, ?, ?, 'Pending')"
        );
        statement.setInt(1, requester_id);
        statement.setInt(2, receiver_id);
        int us = statement.executeUpdate();

        if (us <= 0) {
            con.rollback();
            System.out.println(" ERROR: Failed to insert friend request.");
            return false;
        }

        System.out.println(" Friend request inserted successfully!");

        PreparedStatement statement1 = con.prepareStatement(
            "INSERT INTO notification (Notification_ID, Context, is_read, Type) VALUES (FriendRequestnotification_seq.NEXTVAL, ?, 'N', 'friend request')"
        );
        String notificationMessage = requester_name + " has sent you a friend request";
        statement1.setString(1, notificationMessage);
        int us1 = statement1.executeUpdate();

        if (us1 <= 0) {
            con.rollback();
            System.out.println(" ERROR: Failed to insert notification.");
            return false;
        }

        System.out.println(" Friend request notification inserted successfully!");

        PreparedStatement statement2 = con.prepareStatement(
            "INSERT INTO user_notification (reciever_ID, Notification_ID, Notification_Date, Notification_Time) VALUES (?, FriendRequestnotification_seq.CURRVAL, sysdate, sysdate)"
        );
        statement2.setInt(1, receiver_id);
        int us2 = statement2.executeUpdate();

        if (us2 <= 0) {
            con.rollback();
            System.out.println(" ERROR: Failed to insert user notification.");
            return false;
        }

        System.out.println(" Friend request user notification inserted successfully!");

        con.commit(); 
        isDone = true;

    } catch (SQLException e) {
        con.rollback();
        System.out.println(" ERROR: SQL Exception occurred: " + e.getMessage());
        throw e;
    } finally {
        con.close();
    }

    return isDone;
}


    public static boolean acceptFriendRequest(int requester_id, int receiver_id, String receiver_name) throws SQLException {
    boolean isDone = false;
    Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");

    try {
        con.setAutoCommit(false); 


        PreparedStatement statement = con.prepareStatement(
            "UPDATE FriendRequest SET Status = 'Accepted' WHERE (Requester_id = ? AND Reciever_id = ?) OR (Requester_id = ? AND Reciever_id = ?)"
        );
        statement.setInt(1, requester_id);
        statement.setInt(2, receiver_id);
        statement.setInt(3, receiver_id);
        statement.setInt(4, requester_id);
        int us = statement.executeUpdate();

        if (us <= 0) {
            con.rollback();
            System.out.println(" ERROR: Failed to update FriendRequest table.");
            return false;
        }

        System.out.println(" Friend request status updated successfully!");
        PreparedStatement statement1 = con.prepareStatement(
            "INSERT INTO FriendList (User_ID, Friend_id, FRIENDS_SINCE) VALUES (?, ?, sysdate)"
        );
        statement1.setInt(1, requester_id);
        statement1.setInt(2, receiver_id);
        int us1 = statement1.executeUpdate();

        if (us1 <= 0) {
            con.rollback();
            System.out.println(" ERROR: Failed to insert first row in FriendList.");
            return false;
        }

        System.out.println("First row inserted into FriendList successfully!");

        PreparedStatement statement2 = con.prepareStatement(
            "INSERT INTO FriendList (User_ID, Friend_id, FRIENDS_SINCE) VALUES (?, ?, sysdate)"
        );
        statement2.setInt(1, receiver_id);
        statement2.setInt(2, requester_id);
        int us2 = statement2.executeUpdate();

        if (us2 <= 0) {
            con.rollback();
            System.out.println(" ERROR: Failed to insert second row in FriendList.");
            return false;
        }

        System.out.println("Second row inserted into FriendList successfully!");

        PreparedStatement statement3 = con.prepareStatement(
            "INSERT INTO notification (Notification_ID, Context, is_read, Type) VALUES (FriendRequestnotification_seq.NEXTVAL, ?, 'N', 'accepted friend request')"
        );
        String notificationMessage = receiver_name + " has accepted your friend request";
        statement3.setString(1, notificationMessage);
        int us3 = statement3.executeUpdate();

        if (us3 <= 0) {
            con.rollback();
            System.out.println(" ERROR: Failed to insert accepted friend request notification.");
            return false;
        }

        System.out.println("Accepted friend request notification inserted successfully!");

        PreparedStatement statement4 = con.prepareStatement(
            "INSERT INTO user_notification (reciever_ID, Notification_ID, Notification_Date, Notification_Time) VALUES (?, FriendRequestnotification_seq.CURRVAL, sysdate,  sysdate)"
        );
        statement4.setInt(1, requester_id);
        int us4 = statement4.executeUpdate();

        if (us4 <= 0) {
            con.rollback();
            System.out.println(" ERROR: Failed to insert accepted user notification.");
            return false;
        }

        System.out.println(" Accepted friend request user notification inserted successfully!");

        con.commit(); 
        isDone = true;
        System.out.println(" acceptFriendRequest() executed completely!");

    } catch (SQLException e) {
        con.rollback();
        System.out.println(" ERROR: SQL Exception occurred: " + e.getMessage());
        throw e;
    } finally {
        con.close();
    }

    return isDone;
}

   public static boolean declineFriendRequest(int requester_id, int receiver_id) throws SQLException {
    boolean isDone = false;
    Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");

    try {
        con.setAutoCommit(false); 

        
        PreparedStatement statement = con.prepareStatement(
            "delete from  FriendRequest  WHERE (Requester_id=? AND Reciever_id = ?) OR (Requester_id=? AND Reciever_id = ?)"
        );
        statement.setInt(1, requester_id);
        statement.setInt(2, receiver_id);
        statement.setInt(3, receiver_id);
        statement.setInt(4, requester_id);

        int us = statement.executeUpdate();
        if (us > 0) {
            isDone = true;
            System.out.println(" Friend request status updated successfully.");
            con.commit(); 
        } else {
            con.rollback();
            System.out.println("ERROR: Failed to update FriendRequest table.");
        }

        statement.close();
    } catch (SQLException e) {
        con.rollback();
        System.out.println(" SQL Error in declineFriendRequest: " + e.getMessage());
        throw e;
    } finally {
        con.close();
    }

    return isDone;
}

public static boolean areFriends(int requester_id, int receiver_id) throws SQLException {
         boolean exists;
        try (Connection con = DriverManager.getConnection(connectionString, "iwish", "1234")) {
            String query = "SELECT COUNT(*) FROM FriendList WHERE "
                    + "(user_id = ? AND Friend_id = ? ) OR (user_id = ? AND Friend_id = ?)";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, requester_id);
            statement.setInt(2, receiver_id);
            statement.setInt(3, receiver_id);
            statement.setInt(4, requester_id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            exists = rs.getInt(1) > 0;
            rs.close();
            statement.close();
        }

    return exists;
}

     public static boolean isFriendRequestExists(int requester_id, int receiver_id) throws SQLException {
         boolean exists;
        try (Connection con = DriverManager.getConnection(connectionString, "iwish", "1234")) {
            String query = "SELECT COUNT(*) FROM FriendRequest WHERE "
                    + "(Requester_id = ? AND Reciever_id = ? and status = 'Pending' ) OR (Requester_id = ? AND Reciever_id = ? and status = 'Pending')";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, requester_id);
            statement.setInt(2, receiver_id);
            statement.setInt(3, receiver_id);
            statement.setInt(4, requester_id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            exists = rs.getInt(1) > 0;
            rs.close();
            statement.close();
        }

    return exists;
}
    public static boolean userCheck(String Username, String Password) throws SQLException {
        boolean exists = false;
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("select User_id from USERS where Username=? and Password=?"); //edit
        statement.setString(1, Username);
        statement.setString(2, Password);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            exists = true;
        }
        statement.close();
        con.close();
        return exists;
    }

    public static boolean userCheck(int id) throws SQLException {
        boolean exists = false;
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("select User_id from USERS where User_id=?"); //edit
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        exists = rs.next();
        statement.close();
        con.close();
        return exists;
    }

    public static int getUserID(String Username, String Password) throws SQLException {
        int User_id = -1;
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("select User_id from USERS where User_id=? and Password=?"); //edit
        statement.setString(1, Username);
        statement.setString(2, Password);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            User_id = rs.getInt("User_id");
        }

        statement.close();
        con.close();
        return User_id;
    }

    public static int getUsersMAXID() throws SQLException {
        int User_id = -1;
        try {
            /* this works as a sequence in database */

            Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
            PreparedStatement statement = con.prepareStatement("select max(User_id)+1 as User_id from USERS"); //edit
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                User_id = rs.getInt("User_id");
            }

            statement.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return User_id;
    }

    public static int getWishMAXID() throws SQLException {
        int Wish_id = -1;
        try {
            /* this works as a sequence in database */

            Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
            PreparedStatement statement = con.prepareStatement("select max(Wish_id)+1 as Wish_id from Wish"); //edit
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Wish_id = rs.getInt("Wish_id");
            }

            statement.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Wish_id;
    }

    public static int getNotificationMAXID() throws SQLException {
        int Notification_id = -1;
        try {
            /* this works as a sequence in database */

            Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
            PreparedStatement statement = con.prepareStatement("select max(Notification_id)+1 as Notification_id from NOTIFICATION"); //edit
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Notification_id = rs.getInt("Notification_id");
            }

            statement.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Notification_id;
    }
// used in checkGetUserData

    public static UserInfo getUserData(int User_id) throws SQLException {
        UserInfo user = null;
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("select * from USERS where User_id=?"); //edit
        statement.setInt(1, User_id);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            user = new UserInfo(rs.getInt("User_id"), rs.getString("First_name"),
                    rs.getString("Last_name"), rs.getString("Username"), rs.getString("Password"), rs.getDate("Birthdate"), rs.getString("Email"),
                    rs.getString("Phone"), rs.getString("Bank_card"), rs.getInt("User_balance"));
        }

        statement.close();
        con.close();
        return user;
    }

    public static ArrayList<WishInfo> getWishData(int User_id) throws SQLException {
        ArrayList<WishInfo> Wishes = new ArrayList<WishInfo>();
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("select  WISH.Wish_id,WISH.USER_ID,ITEM.NAME,ITEM.PRICE,WISH.WISH_DATE,collected from WISH left join ITEM on ITEM.ITEM_ID=WISH.ITEM_ID where \n"
                + "USER_ID=? "); //edit

        statement.setInt(1, User_id);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            WishInfo wish = new WishInfo(rs.getInt("USER_ID"), rs.getString("NAME"), rs.getInt("PRICE"), rs.getDate("WISH_DATE"), rs.getInt("collected"), rs.getInt("Wish_id"));
            Wishes.add(wish);
        }

        statement.close();
        con.close();
        return Wishes;
    }

    public static UserInfo getUserData(String Username, String Password) throws SQLException {
        UserInfo user = null;
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("select * from USERS where Username=? and Password=?"); //edit
        statement.setString(1, Username);
        statement.setString(2, Password);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            user = new UserInfo(rs.getInt("User_id"), rs.getString("First_name"),
                    rs.getString("Last_name"), rs.getString("Username"), rs.getString("Password"), rs.getDate("Birthdate"), rs.getString("Email"),
                    rs.getString("Phone"), rs.getString("Bank_card"), rs.getInt("User_balance"));
        }

        statement.close();
        con.close();
        return user;
    }

    public static ArrayList<ItemsInfo> getItemsData() throws SQLException {
        ArrayList<ItemsInfo> Items = new ArrayList<ItemsInfo>();
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("select ITEM_ID,NAME,PRICE,CATEGORY from Item"); //edit

        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Items.add(new ItemsInfo(rs.getInt("Item_id"), rs.getString("NAME"),
                    rs.getInt("PRICE"), rs.getString("CATEGORY")));
        }

        statement.close();
        con.close();
        return Items;
    }

    public static void setWish(WishInfo wish) throws SQLException {

        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("insert into wish(Wish_id,User_id,Item_id,Wish_Date,Wish_Time,Status) values(?,?,?,?,?,?)"); //edit
        statement.setInt(1, wish.getWish_id());
        statement.setInt(2, wish.getUser_id());
        statement.setInt(3, wish.getITEM_ID());
        statement.setDate(4, Date.valueOf(LocalDate.now()));
        statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        statement.setString(6, "New");
        int us = statement.executeUpdate();

        statement.close();
        con.close();
    }

    public static ArrayList<FriendInfo> getUserFriends(int User_id) throws SQLException {
        /* 
            this method takes user's id and returns their friends as an arraylist of FriendInfo objects 
         */
        ArrayList<FriendInfo> friends = new ArrayList<FriendInfo>();
        FriendInfo friend = null;
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("select f.FRIEND_ID as FRIEND_ID, u.FIRST_NAME as FIRST_NAME, u.LAST_NAME as LAST_NAME, u.USERNAME as USERNAME, u.BIRTHDATE as BIRTHDATE, u.EMAIL as EMAIL from FRIENDLIST f JOIN USERS u ON f.friend_id = u.user_id where f.user_id = ?"); //edit
        statement.setInt(1, User_id);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            friend = new FriendInfo(rs.getInt("FRIEND_ID"), rs.getString("FIRST_NAME"),
                    rs.getString("LAST_NAME"), rs.getString("USERNAME"), rs.getDate("BIRTHDATE"), rs.getString("EMAIL"));
            friends.add(friend);
        }

        statement.close();
        con.close();
        return friends;
    }

    public static int newUser(UserInfo user) throws SQLException {
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("insert into USERS values(?,?,?,?,?,?,?,?,?,?)"); //edit
        statement.setInt(1, user.getUser_id());
        statement.setString(2, user.getFirst_name());
        statement.setString(3, user.getLast_name());
        statement.setString(4, user.getUsername());
        statement.setString(5, user.getPassword());
        statement.setDate(6, user.getBirthdate());
        statement.setString(7, user.getEmail());
        statement.setString(8, user.getPhone());
        statement.setString(9, user.getBank_card());
        statement.setInt(10, user.getUser_balance());
        int us = statement.executeUpdate();
        statement.close();
        con.close();
        return us;
    }

    public static int updateBalance(int balance, int id) throws SQLException {
        int result = -1;
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("update USERS set User_balance=User_balance+ ? where User_id=? "); //edit
        statement.setInt(1, balance);
        statement.setInt(2, id);

        int us = statement.executeUpdate();

        PreparedStatement statement2 = con.prepareStatement("select User_balance from USERS where User_id=?");
        statement2.setInt(1, id);
        ResultSet rs = statement2.executeQuery();
        if (rs.next()) {
            result = rs.getInt("User_balance");
        }
        statement.close();
        statement2.close();
        con.close();
        return result;
    }

    public static void DeleteWish(int Wish_id,int User_id) throws SQLException {
        //int result = -1;
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");

        PreparedStatement statement2 = con.prepareStatement("select Contribution_id,Contribution_amount,Contributor_id from Contribution where Wish_id=?");
        statement2.setInt(1, Wish_id);
        ResultSet rs = statement2.executeQuery();
        while (rs.next()) {
            int Contribution_id = rs.getInt("Contribution_id");
            int Contribution_amount = rs.getInt("Contribution_amount");
            int Contributor_id = rs.getInt("Contributor_id");
            PreparedStatement statement3 = con.prepareStatement("update Users set User_balance=User_balance+? where User_id=?");
            statement3.setInt(1, Contribution_amount);
            statement3.setInt(2, Contributor_id);
            statement3.executeUpdate();
            
            PreparedStatement statement4 = con.prepareStatement("insert into NOTIFICATION values(?,?,?,?)");
            int notification_id=DBA.getNotificationMAXID();
            statement4.setInt(1, notification_id);
            String notification = "your friend "+DBA.getUserData(User_id).getFirst_name()+" "+DBA.getUserData(User_id).getLast_name()+" has deleted his"
                    + " wish and you have got a refund of "+Contribution_amount;
            statement4.setString(2, notification);
            statement4.setString(3, "N");
            statement4.setString(4, "Balance Refund");
            statement4.executeUpdate();
            
            PreparedStatement statement5 = con.prepareStatement("insert into User_Notification values(?,?,?,?)");
            statement5.setInt(1, Contributor_id);
            statement5.setInt(2, notification_id);
            statement5.setDate(3, Date.valueOf(LocalDate.now()));
            statement5.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            statement5.executeUpdate();
            
            PreparedStatement statement6 = con.prepareStatement("delete from Contribution where Contribution_id=?");
            statement6.setInt(1, Contribution_id);
            statement6.executeUpdate();
            
            statement3.close();
            statement4.close();
            statement5.close();
             statement6.close();

            
        }
                PreparedStatement statement = con.prepareStatement("delete from wish where Wish_id=?"); //edit
        statement.setInt(1, Wish_id);

        int us = statement.executeUpdate();
        statement.close();
        statement2.close();
        con.close();
        //return result;
    }
    
    public static boolean checkEmail(String email) throws SQLException {
        boolean exists = false;
        try (Connection con = DriverManager.getConnection(connectionString, "iwish", "1234"); 
                PreparedStatement statement = con.prepareStatement("select user_id from USERS where email = ?")) { 
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                exists = true;
            }
        } 
        return exists;
    }
   public static boolean changePassword(String email, String newPassword) throws SQLException {
    boolean isUpdated = false;
    Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");

    try {
        con.setAutoCommit(false); 

        PreparedStatement statement = con.prepareStatement(
            "UPDATE USERS SET password = ? WHERE email = ?"
        );

        statement.setString(1, newPassword);
        statement.setString(2, email);

        int us = statement.executeUpdate();

        if (us <= 0) {
            con.rollback(); 
            System.out.println(" ERROR: Failed to update password");
            return false;
        }

        con.commit();
        System.out.println(" Password updated successfully!");
        isUpdated = true;

    } catch (SQLException e) {
        con.rollback(); 
        System.out.println(" SQL ERROR: " + e.getMessage());
        throw e;
    } finally {
        con.close(); 
    }

    return isUpdated;
}
   
    public static boolean removeFriend(int user_id, int friend_id) throws SQLException {
        // Returns true if at least one row was deleted
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        String query = "DELETE FROM FRIENDLIST WHERE (USER_ID = ? AND FRIEND_ID = ?) OR (USER_ID = ? AND FRIEND_ID = ?)";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, user_id);
        statement.setInt(2, friend_id);
        statement.setInt(3, friend_id);
        statement.setInt(4, user_id);

        int rowsAffected = statement.executeUpdate();

        statement.close();
        con.close();

        return rowsAffected > 0;
    }

    public static ArrayList<FriendWishInfo> getFriendWishes(int Friend_id) throws SQLException {
        /* 
            this method takes friend's id and returns their wishes with the collected amount as an arraylist of FriendInfo objects 
         */
        ArrayList<FriendWishInfo> wishes = new ArrayList<FriendWishInfo>();
        FriendWishInfo friend_wish = null;
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("SELECT "
                + "    w.user_id AS Friend_id, "
                + "    w.WISH_ID AS Wish_id, "
                + "    w.wish_date AS Wish_date, "
                + "    i.NAME AS Name, "
                + "    i.PRICE AS Price, "
                + "    w.STATUS AS Status, "
                + "    w.COLLECTED AS Collected "
                + "FROM "
                + "    wish w "
                + "JOIN "
                + "    item i ON w.item_id = i.item_id "
                + "WHERE "
                + "    w.user_id = ? "
                + "AND "
                + "    (w.Status = 'New'  OR w.Status = 'In progress')"); //edit
        statement.setInt(1, Friend_id);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            friend_wish = new FriendWishInfo(rs.getInt("Friend_id"), rs.getInt("Wish_id"),
                    rs.getDate("Wish_date"), rs.getString("Name"), rs.getDouble("Price"), rs.getDouble("Collected"), rs.getString("Status"));
            wishes.add(friend_wish);
        }
        //System.out.println("no of wishes " + wishes.size());
        rs.close();
        statement.close();
        con.close();
        return wishes;
    }

    public static int getContributionMAXID() throws SQLException {
        /* this works as a sequence in database */
        int contribution_id = -1;
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("select max(contribution_id)+1 as contribution_id from Contribution"); //edit
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            contribution_id = rs.getInt("contribution_id");
        }
        
        rs.close();
        statement.close();
        con.close();
        return contribution_id;
    }

    public static int contributeToWish(Contribution contribution) throws SQLException {
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("INSERT INTO CONTRIBUTION(contribution_id, wish_id, contribution_amount, CONTRIBUTION_DATE, CONTRIBUTION_TIME, CONTRIBUTOR_ID) VALUES (?, ?, ?, sysdate, systimestamp, ?)"); //edit
        statement.setInt(1, contribution.getContribution_id());
        statement.setInt(2, contribution.getWish_id());
        statement.setDouble(3, contribution.getContribution_amount());
        statement.setInt(4, contribution.getContributor_id());

        int rowsAffected = statement.executeUpdate();
        statement.close();
        con.close();

        if (reduceBalance(contribution.getContribution_amount(), contribution.getContributor_id())) {
            System.out.println("Balance reduced");
        } else {
            System.out.println("Error occured while reducing balance!");
        }

        if (updateCollected(contribution.getContribution_amount(), contribution.getWish_id())) {
            System.out.println("Collected amount updated");
        } else {
            System.out.println("Error occured while updating collected amount!");
        }
        if (updateWishStatus(contribution.getWish_id())) {
            System.out.println("Wish Status updated");
        } else {
            System.out.println("Error occured while updating Wish Status!");
        }
        return rowsAffected;
    }

    public static boolean reduceBalance(double Contribution_amount, int User_id) throws SQLException {
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("UPDATE USERS SET USER_BALANCE = USER_BALANCE - ? WHERE USER_ID = ?"); //edit

        statement.setDouble(1, Contribution_amount);
        statement.setInt(2, User_id);

        int rowsAffected = statement.executeUpdate();
        statement.close();
        con.close();
        return rowsAffected > 0;
    }

    public static boolean updateCollected(double Contribution_amount, int Wish_id) throws SQLException {
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("UPDATE WISH SET COLLECTED = COLLECTED + ? WHERE WISH_ID = ?"); //edit

        statement.setDouble(1, Contribution_amount);
        statement.setInt(2, Wish_id);

        int rowsAffected = statement.executeUpdate();
        statement.close();
        con.close();
        return rowsAffected > 0;
    }

    public static boolean updateWishStatus(int wishId) throws SQLException {
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");

        String query = "SELECT w.status, i.price, w.collected FROM wish w "
                + "JOIN item i ON w.item_id = i.item_id WHERE w.wish_id = ?";
        
        int rowsAffected = 0;
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, wishId);
        ResultSet rs = statement.executeQuery();

        if (rs.next()) {
            String wishStatus = rs.getString("status");
            double wishPrice = rs.getDouble("price");
            double collectedAmount = rs.getDouble("collected");

            String updateQuery = null;
            if ("New".equals(wishStatus) && collectedAmount >= wishPrice) {
                updateQuery = "UPDATE wish SET status = 'Completed' WHERE wish_id = ?";
            }else if ("New".equals(wishStatus) && collectedAmount > 0) {
                updateQuery = "UPDATE wish SET status = 'In progress' WHERE wish_id = ?";
            } else if ("In progress".equals(wishStatus) && collectedAmount >= wishPrice) {
                updateQuery = "UPDATE wish SET status = 'Completed' WHERE wish_id = ?";
            }

            if (updateQuery != null) {
                PreparedStatement updateStatement = con.prepareStatement(updateQuery);
                updateStatement.setInt(1, wishId);
                rowsAffected = updateStatement.executeUpdate();
                updateStatement.close();
            }
        }

        rs.close();
        statement.close();
        con.close();
        
        return rowsAffected > 0;
    }

    public static boolean checkUserBalance(int user_id, double contribution_amount) throws SQLException {
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("SELECT USER_BALANCE FROM USERS WHERE USER_ID = ?");

        statement.setInt(1, user_id);
        ResultSet resultSet = statement.executeQuery();

        boolean canContribute = false;
        if (resultSet.next()) {
            double balance = resultSet.getDouble("USER_BALANCE");
            canContribute = balance >= contribution_amount;
        }

        resultSet.close();
        statement.close();
        con.close();

        return canContribute;
    }



    public static boolean insertNotification(NotificationInfo notification, int receiverId) throws SQLException {
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");

        // Insert notification
        String notificationQuery = "INSERT INTO Notification (Notification_id, Context, Is_read, Type) VALUES (?, ?, ?, ?)";
        PreparedStatement notificationStmt = con.prepareStatement(notificationQuery);

        notificationStmt.setInt(1, notification.getNotification_id());
        notificationStmt.setString(2, notification.getContext());
        notificationStmt.setString(3, "N"); // New notification is unread
        notificationStmt.setString(4, notification.getType());

        notificationStmt.executeUpdate();
        notificationStmt.close();

        // Insert into User_Notification
        String userNotificationQuery = "INSERT INTO User_Notification (Reciever_id, Notification_id, Notification_Date, Notification_Time) VALUES (?, ?, SYSDATE, SYSTIMESTAMP)";
        PreparedStatement userNotificationStmt = con.prepareStatement(userNotificationQuery);

        userNotificationStmt.setInt(1, receiverId);
        userNotificationStmt.setInt(2, notification.getNotification_id());

        int rowsAffected = userNotificationStmt.executeUpdate();
        userNotificationStmt.close();
        con.close();
        return rowsAffected > 0;
    }

    public static boolean insertNotification(NotificationInfo notification, ArrayList<Integer> receiverIds) throws SQLException {
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");

        String notificationQuery = "INSERT INTO Notification (Notification_id, Context, Is_read, Type) VALUES (?, ?, ?, ?)";
        PreparedStatement notificationStmt = con.prepareStatement(notificationQuery);

        notificationStmt.setInt(1, notification.getNotification_id());
        notificationStmt.setString(2, notification.getContext());
        notificationStmt.setString(3, "N");
        notificationStmt.setString(4, notification.getType());

        notificationStmt.executeUpdate();
        notificationStmt.close();

        // insert notification for each user
        String userNotificationQuery = "INSERT INTO User_Notification (Reciever_id, Notification_id, Notification_Date, Notification_Time) VALUES (?, ?, SYSDATE, SYSTIMESTAMP)";
        PreparedStatement userNotificationStmt = con.prepareStatement(userNotificationQuery);

        for (int receiverId : receiverIds) {
            userNotificationStmt.setInt(1, receiverId);
            userNotificationStmt.setInt(2, notification.getNotification_id());
            userNotificationStmt.addBatch();
        }

        int[] rowsAffected = userNotificationStmt.executeBatch();
        userNotificationStmt.close();
        con.close();
        for (int i : rowsAffected) {
            if (i == 0) {
                return false;
            }
        }
        return true;

    }

    public static ArrayList<Integer> getContributors(int wishId) throws SQLException {
        ArrayList<Integer> contributors = new ArrayList<>();
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement(
                "SELECT DISTINCT contributor_id FROM CONTRIBUTION WHERE wish_id = ?"
        );
        statement.setInt(1, wishId);
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            contributors.add(rs.getInt("contributor_id"));
        }

        rs.close();
        statement.close();
        con.close();
        return contributors;
    }

    public static String getWishItem(int Wish_id) throws SQLException {
        String itemName = "";
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("select i.name AS Name from Wish w JOIN Item i ON w.item_id = i.item_id where Wish_id=?"); //edit
        statement.setInt(1, Wish_id);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            itemName = rs.getString("Name");
        }
        
        rs.close();
        statement.close();
        con.close();
        return itemName;
    }
    
     public static ArrayList<NotificationInfo> getUserNotifications(int User_id) throws SQLException {
        /* 
            this method takes user's id and returns their Notifications as an arraylist of Notification objects 
         */
        ArrayList<NotificationInfo> notifications = new ArrayList<NotificationInfo>();
        NotificationInfo notification = null;
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("SELECT n.notification_id as Notification_id, n.CONTEXT as Context, n.IS_READ as IsRead, u.Notification_date as Notification_date FROM NOTIFICATION n JOIN USER_NOTIFICATION u ON n.notification_id = u.notification_id WHERE u.RECIEVER_ID = ?"); //edit
        statement.setInt(1, User_id);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            notification = new NotificationInfo(rs.getInt("Notification_id"), rs.getString("Context"), rs.getString("IsRead"), rs.getDate("Notification_date"));
            notifications.add(notification);
        }
        //System.out.println("no of notifications "+notifications.size());
        rs.close();
        statement.close();
        con.close();
        return notifications;
    }

    
    

}
