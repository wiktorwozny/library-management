package pl.edu.agh.to.library.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.library.UI.StageInitializer;
import pl.edu.agh.to.library.model.user.User;
import pl.edu.agh.to.library.model.user.UserService;

@Component
public class LoginPresenter {
    private final UserService userService;

    private final StageInitializer stageInitializer;

    @FXML
    private TextField emailField;

    @FXML
    private Label resultText;

    @Autowired
    public LoginPresenter(UserService userService, StageInitializer stageInitializer) {
        this.userService = userService;
        this.stageInitializer = stageInitializer;
    }

    @FXML
    private void handleButtonClick() {
        String email = emailField.getText();

        try {
            User user = userService.getUserByEmail(email);
            if (user == null) {
                throw new NullPointerException();
            }
            userService.setCurrentUser(user);
            System.out.println(user.getId());
            resultText.textProperty().set("Zalogowano");
            stageInitializer.showMainPage();
        } catch (DataAccessException | NullPointerException e) {
            resultText.textProperty().set("Nie udało się zalogować");
        }
        resultText.styleProperty().set("");
    }

    @FXML
    private void handleBackButtonClick() {
        stageInitializer.showMainPage();
    }
}
