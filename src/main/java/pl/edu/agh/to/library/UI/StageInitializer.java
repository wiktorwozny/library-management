package pl.edu.agh.to.library.UI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class StageInitializer implements ApplicationListener<UiApplication.StageReadyEvent> {

    private final ConfigurableApplicationContext applicationContext;

    private Stage stage;

    @Autowired
    public StageInitializer(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(UiApplication.StageReadyEvent event) {
        stage = event.getStage();
        showMainPage();
    }

    public void showMainPage() {
        Resource resource = new ClassPathResource("view/mainPage.fxml");
        showFromResource(resource);
    }

    public void showLoginPage() {
        Resource resource = new ClassPathResource("view/loginPage.fxml");
        showFromResource(resource);
    }

    public void showAddUserPage() {
        Resource resource = new ClassPathResource("view/addUserPage.fxml");
        showFromResource(resource);
    }

    public void showAdminPage() {
        Resource resource = new ClassPathResource("view/adminPage.fxml");
        showFromResource(resource);
    }

    public void showAddAuthorPage() {
        Resource resource = new ClassPathResource("view/addAuthorPage.fxml");
        showFromResource(resource);
    }

    public void showAddBookPage() {
        Resource resource = new ClassPathResource("view/addBookPage.fxml");
        showFromResource(resource);
    }

    public void showAddBookReviewPage() {
        Resource resource = new ClassPathResource("view/addBookReviewPage.fxml");
        showFromResource(resource);
    }

    public void showBooksViewPage() {
        Resource resource = new ClassPathResource("view/booksViewPage.fxml");
        showFromResource(resource);
    }

    public void showSingleBookPage() {
        Resource resource = new ClassPathResource("view/singleBookPage.fxml");
        showFromResource(resource);
    }

    public void showUserProfilePage() {
        Resource resource = new ClassPathResource("view/userProfilePage.fxml");
        showFromResource(resource);
    }

    public void showChangeFirstnamePage() {
        Resource resource = new ClassPathResource("view/changeFirstnamePage.fxml");
        showFromResource(resource);
    }

    public void showChangeLastnamePage() {
        Resource resource = new ClassPathResource("view/changeLastnamePage.fxml");
        showFromResource(resource);
    }

    public void showLibrarianPage() {
        Resource resource = new ClassPathResource("view/librarianPage.fxml");
        showFromResource(resource);
    }

    public void showNotificationSettings() {
        try {
            Resource resource = new ClassPathResource("view/notificationSettingsPage.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(resource.getURL());
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Settings");
            stage.setScene(new Scene(parent, 700, 400));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showFromResource(Resource resource) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(resource.getURL());
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent, 1280, 720);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
