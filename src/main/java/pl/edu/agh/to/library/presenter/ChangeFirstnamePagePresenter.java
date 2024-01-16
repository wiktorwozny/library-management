package pl.edu.agh.to.library.presenter;

import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.library.UI.StageInitializer;
import pl.edu.agh.to.library.model.user.User;
import pl.edu.agh.to.library.model.user.UserService;
import javafx.scene.control.TextField;

@Component
public class ChangeFirstnamePagePresenter {

    private final UserService userService;

    private final StageInitializer stageInitializer;

    @Autowired
    public ChangeFirstnamePagePresenter(UserService userService, StageInitializer stageInitializer) {
        this.userService = userService;
        this.stageInitializer = stageInitializer;
    }

    @FXML
    private TextField firstnameTextField;

    @FXML
    private void initialize() {
        User currentUser = userService.getCurrentUser();
        firstnameTextField.setText(currentUser.getFirstname());
    }

    @FXML
    private void handleChangeButtonClick() {
        String newFirstname = firstnameTextField.getText();
        userService.changeFirstname(userService.getCurrentUser().getId(), newFirstname);
        stageInitializer.showUserProfilePage();
    }

    @FXML
    public void handleCancelButtonClick() {
        stageInitializer.showUserProfilePage();
    }
}
