/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientApp;

import java.sql.Date;



/**
 *
 * @author osama
 */
public class UserInfo {

    private int User_id;
    private String First_name;
    private String Last_name;
    private String Username;
    private String Password;
    private Date Birthdate;
    private String Email;
    private String Phone;
    private String Bank_card;
    private int User_balance;

    UserInfo(int aInt, String string, String optString, Date birthdate, String optString0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    




    public void setUser_id(int User_id) {
        this.User_id = User_id;
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

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public void setBirthdate(Date Birthdate) {
        this.Birthdate = Birthdate;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public void setBank_card(String Bank_card) {
        this.Bank_card = Bank_card;
    }

    public void setUser_balance(int User_balance) {
        this.User_balance = User_balance;
    }

    public int getUser_id() {
        return User_id;
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

    public String getPassword() {
        return Password;
    }

    public Date getBirthdate() {
        return Birthdate;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhone() {
        return Phone;
    }

    public String getBank_card() {
        return Bank_card;
    }

    public int getUser_balance() {
        return User_balance;
    }

    public UserInfo(int User_id, String First_name, String Last_name, String Username, String Password, Date Birthdate, String Email, String Phone, String Bank_card, int User_balance) {
        this.User_id = User_id;
        this.First_name = First_name;
        this.Last_name = Last_name;
        this.Username = Username;
        this.Password = Password;
        this.Birthdate = Birthdate;
        this.Email = Email;
        this.Phone = Phone;
        this.Bank_card = Bank_card;
        this.User_balance = User_balance;
    }
     public UserInfo(int User_id, String First_name, String Last_name, String Username, Date Birthdate, String Email, String Phone, String Bank_card, int User_balance) {
        this.User_id = User_id;
        this.First_name = First_name;
        this.Last_name = Last_name;
        this.Username = Username;
        this.Birthdate = Birthdate;
        this.Email = Email;
        this.Phone = Phone;
        this.Bank_card = Bank_card;
        this.User_balance = User_balance;
    }
     public UserInfo(int User_id, String Username,  Date Birthdate, String Email) {
        this.User_id = User_id;
        this.Username = Username;
        this.Birthdate = Birthdate;
        this.Email = Email;
    }
     public void setUser(int User_id, String First_name, String Last_name, String Username, 
                    String Password, Date Birthdate, String Email, String Phone, 
                    String Bank_card, int User_balance) {
    this.User_id = User_id;
    this.First_name = First_name;
    this.Last_name = Last_name;
    this.Username = Username;
    this.Password = Password;
    this.Birthdate = Birthdate;
    this.Email = Email;
    this.Phone = Phone;
    this.Bank_card = Bank_card;
    this.User_balance = User_balance;
}
  public UserInfo() {
}
     public static UserInfo getUser() {
    return new UserInfo(); // Ensure this returns a valid instance
}   



}
    