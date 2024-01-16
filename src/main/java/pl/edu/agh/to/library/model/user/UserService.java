package pl.edu.agh.to.library.model.user;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.library.model.book.Book;
import pl.edu.agh.to.library.model.loan.Loan;
import pl.edu.agh.to.library.model.loan.LoanRepository;
import pl.edu.agh.to.library.model.rating.Rating;
import pl.edu.agh.to.library.model.rating.RatingRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final LoanRepository loanRepository;

    private final RatingRepository ratingRepository;

    private User currentUser = null;

    public UserService(UserRepository userRepository, LoanRepository loanRepository, RatingRepository ratingRepository) {
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.ratingRepository = ratingRepository;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o ID: " + userId));
    }

    public boolean canUserReviewABook(int userId, int bookId) {
        List<Book> borrowedBooks = loanRepository.findBooksByUserId(userId);
        for (Book borrowedBook: borrowedBooks) {
            if (bookId == borrowedBook.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean isBookCurrentlyBorrowedByUser(int userId, int bookId) {
        List<Loan> loans = loanRepository.findByUserId(userId);
        for (Loan loan: loans) {
            if (bookId == loan.getBook().getId() && !loan.isReturned()) {
                return true;
            }
        }
        return false;
    }

    public void changeFirstname(int userId, String newFirstname) {
        User user = getUserById(userId);
        user.setFirstname(newFirstname);

        userRepository.save(user);
    }

    public void changeLastname(int userId, String newLastname) {
        User user = getUserById(userId);
        user.setLastname(newLastname);

        userRepository.save(user);
    }

    public User getCurrentUser() {
        if (currentUser != null) {
            return getUserById(currentUser.getId());
        }
        return null;
    }

    public Slice<User> getPage(int pageNumber) {
        int pageSize = 10;

        return userRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }
}
