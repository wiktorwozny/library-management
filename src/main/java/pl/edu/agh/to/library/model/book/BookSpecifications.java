package pl.edu.agh.to.library.model.book;

import org.springframework.data.jpa.domain.Specification;
import pl.edu.agh.to.library.model.author.Author;
import pl.edu.agh.to.library.model.genre.Genre;

public class BookSpecifications {
    public static Specification<Book> hasAuthor(Author author) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("author"), author);
    }

    public static Specification<Book> hasLanguage(String language) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("language"), language);
    }

    public static Specification<Book> hasGenre(Genre genre) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isMember(genre, root.get("genres"));
    }
}
