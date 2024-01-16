package pl.edu.agh.to.library.model.rating;

import jakarta.persistence.*;
import pl.edu.agh.to.library.model.book.Book;
import pl.edu.agh.to.library.model.user.User;

@Entity
public class Rating {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int ratingValue;

    private String reviewText;

    public Rating() {}

    public Rating(Book book, User user, int ratingValue, String reviewText) {
        this.book = book;
        this.user = user;
        this.ratingValue = ratingValue;
        this.reviewText = reviewText;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public String getReviewText() {
        return reviewText;
    }

    public String getUserName() {
        return user.toString();
    }
}
