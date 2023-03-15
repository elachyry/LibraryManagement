package Models;

import DashboardAdmin.Controllers.AuthorsController;
import DashboardAdmin.Controllers.BooksController;
import DashboardAdmin.Pages.Manage.ShowBooksController;
import DashboardMember.DashboardController;
import Data_Base.MySql;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableView;
import javafx.stage.StageStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Author {

    private int Id;
    private String First_Name;
    private String Last_Name;
    private String Nationality;
    private int Number_Of_Books;
    private String Description;
    private JFXButton ShowBooks;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;

    public Author() {

    }

    public Author(String First_Name, String Last_Name) {
        this.First_Name = First_Name;
        this.Last_Name = Last_Name;
    }

    public Author(int Id, String First_Name, String Last_Name, String Nationality, int Number_Of_Books, String Description, JFXButton ShowBooks) {
        this.Id = Id;
        this.First_Name = First_Name;
        this.Last_Name = Last_Name;
        this.Nationality = Nationality;
        this.Number_Of_Books = Number_Of_Books;
        this.Description = Description;
        this.ShowBooks = ShowBooks;

        ShowBooks.setCursor(Cursor.HAND);
        ShowBooks.setFocusTraversable(false);
        ShowBooks.setOnAction((event) -> {
            ObservableList<Author> AuthorList = null;
            if (DashboardController.IsMember) {
                //AuthorList = MemberAuthorsController.AuthorList;
            } else {
                AuthorList = AuthorsController.AuthorList;
            }
            for (Author author : AuthorList) {
                if (author.getShowBooks() == this.ShowBooks) {
                    ShowBooksController.FiterBy = "Author";
                    ShowBooksController.AuthorId = author.getId();
                    DashboardController.AdminShowBooks = true;
                    DashboardController.MemberShowBooks = true;
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/DashboardAdmin/Pages/Manage/ShowBooks.fxml"));
                        DialogPane ShowBooksDialog = loader.load();
                        ShowBooksController SBC = loader.getController();
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setTitle(author.toString() + "'s Books");
                        dialog.initStyle(StageStyle.TRANSPARENT);
                        dialog.setDialogPane(ShowBooksDialog);
                        Optional<ButtonType> Result = dialog.showAndWait();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String First_Name) {
        this.First_Name = First_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Name(String Last_Name) {
        this.Last_Name = Last_Name;
    }

    public String getNationality() {
        return Nationality;
    }

    public void setNationality(String Nationality) {
        this.Nationality = Nationality;
    }

    public int getNumber_Of_Books() {
        return Number_Of_Books;
    }

    public void setNumber_Of_Books(int Number_Of_Books) {
        this.Number_Of_Books = Number_Of_Books;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public JFXButton getShowBooks() {
        return ShowBooks;
    }

    public void setShowBooks(JFXButton ShowBooks) {
        this.ShowBooks = ShowBooks;
    }

    public void addAuthor(String Fname, String Lname, String nationality, int nbrBooks, String description) {

        try {
            Con = MySql.GetConnection();
            Query = "INSERT INTO `authors`(`First_Name`, `Last_Name`, `Nationality`, `Number_Of_Books`, `Description`) VALUES (?,?,?,?,?)";
            PreStatment = Con.prepareStatement(Query);
            PreStatment.setString(1, Fname);
            PreStatment.setString(2, Lname);
            PreStatment.setString(3, nationality);
            PreStatment.setInt(4, nbrBooks);
            PreStatment.setString(5, description);

            PreStatment.execute();
        } catch (SQLException ex) {
        }
    }

    public void UpdateAuthor(int id, String Fname, String Lname, String nationality, int nbrBooks, String description) {

        try {
            Con = MySql.GetConnection();
            Query = "UPDATE `authors` SET `First_Name`=?,`Last_Name`=?,`Nationality`=?,"
                    + "`Number_Of_Books`=?,`Description`=? WHERE `Id`='" + id + "'";
            PreStatment = Con.prepareStatement(Query);
            PreStatment.setString(1, Fname);
            PreStatment.setString(2, Lname);
            PreStatment.setString(3, nationality);
            PreStatment.setInt(4, nbrBooks);
            PreStatment.setString(5, description);
            PreStatment.execute();
        } catch (SQLException ex) {
        }
    }

    public void DeleteAuthor(int AuthorId) {
        try {
            Query = "DELETE FROM `authors` WHERE id  =" + AuthorId;
            Con = MySql.GetConnection();
            PreStatment = Con.prepareStatement(Query);
            PreStatment.execute();
        } catch (SQLException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ExportAuthors() {
        try {
            Query = "SELECT * FROM `authors`";
            Con = MySql.GetConnection();
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();

            XSSFWorkbook XFWB = new XSSFWorkbook();
            XSSFSheet XFSheet = XFWB.createSheet("Author List");
            XSSFRow HeaderRow = XFSheet.createRow(0);
            HeaderRow.createCell(0).setCellValue("Id");
            HeaderRow.createCell(1).setCellValue("First Name");
            HeaderRow.createCell(2).setCellValue("Last Name");
            HeaderRow.createCell(3).setCellValue("Nationality");
            HeaderRow.createCell(4).setCellValue("Number Of Books");
            HeaderRow.createCell(5).setCellValue("Description");

            int RowNum = 1;
            while (resultSet.next()) {
                XSSFRow Row = XFSheet.createRow(RowNum);
                Row.createCell(0).setCellValue(resultSet.getInt(1));
                Row.createCell(1).setCellValue(resultSet.getString(2));
                Row.createCell(2).setCellValue(resultSet.getString(3));
                Row.createCell(3).setCellValue(resultSet.getString(4));
                Row.createCell(4).setCellValue(resultSet.getInt(5));
                Row.createCell(5).setCellValue(resultSet.getString(6));
                RowNum++;
            }
            try (FileOutputStream FileOutStr = new FileOutputStream("Excel\\Authors.xlsx")) {
                XFWB.write(FileOutStr);
            }
        } catch (IOException | SQLException ex) {
        }
    }

    public void SearchAuthor(JFXTextField SearchFieldAuthor, ObservableList<Author> AuthorList, TableView<Author> AuthorTable) {
        FilteredList<Author> filteredDataAuthor = new FilteredList<>(AuthorList, e -> true);
        SearchFieldAuthor.setOnKeyReleased(e -> {
            SearchFieldAuthor.textProperty().addListener((observableValue, OldValue, newValue) -> {
                filteredDataAuthor.setPredicate((Predicate<? super Author>) author -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String LowerCaseFilter = newValue.toLowerCase();
                    if (author.getFirst_Name().toLowerCase().contains(LowerCaseFilter)) {
                        return true;
                    } else if (author.getLast_Name().toLowerCase().contains(LowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Author> sortedListAuthor = new SortedList<>(filteredDataAuthor);
            sortedListAuthor.comparatorProperty().bind(AuthorTable.comparatorProperty());
            AuthorTable.setItems(sortedListAuthor);
        });
    }

    public void Incr_Dec_nbrOfBooks(String AuthorName, char Operation) {

        try {
            Con = MySql.GetConnection();
            Query = "UPDATE `authors` SET `Number_Of_Books`= `Number_Of_Books` " + Operation + " 1 WHERE `Id` = (SELECT DISTINCT Author_Id From `books` WHERE `Author` = '" + AuthorName + "')";
            PreStatment = Con.prepareStatement(Query);
            PreStatment.execute();
        } catch (SQLException ex) {
        }
    }

    @Override
    public String toString() {
        return First_Name + " " + Last_Name;
    }
}
