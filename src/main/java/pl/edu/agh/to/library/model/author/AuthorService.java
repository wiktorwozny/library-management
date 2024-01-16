package pl.edu.agh.to.library.model.author;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void addAuthor(Author author){
        authorRepository.save(author);
    }

    public Author getAuthorByName(String name){
        String[] names=name.split(" ");
        System.out.println(names[0]+" "+names[1]);
        return authorRepository.findByFirstnameAndLastname(names[0],names[1]);
    }

    public List<Author> getAllAuthors(){
        return authorRepository.findAll();
    }
}
