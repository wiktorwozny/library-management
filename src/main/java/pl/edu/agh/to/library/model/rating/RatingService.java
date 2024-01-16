package pl.edu.agh.to.library.model.rating;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.library.model.book.Book;
import pl.edu.agh.to.library.model.book.BookRepository;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final BookRepository bookRepository;

    public RatingService(RatingRepository ratingRepository, BookRepository bookRepository) {
        this.ratingRepository = ratingRepository;
        this.bookRepository = bookRepository;
    }

    public void addRatingToBook(int bookId, Rating rating) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono książki o ID: " + bookId));

        if (rating.getRatingValue() < 1 || rating.getRatingValue() > 5) {
            throw new IllegalArgumentException("Wartość oceny musi być w zakresie od 1 do 5.");
        }

        book.addRating(rating);
        ratingRepository.save(rating);
    }
}
