package pl.edu.agh.to.library.model.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
    List<Rating> findByBookId(int bookId);

    List<Rating> findByUserId(int userId);
}
