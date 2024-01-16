package pl.edu.agh.to.library.model.loan;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.library.model.book.Book;
import pl.edu.agh.to.library.model.book.BookRepository;
import pl.edu.agh.to.library.model.user.User;
import pl.edu.agh.to.library.model.user.UserRepository;
import java.util.List;
import pl.edu.agh.to.library.notifications.MailService;

@Service
public class LoanService {
    private final LoanRepository loanRepository;

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    private MailService mailService;

    private final int pageSize = 10;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService; // Setter injection to resolve circular dependency
    }

    public void borrowBook(int bookId, User user) {
        Book book = this.bookRepository.findById(bookId)
                        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono książki o ID: " + bookId));

        if (book.getCopiesLeft() <= 0) {
            throw new IllegalStateException("Brak dostępnych kopii książki o id: " + bookId);
        }

        book.setCopiesLeft(book.getCopiesLeft() - 1);
        bookRepository.save(book);

        Loan newLoan = new Loan(user, book);
        loanRepository.save(newLoan);

        mailService.sendBookLoanedNotification(newLoan);
    }

    public void returnBook(int bookId, User user) {
        Loan loan = loanRepository.findOldestBookLoanByUserId(user.getId(), bookId);
        Book book = this.bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono książki o ID: " + bookId));

        if (loan == null) {
            throw new IllegalStateException("Użytkownik o id " + user.getId() + "zwrócił wszystkie książki o id " + bookId);
        }

        book.setCopiesLeft(book.getCopiesLeft() + 1);
        bookRepository.save(book);

        loan.setReturned(true);
        loanRepository.save(loan);
    }

    public List<Loan> getLoansByUserId(int userId) {
        return loanRepository.findByUserId(userId);
    }

    public List<Loan> getLoansByBookId(int bookId){
        return loanRepository.findByBookId(bookId);
    }

    public Slice<Loan> getPage(int pageNumber) {
        return loanRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    public Slice<Loan> getAllLoansForSpecificUserPage(String email, int pageNumber) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + email);
        }

        return loanRepository.findAllLoansForSpecificUser(user.getId(), PageRequest.of(pageNumber, pageSize));
    }

    public Slice<Loan> getUnreturnedLoansPage(int pageNumber) {
        return loanRepository.findUnreturnedLoans(PageRequest.of(pageNumber, pageSize));
    }

    public Slice<Loan> getUnreturnedLoansForSpecificUserPage(String email, int pageNumber) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + email);
        }

        return loanRepository.findUnreturnedLoansForSpecificUser(user.getId(), PageRequest.of(pageNumber, pageSize));
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public long getCount() {
        return loanRepository.count();
    }
}
