package pl.edu.agh.to.library.presenter;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.library.UI.StageInitializer;
import pl.edu.agh.to.library.model.role.Role;
import pl.edu.agh.to.library.model.role.RoleService;
import pl.edu.agh.to.library.model.user.User;
import pl.edu.agh.to.library.model.user.UserService;


@Component
public class AdminPagePresenter {

    private final UserService userService;

    private final RoleService roleService;

    private final StageInitializer stageInitializer;

    private IntegerProperty pageNumber = new SimpleIntegerProperty(0);

    private Slice<User> currentSlice = null;

    private SimpleIntegerProperty sliceChange = new SimpleIntegerProperty(0);

    @FXML
    private Label pageCounter;

    @FXML
    private Button leftButton;

    @FXML
    private Button rightButton;

    @FXML
    private VBox userList;

    @Autowired
    public AdminPagePresenter(StageInitializer stageInitializer, UserService userService, RoleService roleService) {
        this.stageInitializer = stageInitializer;
        this.userService = userService;
        this.roleService = roleService;
    }

    @FXML
    private void initialize() {
        getUserPage();

        leftButton.disableProperty().bind(Bindings.equal(0, new SimpleIntegerProperty(0)));
        rightButton.disableProperty().bind(Bindings.createBooleanBinding(() -> !currentSlice.hasNext(), sliceChange));
    }

    private void getUserPage() {
        currentSlice = userService.getPage(pageNumber.get());
        pageCounter.textProperty().set(String.valueOf(pageNumber));
        sliceChange.set(sliceChange.get() + 1);
        userList.getChildren().clear();
        currentSlice.forEach(this::renderUser);
    }

    private void renderUser(User user) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);

        Label label = new Label(user.toString());

        String[] roles = {"Użytkownik", "Bibliotekarz", "Admin"};
        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(roles));
        comboBox.valueProperty().set(switch (user.getRole().getRoleName()) {
            case "user" -> "Użytkownik";
            case "admin" -> "Admin";
            case "librarian" -> "Bibliotekarz";
            default -> throw new IllegalStateException("Unexpected value: " + user.getRole().getRoleName());
        });
        EventHandler<ActionEvent> roleEvent = event -> {
            String selected = comboBox.getValue();
            Role role = roleService.getByName("user");
            switch(selected){
                case "Użytkownik" -> role = roleService.getByName("user");
                case "Admin" -> role = roleService.getByName("admin");
                case "Bibliotekarz" -> role = roleService.getByName("librarian");
            }
            user.setRole(role);
            userService.saveUser(user);
        };
        comboBox.setOnAction(roleEvent);

        Button banButton = new Button("Ban");
        EventHandler<ActionEvent> banEvent = event -> {
            userService.deleteUser(user);
            userList.getChildren().remove(hBox);
        };
        banButton.setOnAction(banEvent);

        hBox.getChildren().addAll(label, comboBox, banButton);
        userList.getChildren().add(hBox);
    }

    @FXML
    private void handleLeftClick() {
        pageNumber.set(pageNumber.get() - 1);
        getUserPage();
    }

    @FXML
    private void handleRightClick() {
        pageNumber.set(pageNumber.get() + 1);
        getUserPage();
    }

    @FXML
    private void handleAddUserClick() {
        stageInitializer.showAddUserPage();
    }

    @FXML
    private void handleBackButtonClick() {
        stageInitializer.showMainPage();
    }
}
