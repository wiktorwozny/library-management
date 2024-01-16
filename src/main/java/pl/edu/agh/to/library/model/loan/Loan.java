package pl.edu.agh.to.library.model.loan;

import jakarta.persistence.*;
import pl.edu.agh.to.library.model.user.User;
import pl.edu.agh.to.library.model.book.Book;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity
public class Loan {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private Date loanDate = new Date();

    private Date returnDate;

    private boolean isReturned = false;

    public Loan() {}

    public Loan(User user, Book book, int days) {
        this.user = user;
        this.book = book;
        this.returnDate = setReturnDate(days);
    }

    public Loan(User user, Book book) {
        this.user = user;
        this.book = book;
        this.returnDate = setReturnDate(30);
    }

    private Date setReturnDate(int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(loanDate);
        c.add(Calendar.DATE, days);

        return c.getTime();
    }

    public Book getBook() {
        return book;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public String getUserEmail() {
        return user.getEmail();
    }

    public String getUserFirstname() {
        return user.getFirstname();
    }

    public String getUserLastname() {
        return user.getLastname();
    }

    public String getBookTitle() {
        return book.getTitle();
    }

    public String getLoanDateToString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(loanDate);
    }

    public String getReturnDateToString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(returnDate);
    }

    public String isReturnedToString() {
        return isReturned ? "tak" : "nie";
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    public User getUser() {
        return user;
    }


}
