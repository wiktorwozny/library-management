package pl.edu.agh.to.library.notifications;

import org.springframework.data.domain.Slice;
import pl.edu.agh.to.library.model.loan.Loan;
import pl.edu.agh.to.library.model.loan.LoanService;
import pl.edu.agh.to.library.model.user.UserService;

import java.time.temporal.ChronoUnit;
import java.util.Date;

public class ReturnReminderThread implements Runnable{
    private final MailService mailService;

    private final LoanService loanService;


    public ReturnReminderThread(MailService mailService, LoanService loanService) {
        this.mailService = mailService;
        this.loanService = loanService;
    }

    @Override
    public void run() {
        if (loanService.getCount() == 0) return;

        Date currentDate = new Date();
        int pageNumber = 0;
        Slice<Loan> currentSlice = loanService.getPage(pageNumber++);
        while (true) {
            for (Loan loan : currentSlice) {
                long daysLeft = currentDate.toInstant().until(loan.getReturnDate().toInstant(), ChronoUnit.DAYS);
                System.out.println(daysLeft);
                if (daysLeft <= 0) {
                    mailService.sendBookOverdueReminder(loan);
                } else if (daysLeft == loan.getUser().getNotification().getDaysLeftToReturnReminder()) {
                    mailService.sendReturnReminder(loan);
                }
            }

            if (currentSlice.hasNext()) {
                currentSlice = loanService.getPage(pageNumber++);
            }
            else break;
        }
    }
}
