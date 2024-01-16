package pl.edu.agh.to.library.presenter;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.library.UI.StageInitializer;
import pl.edu.agh.to.library.model.notification.Notification;
import pl.edu.agh.to.library.model.notification.NotificationService;
import pl.edu.agh.to.library.model.role.Role;
import pl.edu.agh.to.library.model.role.RoleService;
import pl.edu.agh.to.library.model.user.User;
import pl.edu.agh.to.library.model.user.UserService;
import org.apache.commons.validator.routines.EmailValidator;

@Component
public class AddUserPagePresenter {
    private final UserService userService;

    private final RoleService roleService;

    private final NotificationService notificationService;

    private final StageInitializer stageInitializer;

    @Autowired
    public AddUserPagePresenter(UserService userService, RoleService roleService, StageInitializer stageInitializer, NotificationService notificationService) {
        this.userService = userService;
        this.roleService = roleService;
        this.stageInitializer = stageInitializer;
        this.notificationService = notificationService;
    }

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private Label resultText;

    @FXML
    private Button addButton;

    @FXML
    private ComboBox<String> RoleField;

    @FXML
    private void initialize() {
        BooleanBinding emailInvalid = Bindings.createBooleanBinding(() -> !validateEmail(), emailField.textProperty());

        addButton.disableProperty().bind(
                Bindings.isEmpty(firstNameField.textProperty())
                        .or(Bindings.isEmpty(lastNameField.textProperty()))
                        .or(emailInvalid)
        );
        RoleField.setValue("Użytkownik");
    }

    @FXML
    private void handleButtonClick() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        Role role=roleService.getByName("user");
        switch(RoleField.getValue()){
            case "Użytkownik"->role = roleService.getByName("user");
            case "Admin"->role = roleService.getByName("admin");
            case "Bibliotekarz"->role = roleService.getByName("librarian");
        }

        Notification notification = new Notification();
        User newUser = new User(firstName, lastName, email, role, notification);
        notification.setUser(newUser);
        try {
            notificationService.save(notification);
            userService.saveUser(newUser);

            resultText.textProperty().set("Użytkownik dodany");

            firstNameField.setText("");
            lastNameField.setText("");
            emailField.setText("");
            RoleField.setValue("Użytkownik");
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            resultText.textProperty().set("Nie można dodać użytkownika");
        }
        resultText.styleProperty().set("");
    }

    private boolean validateEmail() {
        return EmailValidator.getInstance().isValid(emailField.getText());
    }

    @FXML
    private void handleBackButtonClick() {
        stageInitializer.showMainPage();
    }
}
