package pl.edu.agh.to.library.auxiliary;

import pl.edu.agh.to.library.model.loan.Loan;
import pl.edu.agh.to.library.model.loan.LoanRepository;

import java.text.SimpleDateFormat;

public  class LoanDetails {

    private Loan loan;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");

    public LoanDetails(Loan loan) {
        this.loan = loan;
    }

    public Loan getLoan() {
        return loan;
    }

    public String getTitle() {
        return loan.getBook().getTitle();
    }

    public String getWhenDate() {
        return sdf.format(loan.getLoanDate());
    }

    public String getUntilDate() {
        return sdf.format(loan.getReturnDate());
    }

    public String isReturned() {
        return loan.isReturned() ? "tak" : "nie";
    }
}
