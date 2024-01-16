package pl.edu.agh.to.library.presenter;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.library.UI.StageInitializer;
import pl.edu.agh.to.library.model.author.Author;
import pl.edu.agh.to.library.model.author.AuthorService;
import pl.edu.agh.to.library.model.book.Book;
import pl.edu.agh.to.library.model.book.BookService;
import pl.edu.agh.to.library.model.genre.Genre;
import pl.edu.agh.to.library.model.genre.GenreService;

import java.util.*;

@Component
public class AddBookPagePresenter {
    private BookService bookService;
    private AuthorService authorService;
    private GenreService genreService;
    private final StageInitializer stageInitializer;

    @Autowired
    public AddBookPagePresenter(
            BookService bookService, GenreService genreService, AuthorService authorService, StageInitializer stageInitializer
    ){
        this.bookService = bookService;
        this.genreService = genreService;
        this.authorService = authorService;
        this.stageInitializer = stageInitializer;
    }

    @FXML
    private TextField titleField;

    @FXML
    private TextField languageField;

    @FXML
    private TextField copiesField;

    @FXML
    private ListView<String> genreListView;

    @FXML
    private ComboBox<Author> authorField;

    @FXML
    private Label resultText;

    @FXML
    private Button addButton;

    @FXML
    private TextField imageUrlField;



    @FXML
    public void initialize() {
        genreListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        List<String> genreNames = genreService.getAllGenres().stream().map(Genre::getGenreName).toList();
        genreListView.getItems().addAll(genreNames);

        authorField.getItems().addAll(authorService.getAllAuthors());

        addButton.disableProperty().bind(
                Bindings.isEmpty(titleField.textProperty())
                        .or(Bindings.isEmpty(languageField.textProperty()))
                        .or(Bindings.isEmpty(genreListView.getSelectionModel().getSelectedItems()))
                        .or(Bindings.isEmpty(copiesField.textProperty()))
                        .or(Bindings.isEmpty(imageUrlField.textProperty()))
        );

        copiesField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.isContentChange()) {
                if (!change.getControlNewText().matches("\\d*")) {
                    return null;
                }
            }
            return change;
        }));

        authorField.getSelectionModel().selectFirst();
        authorField.setConverter(new StringConverter<Author>() {
            @Override
            public String toString(Author author) {
                return author.getName();
            }

            @Override
            public Author fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    public void addBookHandler() {
        List<String> selectedItems = genreListView.getSelectionModel().getSelectedItems();
        Set<Genre> selectedGenres = new HashSet<>();
        for (String selectedItem : selectedItems) {
            selectedGenres.add(genreService.getByName(selectedItem));
        }
        Author author = authorField.getValue();
        String title = titleField.getText();
        String language = languageField.getText();
        int copies = Integer.parseInt(copiesField.getText());
        String imageUrl=imageUrlField.getText();
        try{
            bookService.addBook(new Book(title,language,copies,author,selectedGenres,imageUrl));
            titleField.textProperty().set("");
            languageField.textProperty().set("");
            copiesField.textProperty().set("");
            imageUrlField.textProperty().set("");
            genreListView.getSelectionModel().clearSelection();
            authorField.getSelectionModel().selectFirst();
            resultText.textProperty().set("Książka została dodana");
        }catch(DataAccessException e){
            resultText.textProperty().set("Nie udało się dodać książki");
        }
    }

    @FXML
    private void handleBackButtonClick() {
        stageInitializer.showMainPage();
    }
}
