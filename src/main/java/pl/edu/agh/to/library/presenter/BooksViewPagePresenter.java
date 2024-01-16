package pl.edu.agh.to.library.presenter;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.library.UI.StageInitializer;
import pl.edu.agh.to.library.model.author.Author;
import pl.edu.agh.to.library.model.author.AuthorService;
import pl.edu.agh.to.library.model.book.Book;
import pl.edu.agh.to.library.model.book.BookService;
import pl.edu.agh.to.library.model.genre.Genre;
import pl.edu.agh.to.library.model.genre.GenreService;

import java.util.List;

@Component
public class BooksViewPagePresenter {

    private BookService bookService;

    private AuthorService authorService;

    private GenreService genreService;

    private StageInitializer stageInitializer;

    private List<Book> bookListToDisplay;
    private SimpleIntegerProperty currPage = new SimpleIntegerProperty(0);
    private SimpleBooleanProperty isNotFiltered = new SimpleBooleanProperty(true);

    @Autowired
    public BooksViewPagePresenter(BookService bookService, AuthorService authorService, GenreService genreService, StageInitializer stageInitializer) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.stageInitializer = stageInitializer;
    }

    @FXML
    private ComboBox<Author> authorComboBox;

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private ComboBox<Genre> genreComboBox;

    @FXML
    private ListView<Book> booksListView;

    @FXML
    private ComboBox<String> sortingComboBox;

    @FXML
    private TilePane booksList;

    @FXML
    private Label pageCounter;

    @FXML
    private Button leftButton;

    @FXML
    private Button rightButton;

    @FXML
    private HBox hBox;

    private void updateListOfBooks() {
        booksList.getChildren().clear();
        for (Book book : bookListToDisplay) {
            ImageView imageView = new ImageView(new Image(book.getImageUrl()));
            imageView.setFitWidth(180);
            imageView.setFitHeight(300);
            imageView.setPreserveRatio(false);

            Label label = new Label(book.getTitle() + "\n" + book.getAuthor());
            label.fontProperty().set(new Font(20));
            VBox vBox = new javafx.scene.layout.VBox(imageView, label);
            TilePane.setMargin(vBox, new Insets(10, 10, 0, 10));
            vBox.setOnMouseClicked(event -> {
                bookService.setCurrentBook(book);
                stageInitializer.showSingleBookPage();
            });
            if(bookService.isRecommended(book.getId())){
                vBox.setBackground(new Background(new BackgroundFill(Color.INDIANRED, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
            }
            booksList.getChildren().add(vBox);
        }
    }

    @FXML
    private void initialize() {
        bookListToDisplay = bookService.getPage(currPage.get());
        updateListOfBooks();
        booksList.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
        booksList.setHgap(10);
        languageComboBox.getItems().setAll(bookService.getAllBooks().stream().map(Book::getLanguage).distinct().toList());
        authorComboBox.getItems().setAll(authorService.getAllAuthors());
        genreComboBox.getItems().setAll(genreService.getAllGenres());
        authorComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Author author) {
                return author.getName();
            }

            @Override
            public Author fromString(String string) {
                return null;
            }
        });
        genreComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Genre genre) {
                return genre.getGenreName();
            }

            @Override
            public Genre fromString(String string) {
                return null;
            }
        });
        sortingComboBox.setOnAction(event -> {
            String selectedOption = sortingComboBox.getValue();
            switch (selectedOption) {
                case "Ocena malejąco" -> sortingBooks(true);
                case "Ocena rosnąco" -> sortingBooks(false);
            }
        });
        pageCounter.textProperty().set(String.valueOf(currPage.get() + 1));

        leftButton.disableProperty().bind(Bindings.equal(0, currPage));
        rightButton.disableProperty().bind(Bindings.equal(bookService.getAllPages() - 1, currPage));

        hBox.visibleProperty().bind(isNotFiltered);
    }

    @FXML
    private void searchBooks() {
        Author selectedAuthor = authorComboBox.getValue();
        String selectedLanguage = languageComboBox.getValue();
        Genre selectedGenre = genreComboBox.getValue();

        bookListToDisplay = bookService.findBooksByFilter(selectedAuthor, selectedLanguage, selectedGenre);
        updateListOfBooks();
        isNotFiltered.set(false);
    }

    @FXML
    private void resetFilters() {
        authorComboBox.setValue(null);
        languageComboBox.setValue(null);
        genreComboBox.setValue(null);
        bookListToDisplay = bookService.getPage(currPage.get());
        updateListOfBooks();
        isNotFiltered.set(true);
    }

    @FXML
    private void sortingBooks(boolean isDescending) {
        bookListToDisplay = bookListToDisplay.stream().sorted((book1, book2) -> Double.compare(bookService.getBookRating(book1.getId()), bookService.getBookRating(book2.getId()))).toList();
        if (isDescending) {
            bookListToDisplay = bookListToDisplay.reversed();
        }
        updateListOfBooks();
    }

    @FXML
    private void handleBackButtonClick() {
        stageInitializer.showMainPage();
    }

    @FXML
    private void handleLeftClick() {
        currPage.setValue(currPage.getValue() - 1);
        bookListToDisplay = bookService.getPage(currPage.get());
        updateListOfBooks();
        pageCounter.textProperty().set(String.valueOf(currPage.get() + 1));
        sortingComboBox.setValue("Wybierz rodzaj sortowania");
    }

    @FXML
    private void handleRightClick() {
        currPage.setValue(currPage.getValue() + 1);
        bookListToDisplay = bookService.getPage(currPage.get());
        updateListOfBooks();
        pageCounter.textProperty().set(String.valueOf(currPage.get() + 1));
        sortingComboBox.setValue("Wybierz rodzaj sortowania");
    }

}
