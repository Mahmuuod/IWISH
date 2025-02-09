/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import org.json.JSONObject;

/**
 *
 * @author randa
 */
public class Notification {
    private int Notification_id;
    private String Context;
    private String IsRead;
    private String Type;

    public Notification(int Notification_id, String Context, String IsRead, String Type) {
        this.Notification_id = Notification_id;
        this.Context = Context;
        this.IsRead = IsRead;
        this.Type = Type;
    }

    @Override
    public String toString() {
        return "{" + "Notification_id:" + Notification_id + ", Context:" + Context + ", IsRead:" + IsRead + ", Type:" + Type + "}";
    }

    public int getNotification_id() {
        return Notification_id;
    }

    public String getContext() {
        return Context;
    }

    public String getIsRead() {
        return IsRead;
    }

    public String getType() {
        return Type;
    }

    public void setNotification_id(int Notification_id) {
        this.Notification_id = Notification_id;
    }

    public void setContext(String Context) {
        this.Context = Context;
    }

    public void setIsRead(String IsRead) {
        this.IsRead = IsRead;
    }

    public void setType(String Type) {
        this.Type = Type;
    }
    
}

