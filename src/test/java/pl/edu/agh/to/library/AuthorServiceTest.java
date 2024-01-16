package pl.edu.agh.to.library;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.edu.agh.to.library.model.author.Author;
import pl.edu.agh.to.library.model.author.AuthorRepository;
import pl.edu.agh.to.library.model.author.AuthorService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AuthorServiceTest {
    private AuthorService authorService;
    private AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceTest(AuthorService authorService, AuthorRepository authorRepository) {
        this.authorService = authorService;
        this.authorRepository = authorRepository;
    }

    @Test
    @Transactional
    public void addNewAuthorTest() {
        //given
        Author author = new Author("Adam", "Mickiewicz", "Poland");

        //result
        authorService.addAuthor(author);

        //then
        assertEquals(authorRepository.getReferenceById(author.getId()), author);
    }
}
