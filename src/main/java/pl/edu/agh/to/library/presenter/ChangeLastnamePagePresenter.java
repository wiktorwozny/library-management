package pl.edu.agh.to.library.presenter;

import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.library.UI.StageInitializer;
import pl.edu.agh.to.library.model.user.User;
import pl.edu.agh.to.library.model.user.UserService;
import javafx.scene.control.TextField;

@Component
public class ChangeLastnamePagePresenter {

    private final UserService userService;

    private final StageInitializer stageInitializer;

    @Autowired
    public ChangeLastnamePagePresenter(UserService userService, StageInitializer stageInitializer) {
        this.userService = userService;
        this.stageInitializer = stageInitializer;
    }

    @FXML
    private TextField lastnameTextField;

    @FXML
    private void initialize() {
        User currentUser = userService.getCurrentUser();
        lastnameTextField.setText(currentUser.getLastname());
    }

    @FXML
    private void handleChangeButtonClick() {
        String newLastname = lastnameTextField.getText();
        userService.changeLastname(userService.getCurrentUser().getId(), newLastname);
        stageInitializer.showUserProfilePage();
    }

    @FXML
    public void handleCancelButtonClick() {
        stageInitializer.showUserProfilePage();
    }
}
