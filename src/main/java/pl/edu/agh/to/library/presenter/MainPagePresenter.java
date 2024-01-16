package pl.edu.agh.to.library.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.library.UI.StageInitializer;
import pl.edu.agh.to.library.model.user.User;
import pl.edu.agh.to.library.model.user.UserService;

@Component
public class MainPagePresenter {

    private final UserService userService;

    private final StageInitializer stageInitializer;

    @FXML
    private Label userLabel;

    @FXML
    private Button logoutButton;

    @FXML
    private Button loginButton;

    @FXML
    private Button adminPageButton;

    @FXML
    private Button userProfilePageButton;

    @FXML
    private Button librarianPageButton;

    @FXML
    private Button addAuthorButton;

    @FXML
    private Button addBookButton;

    @Autowired
    public MainPagePresenter(UserService userService, StageInitializer stageInitializer) {
        this.userService = userService;
        this.stageInitializer = stageInitializer;
    }

    @FXML
    private void initialize() {
        User user = userService.getCurrentUser();
        String role = getCredentials(user);

        if (role.equals("guest")) {
            userLabel.textProperty().set("Nie zalogowano");
            loginButton.setVisible(true);
        } else {
            userLabel.textProperty().set(user.getEmail() + " (" + role + ")");
            logoutButton.setVisible(true);
            userProfilePageButton.setVisible(true);
            if (!role.equals("user")) {
                addAuthorButton.setVisible(true);
                addBookButton.setVisible(true);
                librarianPageButton.setVisible(true);
                if (role.equals("admin")) {
                    adminPageButton.setVisible(true);
                }
            }
        }
    }

    @FXML
    private void handleLoginClick() {
        stageInitializer.showLoginPage();
    }

    @FXML
    private void handleLogoutClick() {
        userService.setCurrentUser(null);
        stageInitializer.showMainPage();
    }

    @FXML
    private void handleAdminPageClick() {
        stageInitializer.showAdminPage();
    }

    @FXML
    private void handleBooksListClick() {
        stageInitializer.showBooksViewPage();
    }

    @FXML
    private void handleLibrarianPageClick() {
        stageInitializer.showLibrarianPage();
    }

    @FXML
    private void handleAddAuthorClick() {
        stageInitializer.showAddAuthorPage();
    }

    @FXML
    private void handleAddBookClick() {
        stageInitializer.showAddBookPage();
    }

    @FXML
    public void handleUserPageButtonClick() {
        stageInitializer.showUserProfilePage();
    }

    private String getCredentials(User user) {
        if (user == null) {
            return "guest";
        } else {
            return user.getRole().getRoleName();
        }
    }
}
