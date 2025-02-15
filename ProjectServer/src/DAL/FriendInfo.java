/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import java.sql.Date;



/**
 *
 * @author osama
 */
public class FriendInfo {

    private int Friend_id;
    private String First_name;
    private String Last_name;
    private String Username;
    private Date Birthdate;
    private String Email;


    @Override
    public String toString() {
        return "{" + "Friend_id:" + Friend_id + ", First_name:" + First_name + ", Last_name:" + Last_name + ", Username:" + Username + ", Birthdate:" + Birthdate + ", Email:" + Email + "}";
    }

    public void setFriend_id(int Friend_id) {
        this.Friend_id = Friend_id;
    }

    public void setFirst_name(String First_name) {
        this.First_name = First_name;
    }

    public void setLast_name(String Last_name) {
        this.Last_name = Last_name;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public void setBirthdate(Date Birthdate) {
        this.Birthdate = Birthdate;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public int getFriend_id() {
        return Friend_id;
    }

    public String getFirst_name() {
        return First_name;
    }

    public String getLast_name() {
        return Last_name;
    }

    public String getUsername() {
        return Username;
    }

    public Date getBirthdate() {
        return Birthdate;
    }

    public String getEmail() {
        return Email;
    }

    public FriendInfo(int Friend_id, String First_name, String Last_name, String Username, Date Birthdate, String Email) {
        this.Friend_id = Friend_id;
        this.First_name = First_name;
        this.Last_name = Last_name;
        this.Username = Username;
        this.Birthdate = Birthdate;
        this.Email = Email;
    }



}