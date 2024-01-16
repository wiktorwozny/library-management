package pl.edu.agh.to.library.configurator;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import pl.edu.agh.to.library.model.author.Author;
import pl.edu.agh.to.library.model.author.AuthorRepository;
import pl.edu.agh.to.library.model.book.Book;
import pl.edu.agh.to.library.model.book.BookRepository;
import pl.edu.agh.to.library.model.genre.Genre;
import pl.edu.agh.to.library.model.genre.GenreRepository;
import pl.edu.agh.to.library.model.role.Role;
import pl.edu.agh.to.library.model.role.RoleRepository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Configuration
public class Configurator {
    private final Environment env;

    @Autowired
    public Configurator(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:./library.sqlite");
        dataSource.setUsername("admin");
        dataSource.setPassword("admin");

        return dataSource;
    }

    @Bean
    public CommandLineRunner commandLineRunner(RoleRepository roleRepository, GenreRepository genreRepository, AuthorRepository authorRepository, BookRepository bookRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                Role user = new Role("user");
                Role admin = new Role("admin");
                Role librarian = new Role("librarian");
                roleRepository.saveAll(List.of(user, admin, librarian));
            }

            boolean createBooks =
                    genreRepository.count() < 4 || authorRepository.count() < 7 || bookRepository.count() < 100;

            if (createBooks) {
                bookRepository.deleteAll();
                genreRepository.deleteAll();
                authorRepository.deleteAll();

                Genre fantasy = new Genre("Fantasy");
                Genre thriller = new Genre("Thriller");
                Genre scienceFiction = new Genre("Science fiction");
                Genre romance = new Genre("Romance");

                List<Genre> genreList = List.of(scienceFiction, thriller, fantasy, romance);
                genreRepository.saveAll(genreList);


                Author author1 = new Author("Adam", "Mickiewicz", "Polska");
                Author author2 = new Author("Henryk", "Sienckiewicz", "Polska");
                Author author3 = new Author("Charles", "Dickens", "Anglia");
                Author author4 = new Author("Terry", "Pratchett", "Anglia");
                Author author5 = new Author("Albert", "Camus", "Algieria");
                Author author6 = new Author("Marek", "Aureliusz", "Włochy");
                Author author7 = new Author("Ernerst", "Hemingway", "Stany Zjednoczone");

                List<Author> authorList = List.of(author1, author2, author3, author4, author5, author6, author7);
                authorRepository.saveAll(authorList);


                LinkedList<Book> bookList = new LinkedList<>();
                List<String> bookCoverList = List.of(
                        "https://m.media-amazon.com/images/I/51SM+Bv+WeL.jpg",
                        "https://makeheadway.com/_next/image/?url=https%3A%2F%2Fstatic.get-headway.com%2FN5sFqrsfuCkZ9FBthW8Y-163a190e019857-en.jpg&w=750&q=75",
                        "https://m.media-amazon.com/images/W/MEDIAX_792452-T2/images/I/61WXhX3gqKL._AC_UF1000,1000_QL80_.jpg",
                        "https://cdn.kobo.com/book-images/1f7ac493-10e4-4d15-a0d8-90eb22f88a2c/1200/1200/False/for-whom-the-bell-tolls-39.jpg",
                        "https://i.ebayimg.com/images/g/hLsAAOSwA5dkpEi3/s-l1600.jpg"
                );
                List<String> languageList = List.of("Polski", "Angielski", "Francuski");

                for (int i = 0; i < 100; i++) {
                    bookList.add(new Book(
                            "Book " + i,
                            languageList.get(i % languageList.size()),
                            5,
                            authorList.get(i % authorList.size()),
                            Set.of(genreList.get(i % genreList.size())),
                            bookCoverList.get(i % bookCoverList.size())
                    ));
                }

                bookRepository.saveAll(bookList);
            }
        };
    }
}
