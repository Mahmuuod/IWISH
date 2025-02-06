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

    public static boolean removeFriend(int user_id, int friend_id) throws SQLException {
        // Returns true if at least one row was deleted
        Connection con = DriverManager.getConnection(connectionString, "iwish", "1234");
        //TODO remove row where user_id - friend_id and friend_id = user_id
        String query = "DELETE FROM FRIENDLIST WHERE USER_ID = ? AND FRIEND_ID = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, user_id);
        statement.setInt(2, friend_id);

        int rowsAffected = statement.executeUpdate(); 

        statement.close();
        con.close();

        return rowsAffected > 0; 
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
