/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author osama
 */
public class WishInfo {
   private int wish_id;
   private int User_id;
   private int ITEM_ID;
   private Timestamp Wish_Time;
   String Status;
   private String Item_Name;
   private int Item_Price;
  private Date WISH_DATE;
  private int contribution_amount;

    public WishInfo(int User_id, String Item_Name, int Item_Price, Date WISH_DATE, int contribution_amount,int wish_id) {
        this.User_id = User_id;
        this.Item_Name = Item_Name;
        this.Item_Price = Item_Price;
        this.WISH_DATE = WISH_DATE;
        this.contribution_amount = contribution_amount;
        this.wish_id=wish_id;
    }

    public void setContribution_amount(int contribution_amount) {
        this.contribution_amount = contribution_amount;
    }

    public int getWish_id() {
        return wish_id;
    }

    public void setWish_id(int wish_id) {
        this.wish_id = wish_id;
    }

    public int getITEM_ID() {
        return ITEM_ID;
    }

    public void setITEM_ID(int ITEM_ID) {
        this.ITEM_ID = ITEM_ID;
    }

    public Timestamp getWish_Time() {
        return Wish_Time;
    }

    public void setWish_Time(Timestamp Wish_Time) {
        this.Wish_Time = Wish_Time;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public WishInfo(int wish_id, int User_id, int ITEM_ID) {
        this.wish_id = wish_id;
        this.User_id = User_id;
        this.ITEM_ID = ITEM_ID;

    }

    public int getContribution_amount() {
        return contribution_amount;
    }
    @Override
    public String toString() {
        return "{" + "User_id:" + User_id + ", Item_Name:" + Item_Name + ", Item_Price:" + Item_Price + ", WISH_DATE:" + WISH_DATE + ", contribution_amount:" + contribution_amount + ", wish_id:"+wish_id+ "}";
    }


    public int getUser_id() {
        return User_id;
    }

    public void setUser_id(int User_id) {
        this.User_id = User_id;
    }

    public String getItem_Name() {
        return Item_Name;
    }

    public void setItem_Name(String Item_Name) {
        this.Item_Name = Item_Name;
    }

    public int getItem_Price() {
        return Item_Price;
    }

    public void setItem_Price(int Item_Price) {
        this.Item_Price = Item_Price;
    }

    public Date getWISH_DATE() {
        return WISH_DATE;
    }

    public void setWISH_DATE(Date WISH_DATE) {
        this.WISH_DATE = WISH_DATE;
    }
   

    
}
