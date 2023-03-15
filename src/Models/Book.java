package Models;

import DashboardAdmin.Controllers.BooksController;
import DashboardAdmin.Controllers.FullBookController;
import DashboardAdmin.Pages.Manage.ShowBooksController;
import DashboardMember.DashboardController;

import Data_Base.MySql;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.stage.StageStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Book {

    private int Id;
    private String ISBN;
    private String Title;
    private Author author;
    private String Publisher;
    private String Release_Date;
    private String Language;
    private Category category;
    private String Book_Shelf;
    private int nbr_Pages;
    private int nbr_Copies;
    private String Status;
    private JFXButton Button;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;

    public Book() {

    }

    public Book(int Id, String ISBN, String Title, Author author, String Publisher, String Release_Date, String Language, Category category, String Book_Shelf, int nbr_Pages, int nbr_Copies, String Status, JFXButton Button) {
        this.Id = Id;
        this.ISBN = ISBN;
        this.Title = Title;
        this.author = author;
        this.Publisher = Publisher;
        this.Release_Date = Release_Date;
        this.Language = Language;
        this.category = category;
        this.Book_Shelf = Book_Shelf;
        this.nbr_Pages = nbr_Pages;
        this.nbr_Copies = nbr_Copies;
        this.Status = Status;
        this.Button = Button;

        Button.setCursor(Cursor.HAND);
        Button.setFocusTraversable(false);
        Button.setOnAction((event) -> {
            ObservableList<Book> BookList = null;
            if (DashboardController.AdminShowBooks && DashboardController.IsMember == false) {
                BookList = ShowBooksController.BookList;
                System.out.println("showbooks admin");
            } else if (DashboardController.MemberShowBooks && DashboardController.IsMember) {
                BookList = ShowBooksController.BookList;
                System.out.println("showbooks member");
            } else if (DashboardController.IsMember) {
                System.out.println("Member");
            } else if (DashboardController.AdminShowBooks == false && DashboardController.IsMember == false) {
                BookList = BooksController.BookList;
                System.out.println("admin");
            }
            for (Book book : BookList) {
                if (book.getButton() == this.Button) {
                    FullBookController.BookId = book.getId();
                    String BookName = book.getTitle();
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/DashboardAdmin/Pages/FullBook.fxml"));
                        DialogPane AddBookDialog = loader.load();
                        FullBookController FBC = loader.getController();
                        FullBookController.BookId = book.getId();
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setTitle(BookName);
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

    public JFXButton getButton() {
        return Button;
    }

    public void setButton(JFXButton Button) {
        this.Button = Button;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author Author) {
        this.author = Author;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String Publisher) {
        this.Publisher = Publisher;
    }

    public String getRelease_Date() {
        return Release_Date;
    }

    public void setRelease_Date(String Release_Date) {
        this.Release_Date = Release_Date;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String Language) {
        this.Language = Language;
    }

    public int getNbr_Pages() {
        return nbr_Pages;
    }

    public void setNbr_Pages(int nbr_Pages) {
        this.nbr_Pages = nbr_Pages;
    }

    public String getBook_Shelf() {
        return Book_Shelf;
    }

    public void setBook_Shelf(String Book_Shelf) {
        this.Book_Shelf = Book_Shelf;
    }

    public int getNbr_Copies() {
        return nbr_Copies;
    }

    public void setNbr_Copies(int nbr_Copies) {
        this.nbr_Copies = nbr_Copies;
    }

    public void addBook(String isbn, String title, String author, String publisher, String releaseDate, String language, String category, String bookShelf, int nbrPages, int nbrCopies, File file) {

        try {
            PreparedStatement PreStatment2 = null;
            ResultSet resultSet2 = null;
            PreparedStatement PreStatment3 = null;
            ResultSet resultSet3 = null;
            String FullName = author;
            String[] NameSplit = FullName.split(" ");
            Con = MySql.GetConnection();
            FileInputStream fin = null;
            int len = 0;
            if (file != null) {
                fin = new FileInputStream(file);
                len = (int) file.length();
                Query = "INSERT INTO `books`(`ISBN`, `Title`, `Author`, `Publisher`, `Release_Date`,"
                        + " `Language`, `Category`, `Book_shelf`, `Nbr_Pages`, `Author_Id`, `Category_Id`,"
                        + " `Nbr_Copies` , `Book_Image`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            } else {
                Query = "INSERT INTO `books`(`ISBN`, `Title`, `Author`, `Publisher`, `Release_Date`,"
                        + " `Language`, `Category`, `Book_shelf`, `Nbr_Pages`, `Author_Id`, `Category_Id`,"
                        + " `Nbr_Copies` ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            }
            //Get Author Id
            String Query2 = "SELECT `Id` FROM `authors` WHERE `First_Name` = '" + NameSplit[0]
                    + "' AND `Last_Name` = '" + NameSplit[NameSplit.length - 1] + "'";
            //Get category Id
            String Query3 = "SELECT `Id` FROM `categories` WHERE `Name` = '" + category + "'";

            PreStatment2 = Con.prepareStatement(Query2);
            PreStatment3 = Con.prepareStatement(Query3);
            resultSet2 = PreStatment2.executeQuery();
            resultSet2.next();
            resultSet3 = PreStatment3.executeQuery();
            resultSet3.next();
            int Author_Id = resultSet2.getInt(1);
            int Category_Id = resultSet3.getInt(1);
            PreStatment = Con.prepareStatement(Query);
            PreStatment.setString(1, isbn);
            PreStatment.setString(2, title);
            PreStatment.setString(3, author);
            PreStatment.setString(4, publisher);
            PreStatment.setString(5, releaseDate);
            PreStatment.setString(6, language);
            PreStatment.setString(7, category);
            PreStatment.setString(8, bookShelf);
            PreStatment.setInt(9, nbrPages);
            PreStatment.setInt(10, Author_Id);
            PreStatment.setInt(11, Category_Id);
            PreStatment.setInt(12, nbrCopies);
            if (file != null) {
                PreStatment.setBinaryStream(13, fin, len);
            }
            PreStatment.execute();
        } catch (FileNotFoundException | SQLException ex) {
        }
    }

    public void UpdateBook(int id, String isbn, String title, String author, String publisher, String releaseDate, String language, String category, String bookShelf, int nbrPages, int nbrCopies, File file) {

        try {
            PreparedStatement PreStatment2 = null;
            ResultSet resultSet2 = null;
            PreparedStatement PreStatment3 = null;
            ResultSet resultSet3 = null;
            String FullName = author;
            String[] NameSplit = FullName.split(" ");
            FileInputStream fin = null;
            int len = 0;
            Con = MySql.GetConnection();
            if (file != null) {
                fin = new FileInputStream(file);
                len = (int) file.length();

                Query = "UPDATE `books` SET `ISBN`=?,`Title`=?,`Author`=?,"
                        + "`Publisher`=?,`Release_Date`=?,`Language`=?,"
                        + "`Category`=?,`Book_shelf`=?,`Nbr_Pages`=? ,`Author_Id`=?,"
                        + "`Category_Id`=?,`Nbr_Copies`=?, `Book_Image` =? WHERE `Id`='" + id + "'";
            } else {
                Query = "UPDATE `books` SET `ISBN`=?,`Title`=?,`Author`=?,"
                        + "`Publisher`=?,`Release_Date`=?,`Language`=?,"
                        + "`Category`=?,`Book_shelf`=?,`Nbr_Pages`=? ,`Author_Id`=?,"
                        + "`Category_Id`=?,`Nbr_Copies`=? WHERE `Id`='" + id + "'";
            }
            //Get Author Id
            String Query2 = "SELECT `Id` FROM `authors` WHERE `First_Name` = '" + NameSplit[0]
                    + "' AND `Last_Name` = '" + NameSplit[NameSplit.length - 1] + "'";
            //Get category Id
            String Query3 = "SELECT `Id` FROM `categories` WHERE `Name` = '" + category + "'";

            PreStatment2 = Con.prepareStatement(Query2);
            PreStatment3 = Con.prepareStatement(Query3);
            resultSet2 = PreStatment2.executeQuery();
            resultSet2.next();
            resultSet3 = PreStatment3.executeQuery();
            resultSet3.next();
            int Author_Id = resultSet2.getInt(1);
            int Category_Id = resultSet3.getInt(1);

            PreStatment = Con.prepareStatement(Query);
            PreStatment.setString(1, isbn);
            PreStatment.setString(2, title);
            PreStatment.setString(3, author);
            PreStatment.setString(4, publisher);
            PreStatment.setString(5, releaseDate);
            PreStatment.setString(6, language);
            PreStatment.setString(7, category);
            PreStatment.setString(8, bookShelf);
            PreStatment.setInt(9, nbrPages);
            PreStatment.setInt(10, Author_Id);
            PreStatment.setInt(11, Category_Id);
            PreStatment.setInt(12, nbrCopies);
            if (file != null) {
                PreStatment.setBinaryStream(13, fin, len);
            }
            PreStatment.execute();
        } catch (FileNotFoundException | SQLException ex) {
        }
    }

    public void DeleteBook(int BookId) {
        try {
            Query = "DELETE FROM `books` WHERE id  =" + BookId;
            Con = MySql.GetConnection();
            PreStatment = Con.prepareStatement(Query);
            PreStatment.execute();
        } catch (SQLException ex) {
        }
    }

    public void DeleteBooksGroup(String Group, int Id) {
        try {
            Query = "DELETE FROM `books` WHERE " + Group + " = " + Id;
            Con = MySql.GetConnection();
            PreStatment = Con.prepareStatement(Query);
            PreStatment.execute();
        } catch (SQLException ex) {
        }
    }

    public void ExportBooks(String Query, String Name) {
        try {

            Con = MySql.GetConnection();
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();

            XSSFWorkbook XFWB = new XSSFWorkbook();
            XSSFSheet XFSheet = XFWB.createSheet("Book List");
            XSSFRow HeaderRow = XFSheet.createRow(0);
            HeaderRow.createCell(0).setCellValue("Id");
            HeaderRow.createCell(1).setCellValue("ISBN");
            HeaderRow.createCell(2).setCellValue("Title");
            HeaderRow.createCell(3).setCellValue("Author");
            HeaderRow.createCell(4).setCellValue("Publisher");
            HeaderRow.createCell(5).setCellValue("Release Date");
            HeaderRow.createCell(6).setCellValue("Language");
            HeaderRow.createCell(7).setCellValue("Category");
            HeaderRow.createCell(8).setCellValue("Nbr Pages");
            HeaderRow.createCell(9).setCellValue("Book Shelf");
            HeaderRow.createCell(10).setCellValue("Author_Id");
            HeaderRow.createCell(11).setCellValue("Category_Id");
            HeaderRow.createCell(12).setCellValue("Nbr_Copies");

            int RowNum = 1;
            while (resultSet.next()) {
                XSSFRow Row = XFSheet.createRow(RowNum);
                Row.createCell(0).setCellValue(resultSet.getInt(1));
                Row.createCell(1).setCellValue(resultSet.getString(2));
                Row.createCell(2).setCellValue(resultSet.getString(3));
                Row.createCell(3).setCellValue(resultSet.getString(4));
                Row.createCell(4).setCellValue(resultSet.getString(5));
                Row.createCell(5).setCellValue(resultSet.getString(6));
                Row.createCell(6).setCellValue(resultSet.getString(7));
                Row.createCell(7).setCellValue(resultSet.getString(8));
                Row.createCell(8).setCellValue(resultSet.getString(9));
                Row.createCell(9).setCellValue(resultSet.getString(10));
                Row.createCell(10).setCellValue(resultSet.getString(11));
                Row.createCell(11).setCellValue(resultSet.getString(12));
                Row.createCell(12).setCellValue(resultSet.getString(13));
                RowNum++;
            }
            try (FileOutputStream FileOutStr = new FileOutputStream("Excel\\" + Name + ".xlsx")) {
                XFWB.write(FileOutStr);
            }
        } catch (IOException | SQLException ex) {
        }
    }

    public void SearchBook(JFXTextField SearchFieldBook, ObservableList<Book> BookList, TableView<Book> BookTable) {
        FilteredList<Book> filteredDataBook = new FilteredList<>(BookList, e -> true);
        SearchFieldBook.setOnKeyReleased(e -> {
            SearchFieldBook.textProperty().addListener((observableValue, OldValue, newValue) -> {
                filteredDataBook.setPredicate((Predicate<? super Book>) book -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String LowerCaseFilter = newValue.toLowerCase();
                    if (book.getISBN().toLowerCase().contains(LowerCaseFilter)) {
                        return true;
                    } else if (book.getTitle().toLowerCase().contains(LowerCaseFilter)) {
                        return true;
                    } else if (book.author.toString().toLowerCase().contains(LowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Book> sortedListBook = new SortedList<>(filteredDataBook);
            sortedListBook.comparatorProperty().bind(BookTable.comparatorProperty());
            BookTable.setItems(sortedListBook);
        });
    }

    public void Incr_Dec_nbrOfBooks(int LoanId, char Operation) {

        try {
            Con = MySql.GetConnection();
            Query = "UPDATE `books` SET `Nbr_Copies`= `Nbr_Copies` " + Operation + " 1 WHERE `Id` = (SELECT DISTINCT Book_Id From `books_borrowed` WHERE `Id` = " + LoanId + ")";
            PreStatment = Con.prepareStatement(Query);
            PreStatment.execute();
        } catch (SQLException ex) {
        }
    }

}
