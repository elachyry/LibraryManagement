package DashboardAdmin.Controllers;

import Data_Base.MySql;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class OverviewController implements Initializable {

    @FXML
    private Label NumberBooks;

    @FXML
    private Label NumberAuthor;

    @FXML
    private Label NumberCategory;

    @FXML
    private Label NumberBooksBorrowed;

    @FXML
    private LineChart<?, ?> lineChart;

    @FXML
    private PieChart pieChart;

    @FXML
    private Label NumberMembers;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;
    @FXML
    private Label Book1;
    @FXML
    private Label Book2;
    @FXML
    private Label Book3;
    @FXML
    private Label Book4;
    @FXML
    private Label Book5;
    @FXML
    private Label BookR1;
    @FXML
    private Label BookR2;
    @FXML
    private Label BookR3;
    @FXML
    private Label BookR4;
    @FXML
    private Label BookR5;

    public int getNumberBooks() {
        int Count = 0;
        try {
            Con = MySql.GetConnection();
            Query = "SELECT SUM(Nbr_Copies) FROM `books` ";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            resultSet.next();
            Count = resultSet.getInt(1);
        } catch (SQLException ex) {
        }
        return Count;
    }

    public int getNumberAuthors() {
        int Count = 0;
        try {
            Con = MySql.GetConnection();
            Query = "SELECT Count(*) FROM `authors` ";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            resultSet.next();
            Count = resultSet.getInt(1);
        } catch (SQLException ex) {
        }
        return Count;
    }

    public int getNumberCategories() {
        int Count = 0;
        try {
            Con = MySql.GetConnection();
            Query = "SELECT Count(*) FROM `categories` ";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            resultSet.next();
            Count = resultSet.getInt(1);
        } catch (SQLException ex) {
        }
        return Count;
    }

    public int getNumberBooksBorroed() {
        int Count = 0;
        try {
            Con = MySql.GetConnection();
            Query = "SELECT Count(*) FROM `books_borrowed` ";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            resultSet.next();
            Count = resultSet.getInt(1);
        } catch (SQLException ex) {
        }
        return Count;
    }

    public int getNumberMembers() {
        int Count = 0;
        try {
            Con = MySql.GetConnection();
            Query = "SELECT Count(*) FROM `members` ";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            resultSet.next();
            Count = resultSet.getInt(1);
        } catch (SQLException ex) {
        }
        return Count;
    }

    public void getBooksNotYetReturned() {
        String[] BookR = new String[4];
        try {
            Con = MySql.GetConnection();
            Query = "SELECT Title FROM books INNER JOIN books_borrowed ON books.Id = books_borrowed.Book_Id "
                    + "AND books_borrowed.Status = 'Late'";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                BookR[i] = resultSet.getString(1);
                if (i == 3) {
                    break;
                }
                i++;
            }
        } catch (SQLException ex) {
        }

        BookR1.setText(BookR[0]);
        BookR2.setText(BookR[1]);
        BookR3.setText(BookR[2]);
        BookR4.setText(BookR[3]);

    }

    public void getBooksOutOfStock() {
        String[] Books = new String[4];
        try {
            Con = MySql.GetConnection();
            Query = "SELECT Title FROM `books` WHERE Nbr_Copies = 0 ";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                Books[i] = resultSet.getString(1);
                if (i == 3) {
                    break;
                }
                i++;
            }
        } catch (SQLException ex) {
        }

        Book1.setText(Books[0]);
        Book2.setText(Books[1]);
        Book3.setText(Books[2]);
        Book4.setText(Books[3]);

    }

    public int CountStatus(String Status) {
        int count = 0;
        try {
            Con = MySql.GetConnection();
            Query = "SELECT COUNT(*) FROM books_borrowed WHERE Status = '" + Status + "'";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);

        } catch (SQLException ex) {
        }
        return count;
    }

    public void FillPieChar() {
        int Total = 0;
        try {
            Con = MySql.GetConnection();
            Query = "SELECT COUNT(*) FROM books_borrowed ";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            resultSet.next();
            if (resultSet.getInt(1) == 0) {
                Total = 1;
            } else {
                Total = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Pending " + (CountStatus("Pending") * 100) / Total + "%", CountStatus("Pending")),
                new PieChart.Data("Canceled " + (CountStatus("Canceled") * 100) / Total + "%", CountStatus("Canceled")),
                new PieChart.Data("Accepted " + (CountStatus("Accepted") * 100) / Total + "%", CountStatus("Accepted")),
                new PieChart.Data("Late " + (CountStatus("Late") * 100) / Total + "%", CountStatus("Late")),
                new PieChart.Data("Returned " + (CountStatus("Returned") * 100) / Total + "%", CountStatus("Returned")),
                new PieChart.Data("Denied " + (CountStatus("Denied") * 100) / Total + "%", CountStatus("Denied"))
        );
        pieChart.setData(pieChartData);
    }

    public void FillLineChar() {
        int[] Counts = new int[7];
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("u");
        Date date = new Date();
        int dayNbr = Integer.parseInt(formatter2.format(date));
        System.out.println("day number " + dayNbr);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -dayNbr + 1);
        Date ReturnDate = null;
        for (int i = 0; i < 7; i++) {
            c.add(Calendar.DATE, - 1);
            ReturnDate = c.getTime();
            System.out.println("date " + formatter.format(ReturnDate));
            try {
                Con = MySql.GetConnection();
                Query = "SELECT COUNT(*) FROM books_borrowed WHERE Borrowing_Date = '" + formatter.format(ReturnDate) + "'";
                PreStatment = Con.prepareStatement(Query);
                resultSet = PreStatment.executeQuery();
                resultSet.next();
                Counts[i] = resultSet.getInt(1);
            } catch (SQLException ex) {
            }
        }
        int[] Counts2 = new int[7];
        Calendar c2 = Calendar.getInstance();
        c2.setTime(ReturnDate);
        c2.add(Calendar.DATE, -7);
        System.out.println("two week " + formatter.format(c2.getTime()));
        for (int i = 0; i < 7; i++) {
            c2.add(Calendar.DATE, - 1);
            Date ReturnDate2 = c2.getTime();
            System.out.println("date " + formatter.format(ReturnDate2));
            try {
                Con = MySql.GetConnection();
                Query = "SELECT COUNT(*) FROM books_borrowed WHERE Borrowing_Date = '" + formatter.format(ReturnDate2) + "'";
                PreStatment = Con.prepareStatement(Query);
                resultSet = PreStatment.executeQuery();
                resultSet.next();
                Counts2[i] = resultSet.getInt(1);
            } catch (SQLException ex) {
            }
        }
        for (int i = 0; i < 7; i++) {
            System.out.println("count1 " + Counts[i]);
        }
        for (int i = 0; i < 7; i++) {
            System.out.println("count2 " + Counts2[i]);
        }

        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data("Monday", Counts[6]));
        series.getData().add(new XYChart.Data("Tuesday", Counts[5]));
        series.getData().add(new XYChart.Data("Wednesday", Counts[4]));
        series.getData().add(new XYChart.Data("Thursday", Counts[3]));
        series.getData().add(new XYChart.Data("Friday", Counts[2]));
        series.getData().add(new XYChart.Data("Saturday", Counts[1]));
        series.getData().add(new XYChart.Data("Sunday", Counts[0]));
        series.setName("A week ago");

        XYChart.Series series2 = new XYChart.Series();
        series2.getData().add(new XYChart.Data("Monday", Counts2[6]));
        series2.getData().add(new XYChart.Data("Tuesday", Counts2[5]));
        series2.getData().add(new XYChart.Data("Wednesday", Counts2[4]));
        series2.getData().add(new XYChart.Data("Thursday", Counts2[3]));
        series2.getData().add(new XYChart.Data("Friday", Counts2[2]));
        series2.getData().add(new XYChart.Data("Saturday", Counts2[1]));
        series2.getData().add(new XYChart.Data("Sunday", Counts2[0]));
        series2.setName("Two weeks ago");

        lineChart.getData().addAll(series, series2);
        series.getNode().setStyle("-fx-stroke: #0074D1; -fx-stroke-width: 4px;");
        series2.getNode().setStyle("-fx-stroke: #51C9C2; -fx-stroke-width: 4px;");
    }

    public void CheckLaonLate() {
        try {
            Con = MySql.GetConnection();
            SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            System.out.println("date late" + Format.format(date));
            Query = "UPDATE `books_borrowed` SET `Status`= 'Late' WHERE Status = 'Accepted' AND Return_Date < '" + Format.format(date) + "'";
            PreStatment = Con.prepareStatement(Query);
            PreStatment.execute();
        } catch (SQLException ex) {
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CheckLaonLate();
        NumberBooks.setText(String.valueOf(getNumberBooks()));
        NumberAuthor.setText(String.valueOf(getNumberAuthors()));
        NumberCategory.setText(String.valueOf(getNumberCategories()));
        NumberBooksBorrowed.setText(String.valueOf(getNumberBooksBorroed()));
        NumberMembers.setText(String.valueOf(getNumberMembers()));
        getBooksOutOfStock();
        getBooksNotYetReturned();
        FillPieChar();
        FillLineChar();
    }

}
