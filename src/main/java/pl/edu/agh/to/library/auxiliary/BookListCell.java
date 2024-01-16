package pl.edu.agh.to.library.auxiliary;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.library.UI.StageInitializer;
import pl.edu.agh.to.library.model.book.Book;
import pl.edu.agh.to.library.model.book.BookService;

public class BookListCell extends ListCell<Book> {
    private HBox root;

    private Label label;

    private BookService bookService;

    private StageInitializer stageInitializer;

    public BookListCell(BookService bookService, StageInitializer stageInitializer) {
        root = new HBox();
        label = new Label();

        this.bookService = bookService;
        this.stageInitializer = stageInitializer;

        root.getChildren().addAll(label);
    }

    @Override
    protected void updateItem(Book book, boolean empty) {
        super.updateItem(book, empty);

        if (empty || book == null) {
            setText(null);
            setGraphic(null);
        } else {
            label.textProperty().set(book.getTitle() + "-" + book.getAuthor().getName());

            root.setOnMouseClicked(event -> {
                bookService.setCurrentBook(book);
                stageInitializer.showSingleBookPage();
            });

            setText(null);
            setGraphic(root);
        }
    }
}
