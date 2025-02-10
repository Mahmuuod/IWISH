/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBA;

import DAL.Contribution;
import DAL.FriendInfo;
import DAL.FriendWishInfo;
import DAL.NotificationInfo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import oracle.jdbc.driver.OracleDriver;
import javafx.scene.control.Alert;
import DAL.UserInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        
        rs.close();
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
        while (rs.next()) {
            friend = new FriendInfo(rs.getInt("FRIEND_ID"), rs.getString("FIRST_NAME"),
                    rs.getString("LAST_NAME"), rs.getString("USERNAME"), rs.getDate("BIRTHDATE"), rs.getString("EMAIL"));
            friends.add(friend);
        }
        //System.out.println("no of friends "+friends.size());
        statement.close();
        con.close();
        return friends;
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
                + "    (w.Status = 'New' OR w.Status = 'In progress')"); //edit
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

    public static int getNotificationMAXID() throws SQLException {
        /* this works as a sequence in database */
        int Notification_id = -1;
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        PreparedStatement statement = con.prepareStatement("select max(Notification_id)+1 as Notification_id from Notification"); //edit
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            Notification_id = rs.getInt("Notification_id");
        }

        rs.close();
        statement.close();
        con.close();
        return Notification_id;
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
