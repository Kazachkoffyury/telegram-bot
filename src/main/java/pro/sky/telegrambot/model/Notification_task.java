package pro.sky.telegrambot.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Notification_task {
    @Id
    @GeneratedValue
    private long id;
    private long id_chat;
    private String notification;
    private LocalDateTime date;



    public Notification_task() {

    }

    public long getId_chat() {
        return id_chat;
    }

    public void setId_chat(long id_chat) {
        this.id_chat = id_chat;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }





}
