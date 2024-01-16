package pl.edu.agh.to.library.model.notification;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import pl.edu.agh.to.library.model.user.User;

@Entity
public class Notification {
    @Id
    @GeneratedValue
    private int notificationId;

    private boolean enableNotifications = true;

    private boolean sendBookLoanedNotification = true;

    private boolean sendReturnReminder = true;

    private int daysLeftToReturnReminder = 5;

    private boolean sendBookOverdueReminder = true;

    @OneToOne
    @JoinColumn(name="user_id")
    @Cascade(CascadeType.ALL)
    private User user;

    public Notification(){}

    public Notification(User user) {
        this.user = user;
    }

    public boolean isEnableNotifications() {
        return enableNotifications;
    }

    public void setEnableNotifications(boolean enableNotifications) {
        this.enableNotifications = enableNotifications;
    }

    public boolean isSendBookLoanedNotification() {
        return sendBookLoanedNotification;
    }

    public void setSendBookLoanedNotification(boolean sendBookLoanedNotification) {
        this.sendBookLoanedNotification = sendBookLoanedNotification;
    }

    public boolean isSendReturnReminder() {
        return sendReturnReminder;
    }

    public void setSendReturnReminder(boolean sendReturnReminder) {
        this.sendReturnReminder = sendReturnReminder;
    }

    public int getDaysLeftToReturnReminder() {
        return daysLeftToReturnReminder;
    }

    public void setDaysLeftToReturnReminder(int daysLeftToReturnReminder) {
        this.daysLeftToReturnReminder = daysLeftToReturnReminder;
    }

    public boolean isSendBookOverdueReminder() {
        return sendBookOverdueReminder;
    }

    public void setSendBookOverdueReminder(boolean sendBookOverdueReminder) {
        this.sendBookOverdueReminder = sendBookOverdueReminder;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
