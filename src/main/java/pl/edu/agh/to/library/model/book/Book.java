package pl.edu.agh.to.library.model.book;

import jakarta.persistence.*;
import pl.edu.agh.to.library.model.genre.Genre;
import pl.edu.agh.to.library.model.author.Author;
import pl.edu.agh.to.library.model.rating.Rating;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Book {

    @Id
    @GeneratedValue
    private int id;

    private String title;

    private String language;

    private String imageUrl;

    private int copiesLeft;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Rating> ratings = new HashSet<>();

    public Book() {
    }

    public Book(String title, String language, int copiesLeft, Author author, Set<Genre> genres, String imageUrl) {
        this.title = title;
        this.language = language;
        this.copiesLeft = copiesLeft;
        this.author = author;
        this.genres = genres;
        this.imageUrl = imageUrl;
    }

    public void addRating(Rating rating) {
        ratings.add(rating);
        rating.setBook(this);
    }

    public double calculateAverageRating() {
        if (ratings.isEmpty()) {
            return 0.0;
        }

        double sum = ratings.stream().mapToInt(Rating::getRatingValue).sum();
        return Math.round(((sum / ratings.size()) * 100.0)) / 100.0;
    }

    public int getId() {
        return id;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public int getCopiesLeft() {
        return copiesLeft;
    }

    public String getTitle() {
        return title;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public String getGenresAsString() {
        if (genres != null && !genres.isEmpty()) {
            return genres.stream()
                    .map(Genre::getGenreName)
                    .collect(Collectors.joining(", "));
        } else {
            return "";
        }
    }

    public Author getAuthor() {
        return author;
    }

    public String getLanguage() {
        return language;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCopiesLeft(int copiesLeft) {
        this.copiesLeft = copiesLeft;
    }
}
