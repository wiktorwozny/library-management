package pl.edu.agh.to.library.presenter;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.library.UI.StageInitializer;
import pl.edu.agh.to.library.auxiliary.Charts;
import pl.edu.agh.to.library.auxiliary.LoanDetails;
import pl.edu.agh.to.library.model.loan.Loan;
import pl.edu.agh.to.library.model.loan.LoanService;
import pl.edu.agh.to.library.model.user.User;
import pl.edu.agh.to.library.model.user.UserService;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserProfilePagePresenter {

    private UserService userService;
    private LoanService loanService;
    private StageInitializer stageInitializer;
    private Charts charts;

    @Autowired
    public UserProfilePagePresenter(UserService userService, LoanService loanService, StageInitializer stageInitializer, Charts charts) {
        this.userService = userService;
        this.loanService = loanService;
        this.stageInitializer = stageInitializer;
        this.charts = charts;
    }

    @FXML
    private ImageView avatarImageView;

    @FXML
    private Label emailLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private Label firstnameLabel;

    @FXML
    private Label lastnameLabel;

    @FXML
    private TableView<LoanDetails> loanHistoryTableView;

    @FXML
    private TableColumn<LoanDetails, String> titleColumn;

    @FXML
    private TableColumn<LoanDetails, String> whenColumn;

    @FXML
    private TableColumn<LoanDetails, String> untilColumn;

    @FXML
    private TableColumn<LoanDetails, String> returnedColumn;

    @FXML
    private HBox chartsBox;

    @FXML
    private void initialize() {
        User currentUser = userService.getCurrentUser();

        setAvatarImageView();

        emailLabel.setText(currentUser.getEmail());
        roleLabel.setText(currentUser.getRole().getRoleName());
        firstnameLabel.setText(currentUser.getFirstname());
        lastnameLabel.setText(currentUser.getLastname());

        renderLoanHistoryTable(currentUser);

        chartsBox.getChildren().addAll(
                charts.userActivityChart(currentUser),
                charts.userAuthorChart(currentUser),
                charts.userGenreChart(currentUser)
        );
    }

    @FXML
    private void setAvatarImageView() {
        Image image = new Image("file:" + "src/main/resources/images/avatar.jpg");
        avatarImageView.setImage(image);
    }

    @FXML
    private void renderLoanHistoryTable(User currentUser) {
        List<Loan> usersLoans = loanService.getLoansByUserId(currentUser.getId());

        ObservableList<LoanDetails> data = FXCollections.observableArrayList();

        titleColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitle()));
        whenColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getWhenDate()));
        untilColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getUntilDate()));
        returnedColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().isReturned()));

        for (Loan loan: usersLoans) {
            data.add(new LoanDetails(loan));
        }

        loanHistoryTableView.setItems(data);
    }

    @FXML
    private void handleFirstnameButtonClick() {
        stageInitializer.showChangeFirstnamePage();
    }

    @FXML
    private void handleLastnameButtonClick() {
        stageInitializer.showChangeLastnamePage();
    }

    @FXML
    public void handleBackButtonClick() {
        stageInitializer.showMainPage();
    }

    public void handleNotificationSettingsClick() {
        stageInitializer.showNotificationSettings();
    }
}
