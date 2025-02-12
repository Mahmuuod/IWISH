/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBA;

import DAL.FriendInfo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import oracle.jdbc.driver.OracleDriver;
import javafx.scene.control.Alert;
import DAL.UserInfo;
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
        try (Connection con = DriverManager.getConnection(connectionString, "iwish", "1234")) {
            PreparedStatement statement = con.prepareStatement("select User_id from USERS where Username=? and Password=?"); //edit
            statement.setString(1, Username);
            statement.setString(2, Password);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                exists = true;
            }
            statement.close();
        } 
        return exists;
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
    /*   public static int deleteContacts(int id) throws SQLException {
        DriverManager.registerDriver(new OracleDriver());
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("delete from student where id=?"); //edit
        statement.setInt(1, id);

        int us = statement.executeUpdate();
        PreparedStatement statement2 = con.prepareStatement("select * from student"); //edit
        ResultSet rs = statement2.executeQuery();
        while (rs.next()) {
            UserInfo temp = new UserInfo(rs.getInt("id"), rs.getString("Fname"), rs.getString("Lname"), rs.getString("Mname"), rs.getString("email"), rs.getString("phone"));
            contacts.add(temp);
        }
        statement.close();
                statement2.close();

        con.close();
        return us;

    }

    public static int updateContacts(int id, String Fname, String Lname, String Mname, String email, String phone) throws SQLException {
        DriverManager.registerDriver(new ClientDriver());
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/dm45connect", "root", "root");
        PreparedStatement statement = con.prepareStatement("update student set  Fname=?, Lname=?, Mname=?, email=?, phone=? where id=?"); //edit
        statement.setInt(6, id);
        statement.setString(2-1, Fname);
        statement.setString(3-1, Lname);
        statement.setString(4-1, Mname);
        statement.setString(5-1, email);
        statement.setString(6-1, phone);
        
        int us = statement.executeUpdate();
        /*if(us!=-1)
        {
            
        }
        else
        {
            con.rollback();
                   Alert a=new Alert(Alert.AlertType.INFORMATION);
                   a.setTitle("Warning");
                   a.setHeaderText("plz enter correct data");
                   a.show();
        }
        PreparedStatement statement2 = con.prepareStatement("select * from student"); //edit
        ResultSet rs = statement2.executeQuery();
        while (rs.next()) {
            Contact temp = new Contact(rs.getInt("id"), rs.getString("Fname"), rs.getString("Lname"), rs.getString("Mname"), rs.getString("email"), rs.getString("phone"));
            contacts.add(temp);
        }
        statement.close();
                statement2.close();

        con.close();
        return us;

    }
    public static int newContacts(int id, String Fname, String Lname, String Mname, String email, String phone) throws SQLException {
        DriverManager.registerDriver(new ClientDriver());
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/dm45connect", "root", "root");
        PreparedStatement statement = con.prepareStatement("insert into student values(?,?,?,?,?,?)"); //edit
        statement.setInt(1, id);
        statement.setString(2, Fname);
        statement.setString(3, Lname);
        statement.setString(4, Mname);
        statement.setString(5, email);
        statement.setString(6, phone);
        
        int us = statement.executeUpdate();
        /*if(us!=-1)
        {
            
        }
        else
        {
            con.rollback();
                   Alert a=new Alert(Alert.AlertType.INFORMATION);
                   a.setTitle("Warning");
                   a.setHeaderText("plz enter correct data");
                   a.show();
        }
        PreparedStatement statement2 = con.prepareStatement("select * from student"); //edit
        ResultSet rs = statement2.executeQuery();
        while (rs.next()) {
            Contact temp = new Contact(rs.getInt("id"), rs.getString("Fname"), rs.getString("Lname"), rs.getString("Mname"), rs.getString("email"), rs.getString("phone"));
            contacts.add(temp);
        }
        statement.close();
                statement2.close();

        con.close();
        return us;

    }*/
}