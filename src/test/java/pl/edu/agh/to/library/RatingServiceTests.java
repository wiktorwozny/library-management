package pl.edu.agh.to.library;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.to.library.model.book.Book;
import pl.edu.agh.to.library.model.book.BookRepository;
import pl.edu.agh.to.library.model.rating.Rating;
import pl.edu.agh.to.library.model.rating.RatingRepository;
import pl.edu.agh.to.library.model.rating.RatingService;
import pl.edu.agh.to.library.model.user.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RatingServiceTests {

    @InjectMocks
    private RatingService ratingService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Test
    public void addRatingToBookTest() {

        int bookId = 1;
        Book book = new Book();
        book.setId(bookId);

        Rating rating = new Rating(book, new User(), 3, "text");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        ratingService.addRatingToBook(bookId, rating);

        verify(ratingRepository, times(1)).save(rating);
        assertTrue(book.getRatings().contains(rating));
        assertEquals(book, rating.getBook());
    }

    @Test
    public void addRatingToBookInvalidValueTest() {

        int bookId = 1;
        Book book = new Book();
        book.setId(bookId);

        Rating rating = new Rating(book, new User(), 6, "text");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ratingService.addRatingToBook(bookId, rating));

        assertEquals("Wartość oceny musi być w zakresie od 1 do 5.", exception.getMessage());
        verify(ratingRepository, never()).save(rating);
    }
}
