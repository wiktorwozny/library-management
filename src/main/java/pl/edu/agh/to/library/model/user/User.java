package pl.edu.agh.to.library.model.user;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import pl.edu.agh.to.library.model.notification.Notification;
import pl.edu.agh.to.library.model.role.Role;

@Entity
public class User {

    @Id
    @GeneratedValue
    private int id;

    private String firstname;

    private String lastname;

    @Column(unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user")
    private Notification notification;

    public User() {
    }

    public User(String firstname, String lastname, String email, Role role, Notification notification) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.role = role;
        this.notification = notification;
    }

    @Override
    public String toString() {
        return getFirstname() + " " + getLastname();
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Notification getNotification() {
        return notification;
    }
}

