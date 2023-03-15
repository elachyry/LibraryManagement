package DashboardAdmin.Controllers;

import DashboardAdmin.Pages.Manage.AddBookController;
import Data_Base.MySql;
import Models.Author;
import Models.Book;
import Models.Category;
import Models.Tools;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author ELACHYRY
 */
public class BooksController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXDialog Dialog2;

    @FXML
    private Label DialogLabel;

    @FXML
    private JFXButton YesButton;

    @FXML
    private JFXButton NoButton;

    @FXML
    private JFXTextField SearchFieldBook;

    @FXML
    private TableView<Book> BookTable;

    @FXML
    private TableColumn<Book, Integer> IdCol;

    @FXML
    private TableColumn<Book, String> ISBNCol;

    @FXML
    private TableColumn<Book, String> TitleCol;

    @FXML
    private TableColumn<Book, String> AuthorCol;

    @FXML
    private TableColumn<Book, String> CategoryCol;

    @FXML
    private TableColumn<Book, String> StatusCol;

    @FXML
    private TableColumn<Book, JFXButton> ShowMoreCol;

    @FXML
    private ImageView Refrech;

    @FXML
    private JFXButton AddBook;

    @FXML
    private JFXButton EditBook;

    @FXML
    private JFXButton DeleteBook;

    @FXML
    private JFXButton ExportBooks;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;
    Book book = null;
    public static ObservableList<Book> BookList = FXCollections.observableArrayList();

    @FXML
    void Add_Book(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            AddBookController.Edit = false;
            loader.setLocation(getClass().getResource("/DashboardAdmin/Pages/Manage/AddBook.fxml"));
            DialogPane AddBookDialog = loader.load();
            AddBookController ABC = loader.getController();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initStyle(StageStyle.TRANSPARENT);
            dialog.setTitle("Add new book");
            dialog.setDialogPane(AddBookDialog);
            Optional<ButtonType> Result = dialog.showAndWait();

        } catch (IOException e) {
        }
    }

    @FXML
    void Delete_Book(MouseEvent event) {
        book = BookTable.getSelectionModel().getSelectedItem();
        if (book != null) {
            BoxBlur Blur = new BoxBlur(3, 3, 3);
            anchorPane.setEffect(Blur);
            YesButton.setPrefSize(140, 40);
            YesButton.setFont(new Font(17));
            YesButton.setCursor(Cursor.HAND);
            NoButton.setPrefSize(140, 40);
            NoButton.setFont(new Font(17));
            NoButton.setCursor(Cursor.HAND);
            DialogLabel.setText("Do you want to delete this Book?");
            DialogLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-font-size: 20px; -fx-text-fill : #52438F;");
            Dialog2.setTransitionType(JFXDialog.DialogTransition.TOP);
            Dialog2.setDialogContainer(stackPane);
            Dialog2.show();
            Object data = getAuthorValue();
            Object data2 = getCategoryValue();
            YesButton.setOnAction((Event) -> {
                //Decrement Number Of Books in category and author
                Author aut = new Author();
                Category cat = new Category();
                aut.Incr_Dec_nbrOfBooks(data.toString(), '-');
                cat.Incr_Dec_nbrOfBooks(data2.toString(), '-');
                book.DeleteBook(book.getId());
                RefrechBookTable();
                anchorPane.setEffect(null);
                Dialog2.close();
            });
            Dialog2.setOnDialogClosed((JFXDialogEvent Event) -> {
                anchorPane.setEffect(null);
            });
            NoButton.setOnAction((Event) -> {
                anchorPane.setEffect(null);
                Dialog2.close();
            });
        } else {
            Tools.DialogAlert(stackPane, anchorPane, "No row selected!");
        }
    }

    @FXML
    void Edit_Book(MouseEvent event) throws IOException {
        book = BookTable.getSelectionModel().getSelectedItem();

        if (book != null) {
            try {
                //Select Author Value and Category
                Object data = getAuthorValue();
                Object data2 = getCategoryValue();
                //Load AddBook FXML
                AddBookController.BookId = book.getId();
                AddBookController.Edit = true;
                FullBookController.BookId = book.getId();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/DashboardAdmin/Pages/Manage/AddBook.fxml"));
                DialogPane AddBookDialog = loader.load();
                AddBookController ABC = loader.getController();
                ABC.setEdit(true);
                ABC.EditData(book.getId(), book.getISBN(), book.getTitle(), data.toString(), book.getPublisher(), book.getRelease_Date(), book.getLanguage(), data2.toString(), book.getBook_Shelf(), book.getNbr_Pages(), book.getNbr_Copies());
                ABC.setTitleLabel("Edit Book");
                ABC.setButtonText("Edit");
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Edit Book");
                dialog.initStyle(StageStyle.TRANSPARENT);
                dialog.setDialogPane(AddBookDialog);
                Optional<ButtonType> Result = dialog.showAndWait();
            } catch (IOException e) {
            }

        } else {
            Tools.DialogAlert(stackPane, anchorPane, "No row selected!");
        }
    }

    @FXML
    void Export_Book(MouseEvent event) throws IOException {
        book = new Book();
        book.ExportBooks("SELECT * FROM `books`", "AllBooks");
        Tools.DialogAlert(stackPane, anchorPane, "Members have been exported successfully!");
        Desktop.getDesktop().open(new File("Excel\\AllBooks.xlsx"));
    }

    @FXML
    void Refresh_Books(MouseEvent event) {
        RefrechBookTable();
    }

    public void FillBookTable() {
        Con = MySql.GetConnection();
        RefrechBookTable();
        IdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        ISBNCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        TitleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        AuthorCol.setCellValueFactory(new PropertyValueFactory<>("Author"));
        CategoryCol.setCellValueFactory(new PropertyValueFactory<>("Category"));
        StatusCol.setCellValueFactory(new PropertyValueFactory<>("Status"));
        ShowMoreCol.setCellValueFactory(new PropertyValueFactory<>("Button"));

    }

    public void RefrechBookTable() {
        try {
            BookList.clear();
            Query = "SELECT * FROM `books`";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            Author author;
            Category category;
            String FullName;
            String Status;
            while (resultSet.next()) {
                FullName = resultSet.getString(4);
                String[] NameSplit = FullName.split(" ");
                author = new Author(NameSplit[0], NameSplit[NameSplit.length - 1]);
                category = new Category(resultSet.getString(8));
                if (resultSet.getInt(13) > 0) {
                    Status = "In Stock";
                } else {
                    Status = "Out Of Stock";
                }
                BookList.add(new Book(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        author,
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        category,
                        resultSet.getString(9),
                        resultSet.getInt(10),
                        resultSet.getInt(13),
                        Status,
                        new JFXButton("Show More")
                ));
                //System.out.println(resultSet.getString(10));
                BookTable.setItems(BookList);
            }
        } catch (SQLException ex) {
        }
    }

    public Object getAuthorValue() {
        TablePosition pos = BookTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        Book item = BookTable.getItems().get(row);
        TableColumn col = BookTable.getColumns().get(3);
        Object data = col.getCellObservableValue(item).getValue();
        return data;
    }

    public Object getCategoryValue() {
        TablePosition pos = BookTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        Book item = BookTable.getItems().get(row);
        TableColumn col = BookTable.getColumns().get(4);
        Object data2 = col.getCellObservableValue(item).getValue();
        return data2;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tooltip tooltip = new Tooltip("Refrech Table");
        Tooltip.install(Refrech, tooltip);
        AddBook.setTooltip(new Tooltip("Add a new book"));
        EditBook.setTooltip(new Tooltip("Edit a book"));
        DeleteBook.setTooltip(new Tooltip("Delete a book"));
        ExportBooks.setTooltip(new Tooltip("Export all books To Excel file"));
        SearchFieldBook.setTooltip(new Tooltip("Search for books"));
        FillBookTable();
        book = new Book();
        book.SearchBook(SearchFieldBook, BookList, BookTable);
    }

}
