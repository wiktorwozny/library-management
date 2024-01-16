package pl.edu.agh.to.library.model.genre;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Genre getByName(String name){
        return genreRepository.findByGenreName(name);
    }

    public List<Genre> getAllGenres(){
        return genreRepository.findAll();
    }
}
