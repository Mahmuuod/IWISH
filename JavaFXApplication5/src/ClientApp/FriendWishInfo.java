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
public class FriendWishInfo {
    
    private int Friend_id;
    private int Wish_id;
    private Date Wish_date;
    private String Name;
    private Double Price;
    private Double Collected;
    
    @Override
    public String toString() {
        return "{" + "Friend_id:" + Friend_id + ", Wish_id:" + Wish_id + ", Wish_date:" + Wish_date + ", Name:" + Name + ", Price:" + Price + ", Collected:" + Collected + "}";
    }
    
    public FriendWishInfo(int Friend_id, int Wish_id, Date Wish_date, String Name, Double Price, Double Collected) {
        this.Friend_id = Friend_id;
        this.Wish_id = Wish_id;
        this.Wish_date = Wish_date;
        this.Name = Name;
        this.Price = Price;
        this.Collected = Collected;
    }

    public void setFriend_id(int Friend_id) {
        this.Friend_id = Friend_id;
    }

    public void setWish_id(int Wish_id) {
        this.Wish_id = Wish_id;
    }

    public void setWish_date(Date Wish_date) {
        this.Wish_date = Wish_date;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setPrice(Double Price) {
        this.Price = Price;
    }

    public void setCollected(Double Collected) {
        this.Collected = Collected;
    }

    public int getFriend_id() {
        return Friend_id;
    }

    public int getWish_id() {
        return Wish_id;
    }

    public Date getWish_date() {
        return Wish_date;
    }

    public String getName() {
        return Name;
    }

    public Double getPrice() {
        return Price;
    }

    public Double getCollected() {
        return Collected;
    }

    

}