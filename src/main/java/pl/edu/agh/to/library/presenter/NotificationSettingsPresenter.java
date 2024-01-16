package pl.edu.agh.to.library.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.library.model.notification.Notification;
import pl.edu.agh.to.library.model.notification.NotificationService;
import pl.edu.agh.to.library.model.user.UserService;

import java.util.function.UnaryOperator;

@Component
public class NotificationSettingsPresenter {
    private final NotificationService notificationService;

    private final UserService userService;

    @FXML
    private CheckBox enableMail;

    @FXML
    private CheckBox sendBookLoanedNotification;

    @FXML
    private CheckBox sendReturnReminder;

    @FXML
    private CheckBox sendBookOverdueReminder;

    @FXML
    private TextField daysLeftToReturnReminder;

    private Notification notificationSettings;

    @Autowired
    public NotificationSettingsPresenter(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @FXML
    private void initialize() {
        notificationSettings = userService.getCurrentUser().getNotification();

        enableMail.selectedProperty().set(notificationSettings.isEnableNotifications());
        sendBookLoanedNotification.selectedProperty().set(notificationSettings.isSendBookLoanedNotification());
        sendReturnReminder.selectedProperty().set(notificationSettings.isSendReturnReminder());
        sendBookOverdueReminder.selectedProperty().set(notificationSettings.isSendBookOverdueReminder());

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            if (input.matches("[0-9]*")) {
                return change;
            }
            return null;
        };
        daysLeftToReturnReminder.setTextFormatter(new TextFormatter<String>(integerFilter));
        daysLeftToReturnReminder.textProperty().set(String.valueOf(notificationSettings.getDaysLeftToReturnReminder()));
    }

    @FXML
    private void saveSettings() {
        notificationSettings.setEnableNotifications(enableMail.selectedProperty().get());
        notificationSettings.setSendBookLoanedNotification(sendBookLoanedNotification.selectedProperty().get());
        notificationSettings.setSendReturnReminder(sendReturnReminder.selectedProperty().get());
        notificationSettings.setSendBookOverdueReminder(sendBookOverdueReminder.selectedProperty().get());
        notificationSettings.setDaysLeftToReturnReminder(Integer.parseInt(daysLeftToReturnReminder.getText()));
        notificationService.save(notificationSettings);
    }
}
