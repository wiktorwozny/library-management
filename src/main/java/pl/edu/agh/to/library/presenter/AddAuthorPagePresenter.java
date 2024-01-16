package pl.edu.agh.to.library.presenter;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.library.UI.StageInitializer;
import pl.edu.agh.to.library.model.author.Author;
import pl.edu.agh.to.library.model.author.AuthorService;

@Component
public class AddAuthorPagePresenter {
    private AuthorService authorService;

    private StageInitializer stageInitializer;

    @Autowired
    public AddAuthorPagePresenter(AuthorService authorService, StageInitializer stageInitializer){
        this.authorService = authorService;
        this.stageInitializer = stageInitializer;
    }

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField countryTextField;
    @FXML
    private Label resultText;

    @FXML
    private Button addButton;

    @FXML
    private void initialize(){
        addButton.disableProperty().bind(
                Bindings.isEmpty(firstNameTextField.textProperty())
                        .or(Bindings.isEmpty(lastNameTextField.textProperty()))
                        .or(Bindings.isEmpty(countryTextField.textProperty()))
        );
    }

    @FXML
    public void addAuthorHandler(){
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String country = countryTextField.getText();
        Author author=new Author(firstName,lastName,country);
        try{
            authorService.addAuthor(author);
            firstNameTextField.textProperty().set("");
            lastNameTextField.textProperty().set("");
            countryTextField.textProperty().set("");
            resultText.textProperty().set("Autor został dodany");
        }catch(DataAccessException e){
            resultText.textProperty().set("Nie udało się dodać autora");
        }
//        resultText.textProperty().set("");
    }

    @FXML
    private void handleBackButtonClick() {
        stageInitializer.showMainPage();
    }
}
