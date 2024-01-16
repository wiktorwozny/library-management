package pl.edu.agh.to.library.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.library.UI.StageInitializer;
import pl.edu.agh.to.library.auxiliary.Charts;
import pl.edu.agh.to.library.model.book.Book;
import pl.edu.agh.to.library.model.book.BookService;
import pl.edu.agh.to.library.model.loan.LoanService;
import pl.edu.agh.to.library.model.rating.Rating;
import javafx.scene.control.Label;
import pl.edu.agh.to.library.model.user.User;
import pl.edu.agh.to.library.model.user.UserService;

import java.util.List;

@Component
public class SingleBookPagePresenter {

    private final BookService bookService;

    private final UserService userService;

    private final LoanService loanService;

    private final StageInitializer stageInitializer;

    private final Charts charts;

    @Autowired
    public SingleBookPagePresenter(BookService bookService, UserService userService, LoanService loanService, StageInitializer stageInitializer, Charts charts) {
        this.bookService = bookService;
        this.userService = userService;
        this.loanService = loanService;
        this.stageInitializer = stageInitializer;
        this.charts = charts;
    }

    @FXML
    private ImageView coverUrlImageView;

    @FXML
    private Label titleLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Label genresLabel;

    @FXML
    private Label languageLabel;

    @FXML
    private Label copiesLeftLabel;

    @FXML
    private Label averageRatingLabel;

    @FXML
    private Button borrowButton;

    @FXML
    private Button returnButton;

    @FXML
    private VBox reviewsVbox;

    @FXML
    private VBox chartsBox;

    @FXML
    private Button addReviewButton;

    @FXML
    private void initialize() {
        Book currentBook = bookService.getCurrentBook();
        User currentUser = userService.getCurrentUser();
        String currentUserRole = getCredentials(currentUser);

        setCoverUrlImageView(currentBook.getImageUrl());
        setBookInformationLabels(currentBook);
        createRatingsLabels();

        if (currentUserRole.equals("guest") || currentBook.getCopiesLeft() <= 0) {
            borrowButton.setVisible(false);
        }

        if (currentUserRole.equals("guest") || !userService.isBookCurrentlyBorrowedByUser(currentUser.getId(), currentBook.getId())) {
            returnButton.setVisible(false);
        }

        if (currentUserRole.equals("guest") || !userService.canUserReviewABook(currentUser.getId(), currentBook.getId())) {
            addReviewButton.setVisible(false);
        }

        chartsBox.getChildren().add(charts.bookPopularityChart(currentBook));
    }

    @FXML
    private void setCoverUrlImageView(String imageUrl) {
        Image image = new Image(imageUrl);
        coverUrlImageView.setImage(image);
    }

    @FXML
    private void setBookInformationLabels(Book currentBook) {
        titleLabel.setText(currentBook.getTitle());
        authorLabel.setText(currentBook.getAuthor().toString());
        genresLabel.setText(currentBook.getGenresAsString());
        languageLabel.setText(currentBook.getLanguage());
        copiesLeftLabel.setText("pozostało " + currentBook.getCopiesLeft() + " kopii");
        averageRatingLabel.setText("ocena czytelników: " + currentBook.calculateAverageRating());
    }

    @FXML
    private void createRatingsLabels() {
        List<Rating> ratingList = bookService.getAllReviewsForBook(bookService.getCurrentBook().getId());
        for (int i = 0; i < ratingList.size(); i++) {
            Rating rating = ratingList.get(i);
            Label ratingUserNameLabel = new Label(rating.getUserName() + ": " + rating.getRatingValue());
            Label reviewTextValueLabel = new Label(rating.getReviewText());

            ratingUserNameLabel.setPadding(new Insets(0, 0, 0, 10));
            reviewTextValueLabel.setPadding(new Insets(5, 0, 10, 10));

            reviewsVbox.getChildren().addAll(ratingUserNameLabel, reviewTextValueLabel);
        }
    }

    private String getCredentials(User user) {
        if (user == null) {
            return "guest";
        } else {
            return user.getRole().getRoleName();
        }
    }

    @FXML
    public void handleBorrowButtonClick(ActionEvent actionEvent) {
        Book currentBook = bookService.getCurrentBook();
        User currentUser = userService.getCurrentUser();
        loanService.borrowBook(currentBook.getId(), currentUser);
        stageInitializer.showSingleBookPage();
    }

    @FXML
    public void handleReturnButtonClick(ActionEvent actionEvent) {
        Book currentBook = bookService.getCurrentBook();
        User currentUser = userService.getCurrentUser();
        loanService.returnBook(currentBook.getId(), currentUser);
        stageInitializer.showSingleBookPage();
    }

    @FXML
    public void handleAddReviewButton() {
        stageInitializer.showAddBookReviewPage();
    }

    @FXML
    public void handleBackButtonClick() {
        stageInitializer.showBooksViewPage();
    }
}
