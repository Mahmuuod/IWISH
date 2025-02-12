/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBA;

import DAL.FriendInfo;
import DAL.ItemsInfo;
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
        PreparedStatement statement = con.prepareStatement("select  WISH.Wish_id,WISH.USER_ID,ITEM.NAME,ITEM.PRICE,WISH.WISH_DATE,CONTRIBUTION.CONTRIBUTION_AMOUNT from WISH left join ITEM on ITEM.ITEM_ID=WISH.ITEM_ID left join CONTRIBUTION\n"
                + "on WISH.WISH_ID=CONTRIBUTION.WISH_ID where USER_ID=? "); //edit

        statement.setInt(1, User_id);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            WishInfo wish = new WishInfo(rs.getInt("USER_ID"), rs.getString("NAME"), rs.getInt("PRICE"), rs.getDate("WISH_DATE"), rs.getInt("CONTRIBUTION_AMOUNT"), rs.getInt("Wish_id"));
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
        statement.setString(6, "new");
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
                    + " wish and you have got a refund = "+Contribution_amount;
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
