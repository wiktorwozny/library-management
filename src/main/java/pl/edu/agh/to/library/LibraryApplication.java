package pl.edu.agh.to.library;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.agh.to.library.UI.UiApplication;

@SpringBootApplication
public class LibraryApplication{

    public static void main(String[] args) {
        Application.launch(UiApplication.class, args);
    }
}
