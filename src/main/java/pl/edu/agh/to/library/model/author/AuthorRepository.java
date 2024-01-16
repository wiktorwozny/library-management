package pl.edu.agh.to.library.model.author;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Author findByFirstnameAndLastname(String firstname,String lastname);
}
