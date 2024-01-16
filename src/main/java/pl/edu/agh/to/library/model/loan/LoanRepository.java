package pl.edu.agh.to.library.model.loan;
import pl.edu.agh.to.library.model.book.Book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {
    @Query("select e.book from Loan e where e.user.id = :userId")
    List<Book> findBooksByUserId(@Param("userId") int userId);

    @Query("SELECT l FROM Loan l WHERE l.user.id = :userId AND l.book.id = :bookId AND l.returnDate = (SELECT MIN(l2.returnDate) FROM Loan l2 WHERE l2.user.id = :userId AND l2.book.id = :bookId AND l2.isReturned = false)")
    Loan findOldestBookLoanByUserId(@Param("userId") int userId, @Param("bookId") int bookId);

    List<Loan> findByUserId(int userId);

    List<Loan> findByBookId(int bookId);

    @Query("SELECT l FROM Loan l WHERE l.isReturned = false")
    Slice<Loan> findUnreturnedLoans(Pageable pageable);

    @Query("SELECT l FROM Loan l WHERE l.user.id = :userId")
    Slice<Loan> findAllLoansForSpecificUser(@Param("userId") int userId, Pageable pageable);

    @Query("SELECT l FROM Loan l WHERE l.isReturned = false AND l.user.id = :userId")
    Slice<Loan> findUnreturnedLoansForSpecificUser(@Param("userId") int userId, Pageable pageable);
}
