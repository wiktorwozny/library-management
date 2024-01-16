package pl.edu.agh.to.library.model.author;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Author {

    @Id
    @GeneratedValue
    private int id;

    private String firstname;

    public int getId() {
        return id;
    }

    private String lastname;

    private String country;

    public Author() {}

    public Author(String firstname, String lastname, String country) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.country = country;
    }

    public String getName() {
        return toString();
    }

    @Override
    public String toString() {
        return firstname + " " + lastname;
    }
}
