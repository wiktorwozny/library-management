package pl.edu.agh.to.library;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.edu.agh.to.library.model.author.Author;
import pl.edu.agh.to.library.model.author.AuthorRepository;
import pl.edu.agh.to.library.model.book.Book;
import pl.edu.agh.to.library.model.book.BookRepository;
import pl.edu.agh.to.library.model.book.BookService;
import pl.edu.agh.to.library.model.genre.Genre;
import pl.edu.agh.to.library.model.genre.GenreRepository;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookServiceTest {
    private BookService bookService;
    private BookRepository bookRepository;
    private GenreRepository genreRepository;
    private AuthorRepository authorRepository;

    @Autowired
    public BookServiceTest(BookService bookService, BookRepository bookRepository, GenreRepository genreRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.bookService = bookService;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Test
    @Transactional
    public void addNewBookTest() {
        //given
        Author author = new Author("Adam", "Mickiewicz", "Poland");
        Genre genre = new Genre("Romance");
        Book book = new Book("Dziady cz.3", "Polish", 1, author, Set.of(genre), "asdasda");

        //result
        authorRepository.save(author);
        genreRepository.save(genre);
        bookService.addBook(book);

        //then
        assertEquals(bookRepository.getReferenceById(book.getId()), book);
    }

    @Test
    @Transactional
    public void findBooksByFilterTest() {
        //given
        Author author1 = new Author("Bolesław", "Prus", "Polska");
        Author author2 = new Author("Stephen", "King", "Anglia");

        Genre genre1 = new Genre("Kryminał");
        Genre genre2 = new Genre("Horror");

        Book book1 = new Book("Lalka", "Polski", 1, author1, Set.of(genre1,genre2), "asdasd");
        Book book2 = new Book("Zielona mila", "Angielski", 2, author2, Set.of(genre2), "asdasdasd");

        //result
        authorRepository.save(author1);
        authorRepository.save(author2);

        genreRepository.save(genre1);
        genreRepository.save(genre2);

        bookRepository.save(book1);
        bookRepository.save(book2);

        //then
        assertEquals(bookService.findBooksByFilter(author1, null, null), List.of(book1));
        assertEquals(bookService.findBooksByFilter(null, "Angielski", null), List.of(book2));
        assertEquals(bookService.findBooksByFilter(null, null, genre2), List.of(book1,book2));
        assertEquals(bookService.findBooksByFilter(null, null, genre1), List.of(book1));
        assertEquals(bookService.findBooksByFilter(author1, "Polski", genre1), List.of(book1));
    }
}
