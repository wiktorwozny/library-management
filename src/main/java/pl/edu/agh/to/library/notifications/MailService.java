package pl.edu.agh.to.library.notifications;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.library.model.loan.Loan;
import pl.edu.agh.to.library.model.loan.LoanService;
import pl.edu.agh.to.library.model.user.UserService;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class MailService {

    private InternetAddress appEmailAddress = new InternetAddress("noreply@bibliotekorze.com");

    private LoanService loanService;

    @Autowired
    public MailService(LoanService loanService) throws AddressException {
        this.loanService = loanService;

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(
                new ReturnReminderThread(this, loanService), 0, 1, TimeUnit.DAYS
        );
    }

    @PostConstruct
    private void init() {
        loanService.setMailService(this);
    }

    private void sendMail(String addressTo, String title, String body, Loan loan) {
        if (!loan.getUser().getNotification().isEnableNotifications()) return;

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "localhost");
        properties.setProperty("mail.smtp.port", "1025");
        Session session = Session.getInstance(properties);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(appEmailAddress);

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(addressTo));

            message.setSubject(title);
            message.setText(body);

            Transport.send(message);
            System.out.println("Mail sent");
        } catch (MessagingException e) {
//            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendBookLoanedNotification(Loan loan) {
        if (loan.getUser().getNotification().isSendBookLoanedNotification()) {
            String addressTo = loan.getUser().getEmail();
            String title = "Wypożyczono książkę";
            String body = "Wypożyczyłeś/aś książkę " + loan.getBook().getTitle() + " autorstwa " +
                    loan.getBook().getAuthor().getName() + ". Termin zwrotu to " + loan.getReturnDate().toString() +
                    ". Miłej lektury!";
            sendMail(addressTo, title, body, loan);
        }
    }

    public void sendReturnReminder(Loan loan) {
        if (loan.getUser().getNotification().isSendReturnReminder()) {
            String addressTo = loan.getUser().getEmail();
            String title = "Zostało Ci " + loan.getUser().getNotification().getDaysLeftToReturnReminder()
                    + " dni na zwrot książki";
            String body = loan.getReturnDate().toString() + " upływa termin zwrotu książki " +
                    loan.getBook().getTitle() + " autorstwa " + loan.getBook().getAuthor().getName() + ". " +
                    "Pamiętaj o zwrocie lub przedłuż termin w aplikacji Bibliotekorze™.";
            sendMail(addressTo, title, body, loan);
        }
    }

    public void sendBookOverdueReminder(Loan loan) {
        if (loan.getUser().getNotification().isSendBookOverdueReminder()) {
            String addressTo = loan.getUser().getEmail();
            String title = "Upłynął termin zwrotu książki";
            String body = loan.getReturnDate().toString() + " upłynął termin zwrotu książki " +
                    loan.getBook().getTitle() + " autorstwa " + loan.getBook().getAuthor().getName() + ". " +
                    "Pamiętaj o jak najszybszym zwrocie!";
            sendMail(addressTo, title, body, loan);
        }
    }
}
