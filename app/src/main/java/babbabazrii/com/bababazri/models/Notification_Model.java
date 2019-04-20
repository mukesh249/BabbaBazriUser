package babbabazrii.com.bababazri.models;

import java.io.Serializable;

public class Notification_Model implements Serializable{
    String Notification_Title,Notification_message,notificatin_createdAt;

    public String getNotificatin_createdAt() {
        return notificatin_createdAt;
    }

    public void setNotificatin_createdAt(String notificatin_createdAt) {
        this.notificatin_createdAt = notificatin_createdAt;
    }

    public String getNotification_Title() {
        return Notification_Title;
    }

    public void setNotification_Title(String notification_Title) {
        Notification_Title = notification_Title;
    }

    public String getNotification_message() {
        return Notification_message;
    }

    public void setNotification_message(String notification_message) {
        Notification_message = notification_message;
    }
}
