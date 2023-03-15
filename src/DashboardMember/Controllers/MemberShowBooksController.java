package DashboardMember.Controllers;

import DashboardMember.DashboardController;
import Data_Base.MySql;
import Models.Author;
import Models.Book;
import Models.Category;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ELACHYRY
 */
public class MemberShowBooksController implements Initializable {

    @FXML
    private StackPane stackPane;

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

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;
    Book book = null;
    public static ObservableList<Book> BookList = FXCollections.observableArrayList();
    public static String FiterBy;
    public static int CategoryId;
    public static int AuthorId;

    public void FillBookTable() {
        Con = MySql.GetConnection();
        RefrechBookTable();
        IdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        ISBNCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        TitleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        AuthorCol.setCellValueFactory(new PropertyValueFactory<>("Author"));
        CategoryCol.setCellValueFactory(new PropertyValueFactory<>("Category"));
        StatusCol.setCellValueFactory(new PropertyValueFactory<>("Status"));
        ShowMoreCol.setCellValueFactory(new PropertyValueFactory<>("ShowMore"));

    }

    public void RefrechBookTable() {
        try {
            BookList.clear();
            if ("Author".equals(FiterBy)) {
                Query = "SELECT * FROM `books` WHERE Author_Id = " + AuthorId;
            } else {
                Query = "SELECT * FROM `books` WHERE Category_Id = " + CategoryId;
            }
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

    @FXML
    private void Close(ActionEvent event) {
        Stage stage = (Stage) BookTable.getScene().getWindow();
        stage.close();
        DashboardController.MemberShowBooks = false;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SearchFieldBook.setTooltip(new Tooltip("Search for books"));
        FillBookTable();
    }

}
