package pl.edu.agh.to.library.model.genre;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import pl.edu.agh.to.library.model.book.Book;

import java.util.Set;

@Entity
public class Genre {

    @Id
    @GeneratedValue
    private int id;

    private String genreName;

    @ManyToMany(mappedBy = "genres")
    Set<Book> books;

    public Genre() {}

    public Genre(String genreName) {
        this.genreName = genreName;
    }

    public String getGenreName() {
        return genreName;
    }
}
