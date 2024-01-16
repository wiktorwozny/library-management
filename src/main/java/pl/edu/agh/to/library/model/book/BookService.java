package pl.edu.agh.to.library.model.book;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.library.model.author.Author;
import pl.edu.agh.to.library.model.genre.Genre;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import pl.edu.agh.to.library.model.loan.Loan;
import pl.edu.agh.to.library.model.rating.Rating;
import pl.edu.agh.to.library.model.rating.RatingRepository;


@Service
public class BookService {

    private final BookRepository bookRepository;

    private final RatingRepository ratingRepository;

    private Book currentBook = null;
    private final int pageSize = 10;

    public BookService(BookRepository bookRepository, RatingRepository ratingRepository) {
        this.bookRepository = bookRepository;
        this.ratingRepository = ratingRepository;
    }

    public double getAverageRatingForBook(int bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono książki o ID: " + bookId));

        return book.calculateAverageRating();
    }

    public Book getBookById(int bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono książki o ID: " + bookId));
    }

    public Book getCurrentBook() {
        return getBookById(currentBook.getId());
    }

    public void setCurrentBook(Book currentBook) {
        this.currentBook = currentBook;
    }

    public List<Rating> getAllReviewsForBook(int bookId) {
        return ratingRepository.findByBookId(bookId);
    }

    public void addBook(Book book) {
        bookRepository.save(book);
    }
    public List<Book> getPage(int pageNumber) {
        return bookRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }
    public int getAllPages(){
        return bookRepository.findAll(PageRequest.of(0, pageSize)).getTotalPages();
    }
    public List<Book> findBooksByFilter(Author author, String language, Genre genre) {
        Specification<Book> spec = Specification.where(null);

        if (author != null) {
            spec = spec.and(BookSpecifications.hasAuthor(author));
        }

        if (language != null) {
            spec = spec.and(BookSpecifications.hasLanguage(language));
        }

        if (genre != null) {
            spec = spec.and(BookSpecifications.hasGenre(genre));
        }

        return bookRepository.findAll(spec);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public double getBookRating(int bookId) {
        Double rating = bookRepository.getBookRating(bookId);
        if (rating==null) {
            return 0.0;
        }
        return rating.doubleValue();
    }
    public boolean isRecommended(int bookId){
        return getBookRating(bookId)>=4.0;
    }
}
