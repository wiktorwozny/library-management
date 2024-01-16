package pl.edu.agh.to.library.presenter;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.Chart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.library.UI.StageInitializer;
import pl.edu.agh.to.library.auxiliary.Charts;
import pl.edu.agh.to.library.model.loan.Loan;
import pl.edu.agh.to.library.model.loan.LoanService;
import pl.edu.agh.to.library.model.user.User;
import pl.edu.agh.to.library.model.user.UserService;

@Component
public class LibrarianPagePresenter {

    private final LoanService loanService;

    private final UserService userService;

    private final Charts charts;

    private final StageInitializer stageInitializer;

    private IntegerProperty pageNumber = new SimpleIntegerProperty(0);

    private Slice<Loan> currentSlice = null;

    private SimpleIntegerProperty sliceChange = new SimpleIntegerProperty(0);

    private User specificUser = null;

    private boolean showUnreturned = false;

    @Autowired
    public LibrarianPagePresenter(LoanService loanService, UserService userService, StageInitializer stageInitializer, Charts charts) {
        this.loanService = loanService;
        this.userService = userService;
        this.stageInitializer = stageInitializer;
        this.charts = charts;
    }

    @FXML
    private TextField emailTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private CheckBox unreturnedCheckBox;

    @FXML
    private TableView<Loan> loansTableView;

    @FXML
    TableColumn<Loan, String> emailColumn;

    @FXML
    TableColumn<Loan, String> firstnameColumn;

    @FXML
    TableColumn<Loan, String> lastnameColumn;

    @FXML
    TableColumn<Loan, String> titleColumn;

    @FXML
    TableColumn<Loan, String> whenColumn;

    @FXML
    TableColumn<Loan, String> untilColumn;

    @FXML
    TableColumn<Loan, String> returnedColumn;

    @FXML
    private Button leftButton;

    @FXML
    private Button rightButton;

    @FXML
    private Label pageCounter;

    @FXML
    private HBox chartsBox;

    @FXML
    private void initialize() {
        getLoanPage();

        loansTableView.setStyle("-fx-font-size: 15px;");

        leftButton.disableProperty().bind(Bindings.equal(pageNumber, 0));
        rightButton.disableProperty().bind(Bindings.createBooleanBinding(() -> !currentSlice.hasNext(), sliceChange));

        chartsBox.getChildren().addAll(charts.numberOfLoansChart(), charts.totalAuthorChart(), charts.totalGenreChart());
    }

    private void getLoanPage() {
        String userEmail = (specificUser != null) ? specificUser.getEmail() : null;

        if (showUnreturned) {
            currentSlice = (userEmail != null) ?
                    loanService.getUnreturnedLoansForSpecificUserPage(userEmail, pageNumber.get()) :
                    loanService.getUnreturnedLoansPage(pageNumber.get());
        } else {
            currentSlice = (userEmail != null) ?
                    loanService.getAllLoansForSpecificUserPage(userEmail, pageNumber.get()) :
                    loanService.getPage(pageNumber.get());
        }

        sliceChange.set(sliceChange.get() + 1);
        pageCounter.textProperty().set(String.valueOf(pageNumber.get()));
        loansTableView.getItems().clear();
        renderLoansTable();
    }

    @FXML
    private void renderLoansTable() {
        ObservableList<Loan> data = FXCollections.observableArrayList();

        emailColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getUserEmail()));
        firstnameColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getUserFirstname()));
        lastnameColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getUserLastname()));
        titleColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getBookTitle()));
        whenColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getLoanDateToString()));
        untilColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getReturnDateToString()));
        returnedColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().isReturnedToString()));

        for (Loan loan: currentSlice) {
            data.add(loan);
        }

        loansTableView.setItems(data);
    }

    @FXML
    private void searchLoans() {
        if (!emailTextField.getText().isEmpty()) {
            specificUser = userService.getUserByEmail(emailTextField.getText());
            if (specificUser == null) {
                errorLabel.setVisible(true);
                resetFilters();
                return;
            }
        }

        errorLabel.setVisible(false);
        showUnreturned = unreturnedCheckBox.isSelected();
        pageNumber = new SimpleIntegerProperty(0);

        getLoanPage();
    }

    @FXML
    private void resetFilters() {
        specificUser = null;
        showUnreturned = false;
        pageNumber = new SimpleIntegerProperty(0);

        emailTextField.setText("");
        unreturnedCheckBox.setSelected(false);

        getLoanPage();
    }

    @FXML
    private void handleLeftClick() {
        pageNumber.set(pageNumber.get() - 1);
        getLoanPage();
    }

    @FXML
    private void handleRightClick() {
        pageNumber.set(pageNumber.get() + 1);
        getLoanPage();
    }

    @FXML
    private void handleBackButtonClick() {
        stageInitializer.showMainPage();
    }

}
