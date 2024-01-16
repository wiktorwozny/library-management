package pl.edu.agh.to.library;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.to.library.model.book.Book;
import pl.edu.agh.to.library.model.book.BookRepository;
import pl.edu.agh.to.library.model.loan.Loan;
import pl.edu.agh.to.library.model.loan.LoanRepository;
import pl.edu.agh.to.library.model.loan.LoanService;
import pl.edu.agh.to.library.model.user.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTests {

    @InjectMocks
    private LoanService loanService;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Test
    public void borrowBookTest() {

        int bookId = 1;
        Book book = new Book();
        book.setId(bookId);
        book.setCopiesLeft(2);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        loanService.borrowBook(bookId, new User());

        verify(bookRepository, times(1)).save(book);
        assertEquals(1, book.getCopiesLeft());
        verify(loanRepository, times(1)).save(any(Loan.class));
    }
}
