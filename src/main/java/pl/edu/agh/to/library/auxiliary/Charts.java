package pl.edu.agh.to.library.auxiliary;

import javafx.scene.chart.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.library.model.book.Book;
import pl.edu.agh.to.library.model.genre.Genre;
import pl.edu.agh.to.library.model.loan.Loan;
import pl.edu.agh.to.library.model.loan.LoanService;
import pl.edu.agh.to.library.model.user.User;

import java.util.*;

@Component
public class Charts {
    private final String[] MONTHS = {"Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec",
            "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień"};

    private LoanService loanService;

    @Autowired
    public Charts(LoanService loanService) {
        this.loanService = loanService;
    }

    private LineChart<String, Number> last12MonthsChart(List<Loan> loanList) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Liczba wypożyczeń w danym miesiącu");

        Calendar date = Calendar.getInstance();
        date.add(Calendar.MONTH, -11);

        for (int i = 0; i < 12; i++) {
            long count = loanList.stream().filter(loan -> {
                Calendar loanDate = Calendar.getInstance();
                loanDate.setTime(loan.getLoanDate());
                return loanDate.get(Calendar.YEAR) == date.get(Calendar.YEAR) && loanDate.get(Calendar.MONTH) == date.get(Calendar.MONTH);
            }).count();
            series.getData().add(new XYChart.Data<>(MONTHS[date.get(Calendar.MONTH)], count));
            date.add(Calendar.MONTH, 1);
        }

        chart.getData().add(series);

        return chart;
    }

    public LineChart<String, Number> numberOfLoansChart() {
        List<Loan> loanList = loanService.getAllLoans();
        return last12MonthsChart(loanList);
    }

    public LineChart<String, Number> bookPopularityChart(Book book) {
        List<Loan> loanList = loanService.getLoansByBookId(book.getId());
        return last12MonthsChart(loanList);
    }

    public LineChart<String, Number> userActivityChart(User user) {
        List<Loan> loanList = loanService.getLoansByUserId(user.getId());
        return last12MonthsChart(loanList);
    }

    private PieChart genreChart(List<Loan> loanList) {
        HashMap<String, Integer> genreMap = new HashMap<>();

        for (Loan loan : loanList) {
            Set<Genre> genreSet = loan.getBook().getGenres();
            for (Genre genre : genreSet) {
                genreMap.putIfAbsent(genre.getGenreName(), 0);
                genreMap.put(genre.getGenreName(), genreMap.get(genre.getGenreName()) + 1);
            }
        }

        PieChart pieChart = new PieChart();
        genreMap.forEach((key, value) -> pieChart.getData().add(new PieChart.Data(key, (double) value / loanList.size())));

        return pieChart;
    }

    public PieChart userGenreChart(User user) {
        List<Loan> loanList = loanService.getLoansByUserId(user.getId());
        return genreChart(loanList);
    }

    public PieChart totalGenreChart() {
        List<Loan> loanList = loanService.getAllLoans();
        return genreChart(loanList);
    }

    private PieChart authorChart(List<Loan> loanList) {
        HashMap<String, Integer> authorMap = new HashMap<>();

        for (Loan loan : loanList) {
            authorMap.putIfAbsent(loan.getBook().getAuthor().getName(), 0);
            authorMap.put(loan.getBook().getAuthor().getName(), authorMap.get(loan.getBook().getAuthor().getName()) + 1);
        }

        PieChart pieChart = new PieChart();
        authorMap.forEach((key, value) -> pieChart.getData().add(new PieChart.Data(key, (double) value / loanList.size())));

        return pieChart;
    }

    public PieChart userAuthorChart(User user) {
        List<Loan> loanList = loanService.getLoansByUserId(user.getId());
        return authorChart(loanList);
    }

    public PieChart totalAuthorChart() {
        List<Loan> loanList = loanService.getAllLoans();
        return authorChart(loanList);
    }
}
