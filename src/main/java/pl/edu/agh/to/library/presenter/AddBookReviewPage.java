package pl.edu.agh.to.library.presenter;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.library.UI.StageInitializer;
import pl.edu.agh.to.library.model.book.Book;
import pl.edu.agh.to.library.model.book.BookService;
import pl.edu.agh.to.library.model.rating.Rating;
import pl.edu.agh.to.library.model.rating.RatingService;
import pl.edu.agh.to.library.model.user.User;
import pl.edu.agh.to.library.model.user.UserService;

@Component
public class AddBookReviewPage {

    private final BookService bookService;

    private final UserService userService;

    private final RatingService ratingService;

    private final StageInitializer stageInitializer;

    @Autowired
    public AddBookReviewPage(BookService bookService, UserService userService, RatingService ratingService, StageInitializer stageInitializer) {
        this.bookService = bookService;
        this.userService = userService;
        this.ratingService = ratingService;
        this.stageInitializer = stageInitializer;
    }

    @FXML
    private ComboBox<String> ratingValueComboBox;

    @FXML
    private TextField reviewTextField;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void initialize() {
        addButton.disableProperty().bind(Bindings.isEmpty(reviewTextField.textProperty()));
        ratingValueComboBox.setValue("3");
    }

    @FXML
    public void handleAddButtonClick() {
        Book currentBook = bookService.getCurrentBook();
        User currentUser = userService.getCurrentUser();

        int ratingValue = Integer.parseInt(ratingValueComboBox.getValue());
        String reviewText = reviewTextField.getText();

        Rating newRating = new Rating(currentBook, currentUser, ratingValue, reviewText);
        ratingService.addRatingToBook(currentBook.getId(), newRating);
        stageInitializer.showSingleBookPage();
    }

    @FXML
    public void handleCancelButtonClick() {
        stageInitializer.showSingleBookPage();
    }
}
