package pl.edu.agh.to.library.UI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import pl.edu.agh.to.library.LibraryApplication;

public class UiApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init(){
        ApplicationContextInitializer<GenericApplicationContext> initializer =
                applicationContext -> applicationContext.registerBean(Application.class, () -> UiApplication.this);

        applicationContext = new SpringApplicationBuilder(LibraryApplication.class).initializers(initializer).run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        applicationContext.publishEvent(new StageReadyEvent(primaryStage));
    }

    @Override
    public void stop(){
        applicationContext.close();
        Platform.exit();
    }


    public class StageReadyEvent extends ApplicationEvent {
        public Stage getStage(){
            return (Stage)getSource();
        }

        public StageReadyEvent(Stage primaryStage) {
            super(primaryStage);
        }
    }
}
