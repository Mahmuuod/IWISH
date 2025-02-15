/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientApp;

import java.util.Date;

/**
 *
 * @author randa
 */
public class NotificationInfo {
    
    private int Notification_id;
    private String Context;
    private Date Notification_date;
    private String IsRead;
    
    public NotificationInfo(int Notification_id, String Context, Date Notification_date, String IsRead) {
        this.Notification_id = Notification_id;
        this.Context = Context;
        this.Notification_date = Notification_date;
        this.IsRead = IsRead;
    }

@Override
public String toString() {
        return "{" + "Notification_id:" + Notification_id + ", Context:" + Context + ", Notification_date:" + Notification_date + ", IsRead:" + IsRead+ "}";
    }

    public void setIsRead(String IsRead) {
        this.IsRead = IsRead;
    }

    public String getIsRead() {
        return IsRead;
    }

    public int getNotification_id() {
        return Notification_id;
    }

    public String getContext() {
        return Context;
    }

    public Date getNotification_date() {
        return Notification_date;
    }

    public void setNotification_id(int Notification_id) {
        this.Notification_id = Notification_id;
    }

    public void setContext(String Context) {
        this.Context = Context;
    }

    public void setNotification_date(Date Notification_date) {
        this.Notification_date = Notification_date;
    }


}