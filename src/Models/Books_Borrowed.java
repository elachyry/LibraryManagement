package Models;

import DashboardAdmin.Controllers.Books_BorrowedController;
import DashboardAdmin.Controllers.FullBookController;
import DashboardAdmin.Pages.Manage.SendEmailsController;
import DashboardMember.Controllers.DialogConfirmationController;
import Data_Base.MySql;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;
import java.util.function.Predicate;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.stage.StageStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Books_Borrowed {

    private int Id;
    private String Borrowing_Date;
    private int Number_Of_Days;
    private String Return_Date;
    private String Status;
    private String Book_Title;
    private String Member_Name;
    private JFXButton SenEmail;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;
    public static int Admin = 0;

    public Books_Borrowed() {
    }

    public Books_Borrowed(int Id, String Borrowing_Date, int Number_Of_Days, String Return_Date, String Status, String Book_Title, String Member_Name, JFXButton SenEmail) {
        this.Id = Id;
        this.Borrowing_Date = Borrowing_Date;
        this.Number_Of_Days = Number_Of_Days;
        this.Return_Date = Return_Date;
        this.Status = Status;
        this.Book_Title = Book_Title;
        this.Member_Name = Member_Name;
        this.SenEmail = SenEmail;

        SenEmail.setCursor(Cursor.HAND);
        SenEmail.setFocusTraversable(false);
        SenEmail.setTooltip(new Tooltip("Send Email to Member"));
        SenEmail.setOnAction((event) -> {
            ObservableList<Books_Borrowed> LoanList = Books_BorrowedController.BorrowedBookList;
            for (Books_Borrowed Loan : LoanList) {
                if (Loan.getSenEmail() == this.SenEmail) {

                    try {
                        FXMLLoader loader = new FXMLLoader();
                        SendEmailsController.BookTitle = this.Book_Title;
                        SendEmailsController.BorringDate = this.Borrowing_Date;
                        SendEmailsController.ReturnDate = this.Return_Date;
                        SendEmailsController.MemberName = this.Member_Name;
                        loader.setLocation(getClass().getResource("/DashboardAdmin/Pages/Manage/SendEmails.fxml"));
                        DialogPane AddBookDialog = loader.load();
                        SendEmailsController FBC = loader.getController();
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.initStyle(StageStyle.TRANSPARENT);
                        dialog.setDialogPane(AddBookDialog);
                        Optional<ButtonType> Result = dialog.showAndWait();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getBorrowing_Date() {
        return Borrowing_Date;
    }

    public void setBorrowing_Date(String Borrowing_Date) {
        this.Borrowing_Date = Borrowing_Date;
    }

    public int getNumber_Of_Days() {
        return Number_Of_Days;
    }

    public void setNumber_Of_Days(int Number_Of_Days) {
        this.Number_Of_Days = Number_Of_Days;
    }

    public String getReturn_Date() {
        return Return_Date;
    }

    public void setReturn_Date(String Return_Date) {
        this.Return_Date = Return_Date;
    }

    public String getBook_Title() {
        return Book_Title;
    }

    public void setBook_Title(String Book_Title) {
        this.Book_Title = Book_Title;
    }

    public String getMember_Name() {
        return Member_Name;
    }

    public void setMember_Name(String Member_Name) {
        this.Member_Name = Member_Name;
    }

    public JFXButton getSenEmail() {
        return SenEmail;
    }

    public void setSenEmail(JFXButton SenEmail) {
        this.SenEmail = SenEmail;
    }

    public void addLoan() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        //Get number of days 
        if (FullBookController.Nbr_Pages < 150) {
            Number_Of_Days = 5;
        } else if (FullBookController.Nbr_Pages < 400) {
            Number_Of_Days = 7;
        } else {
            Number_Of_Days = 10;
        }
        //Add Number of days to Borrowing Date to get Return Date
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, Number_Of_Days);
        Date ReturnDate = c.getTime();
        //get Member Id
        int MemberId;
        if (Admin == 0) {
            Tools T = new Tools();
            MemberId = T.getUsernameId("members");
        } else {
            MemberId = Admin;
        }

        try {
            Con = MySql.GetConnection();
            Query = "INSERT INTO `books_borrowed`(`Borrowing_Date`, `Number_Of_Days`,"
                    + " `Return_Date`, `Status`, `Member_Id`, `Book_Id`) VALUES (?,?,?,?,?,?)";
            PreStatment = Con.prepareStatement(Query);
            PreStatment.setString(1, formatter.format(date));
            PreStatment.setInt(2, Number_Of_Days);
            PreStatment.setString(3, formatter.format(ReturnDate));
            PreStatment.setString(4, "Pending");
            PreStatment.setInt(5, MemberId);
            PreStatment.setInt(6, DialogConfirmationController.Id_Book);
            PreStatment.execute();
        } catch (SQLException ex) {
        }
    }

    public void EditStatusLoan(int LoanId, String status) {
        try {
            Con = MySql.GetConnection();
            Query = "UPDATE `books_borrowed` SET `Status`=? WHERE Id = " + LoanId;
            PreStatment = Con.prepareStatement(Query);
            PreStatment.setString(1, status);
            PreStatment.execute();
        } catch (SQLException ex) {
        }
    }

    public void Export() {
        try {
            Query = "SELECT * FROM `books_borrowed`";
            Con = MySql.GetConnection();
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();

            XSSFWorkbook XFWB = new XSSFWorkbook();
            XSSFSheet XFSheet = XFWB.createSheet("BooksBorrowedList");
            XSSFRow HeaderRow = XFSheet.createRow(0);
            HeaderRow.createCell(0).setCellValue("Id");
            HeaderRow.createCell(1).setCellValue("Member Name");
            HeaderRow.createCell(2).setCellValue("Book Title");
            HeaderRow.createCell(3).setCellValue("Borrowing date");
            HeaderRow.createCell(4).setCellValue("Number Of Days");
            HeaderRow.createCell(5).setCellValue("Return Date");
            HeaderRow.createCell(6).setCellValue("Status");

            int RowNum = 1;
            while (resultSet.next()) {
                XSSFRow Row = XFSheet.createRow(RowNum);
                Row.createCell(0).setCellValue(resultSet.getInt(1));
                Row.createCell(1).setCellValue(new Tools().getBookTitle(resultSet.getInt(7)));
                Row.createCell(2).setCellValue(new Tools().getMemberName(resultSet.getInt(6)));
                Row.createCell(3).setCellValue(resultSet.getString(2));
                Row.createCell(4).setCellValue(resultSet.getInt(3));
                Row.createCell(5).setCellValue(resultSet.getString(4));
                Row.createCell(6).setCellValue(resultSet.getString(5));
                RowNum++;
            }
            try (FileOutputStream FileOutStr = new FileOutputStream("Excel\\BooksBorrowed.xlsx")) {
                XFWB.write(FileOutStr);
            }

        } catch (IOException | SQLException ex) {
        }
    }

    public void SearchBook(JFXTextField SearchFieldBooksBorrowed, ObservableList<Books_Borrowed> BooksBorrowedList, TableView<Books_Borrowed> BooksBorrowed) {
        FilteredList<Books_Borrowed> filteredDataBooksBorrowed = new FilteredList<>(BooksBorrowedList, e -> true);
        SearchFieldBooksBorrowed.setOnKeyReleased(e -> {
            SearchFieldBooksBorrowed.textProperty().addListener((observableValue, OldValue, newValue) -> {
                filteredDataBooksBorrowed.setPredicate((Predicate<? super Books_Borrowed>) bb -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String LowerCaseFilter = newValue.toLowerCase();
                    if (bb.Member_Name.toLowerCase().contains(LowerCaseFilter)) {
                        return true;
                    } else if (bb.Book_Title.toLowerCase().contains(LowerCaseFilter)) {
                        return true;
                    } else if (bb.Status.toLowerCase().contains(LowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Books_Borrowed> sortedListBooksBorrowed = new SortedList<>(filteredDataBooksBorrowed);
            sortedListBooksBorrowed.comparatorProperty().bind(BooksBorrowed.comparatorProperty());
            BooksBorrowed.setItems(sortedListBooksBorrowed);
        });
    }

}
