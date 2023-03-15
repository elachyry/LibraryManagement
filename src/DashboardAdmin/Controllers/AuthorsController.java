package DashboardAdmin.Controllers;

import DashboardAdmin.Pages.Manage.AddAuthorController;
import Data_Base.MySql;
import Models.Author;
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

public class AuthorsController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXTextField SearchFieldAuthor;

    @FXML
    private TableView<Author> AuthorTable;

    @FXML
    private TableColumn<Author, Integer> IdCol;

    @FXML
    private TableColumn<Author, String> FNameCol;

    @FXML
    private TableColumn<Author, String> LNameCol;

    @FXML
    private TableColumn<Author, String> NationalityCol;

    @FXML
    private TableColumn<Author, Integer> NbrBooksCol;

    @FXML
    private TableColumn<Author, String> DescriptionCol;

    @FXML
    private TableColumn<Author, JFXButton> ShowBooksCol;

    @FXML
    private JFXDialog Dialog2;

    @FXML
    private Label DialogLabel;

    @FXML
    private JFXButton YesButton;

    @FXML
    private JFXButton NoButton;

    @FXML
    private ImageView Refrech;

    @FXML
    private JFXButton AddAuthor;

    @FXML
    private JFXButton EditAuthor;

    @FXML
    private JFXButton DeleteAuthor;

    @FXML
    private JFXButton ExportAuthors;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;
    Author author = null;
    public static ObservableList<Author> AuthorList = FXCollections.observableArrayList();

    @FXML
    void Add_Author(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/DashboardAdmin/Pages/Manage/AddAuthor.fxml"));
            DialogPane AddAuthorDialog = loader.load();
            AddAuthorController AAC = loader.getController();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add new Author");
            dialog.initStyle(StageStyle.TRANSPARENT);
            dialog.setDialogPane(AddAuthorDialog);
            Optional<ButtonType> Result = dialog.showAndWait();

        } catch (IOException e) {
        }
    }

    @FXML
    void Edit_Author(MouseEvent event) {
        author = AuthorTable.getSelectionModel().getSelectedItem();
        if (author != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/DashboardAdmin/Pages/Manage/AddAuthor.fxml"));
                DialogPane AddAuthorDialog = loader.load();
                AddAuthorController AAC = loader.getController();
                AAC.setEdit(true);
                AAC.EditData(author.getId(), author.getFirst_Name(), author.getLast_Name(), author.getNationality(), author.getNumber_Of_Books(), author.getDescription());
                AAC.setTitleLabel("Edit Author");
                AAC.setButtonText("Edit");
                AAC.setFNameDisable(true);
                AAC.setLNameDisable(true);
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Edit Author");
                dialog.initStyle(StageStyle.TRANSPARENT);
                dialog.setDialogPane(AddAuthorDialog);
                Optional<ButtonType> Result = dialog.showAndWait();
            } catch (IOException e) {
            }
        } else {
            Tools.DialogAlert(stackPane, anchorPane, "No row selected!");
        }
    }

    @FXML
    void Delete_Author(MouseEvent event) {
        author = AuthorTable.getSelectionModel().getSelectedItem();
        if (author != null) {
            if (author.getNumber_Of_Books() != 0) {
                Tools.DialogAlert(stackPane, anchorPane, "This author  cannot be removed, because he has books");
            } else {
                BoxBlur Blur = new BoxBlur(3, 3, 3);
                anchorPane.setEffect(Blur);
                YesButton.setPrefSize(140, 40);
                YesButton.setFont(new Font(17));
                YesButton.setCursor(Cursor.HAND);
                NoButton.setPrefSize(140, 40);
                NoButton.setFont(new Font(17));
                NoButton.setCursor(Cursor.HAND);
                DialogLabel.setText("Do you want to delete this Author?");
                DialogLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-font-size: 20px; -fx-text-fill : #52438F;");
                Dialog2.setTransitionType(JFXDialog.DialogTransition.TOP);
                Dialog2.setDialogContainer(stackPane);
                Dialog2.show();
                YesButton.setOnAction((Event) -> {
                    author.DeleteAuthor(author.getId());
                    RefrechAuthorTable();
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
            }

        } else {
            Tools.DialogAlert(stackPane, anchorPane, "No row selected!");
        }
    }

    @FXML
    void Export_Author(MouseEvent event) throws IOException {
        author = new Author();
        author.ExportAuthors();
        Tools.DialogAlert(stackPane, anchorPane, "Authors have been exported successfully!");
        Desktop.getDesktop().open(new File("Excel\\Authors.xlsx"));
    }

    @FXML
    void Refresh_Authors(MouseEvent event) {
        RefrechAuthorTable();
    }

    public void FillAuthorTable() {
        Con = MySql.GetConnection();
        RefrechAuthorTable();
        IdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        FNameCol.setCellValueFactory(new PropertyValueFactory<>("First_Name"));
        LNameCol.setCellValueFactory(new PropertyValueFactory<>("Last_Name"));
        NationalityCol.setCellValueFactory(new PropertyValueFactory<>("Nationality"));
        NbrBooksCol.setCellValueFactory(new PropertyValueFactory<>("Number_Of_Books"));
        DescriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        ShowBooksCol.setCellValueFactory(new PropertyValueFactory<>("ShowBooks"));

    }

    public void RefrechAuthorTable() {
        try {
            AuthorList.clear();

            /* Query = "SELECT *\n"
                    + "FROM\n"
                    + "    (select a.Id, count(*) Number_Of_Books\n"
                    + "    from authors a\n"
                    + "    inner join books b ON b.Author_Id = a.Id\n"
                    + "    group by a.Id) ab\n"
                    + "JOIN authors a ON a.Id = ab.Id";*/
            Query = "SELECT * FROM `authors`";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();

            while (resultSet.next()) {
                AuthorList.add(new Author(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getInt(5),
                        resultSet.getString(6),
                        new JFXButton("ShowBooks")
                ));
                AuthorTable.setItems(AuthorList);
            }
        } catch (SQLException ex) {
        }
    }

    /* public void Get_Book_Count() {
        
        try {
            Query = "SELECT COUNT(*) AS Author_Book_Ccount FROM `authors`";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            while(resultSet.next()){
                
            }
        } catch (SQLException ex) {
        }
        
    }*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tooltip tooltip = new Tooltip("Refrech Table");
        Tooltip.install(Refrech, tooltip);
        AddAuthor.setTooltip(new Tooltip("Add a new author"));
        EditAuthor.setTooltip(new Tooltip("Edit an author"));
        DeleteAuthor.setTooltip(new Tooltip("Delete an author"));
        ExportAuthors.setTooltip(new Tooltip("Export all authors To Excel file"));
        SearchFieldAuthor.setTooltip(new Tooltip("Search for authors"));
        FillAuthorTable();
        author = new Author();
        author.SearchAuthor(SearchFieldAuthor, AuthorList, AuthorTable);
    }

}
