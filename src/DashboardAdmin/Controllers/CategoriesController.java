package DashboardAdmin.Controllers;

import DashboardAdmin.Pages.Manage.AddCategoryController;
import Data_Base.MySql;
import Models.Category;
import Models.Tools;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
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

public class CategoriesController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXTextField SearchFieldCategory;

    @FXML
    private TableView<Category> CategoryTable;

    @FXML
    private TableColumn<Category, Integer> IdCol;

    @FXML
    private TableColumn<Category, String> NameCol;

    @FXML
    private TableColumn<Category, Integer> NbrBooksCol;

    @FXML
    private TableColumn<Category, String> DescriptionCol;
    
    @FXML
    private TableColumn<Category, JFXButton> ShowBooksCol;

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
    private JFXButton AddCategory;

    @FXML
    private JFXButton EditCategory;

    @FXML
    private JFXButton DeleteCategory;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;
    Category category = null;
    public static ObservableList<Category> CategoryList = FXCollections.observableArrayList();

    @FXML
    void Add_Category(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/DashboardAdmin/Pages/Manage/AddCategory.fxml"));
            DialogPane AddCategoryDialog = loader.load();
            AddCategoryController ACC = loader.getController();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add new Category");
            dialog.initStyle(StageStyle.TRANSPARENT);
            dialog.setDialogPane(AddCategoryDialog);
            Optional<ButtonType> Result = dialog.showAndWait();
        } catch (IOException e) {
        }
    }

    @FXML
    void Delete_Category(MouseEvent event) {
        category = CategoryTable.getSelectionModel().getSelectedItem();
        if (category != null) {
            if (category.getNumber_Of_Books() != 0) {
                Tools.DialogAlert(stackPane, anchorPane, "This category  cannot be removed, because it has books");
            } else {
                BoxBlur Blur = new BoxBlur(3, 3, 3);
                anchorPane.setEffect(Blur);
                YesButton.setPrefSize(140, 40);
                YesButton.setFont(new Font(17));
                YesButton.setCursor(Cursor.HAND);
                NoButton.setPrefSize(140, 40);
                NoButton.setFont(new Font(17));
                NoButton.setCursor(Cursor.HAND);
                DialogLabel.setText("Do you want to delete this Category?");
                DialogLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-font-size: 20px; -fx-text-fill : #52438F;");
                Dialog2.setTransitionType(JFXDialog.DialogTransition.TOP);
                Dialog2.setDialogContainer(stackPane);
                Dialog2.show();
                YesButton.setOnAction((Event) -> {
                    category.DeleteCategory(category.getId());
                    RefrechCategoryTable();
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
    void Edit_Category(MouseEvent event) {
        category = CategoryTable.getSelectionModel().getSelectedItem();
        if (category != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/DashboardAdmin/Pages/Manage/AddCategory.fxml"));
                DialogPane AddCategoryDialog = loader.load();
                AddCategoryController ACC = loader.getController();
                ACC.setEdit(true);
                ACC.EditData(category.getId(), category.getName(), category.getNumber_Of_Books(), category.getDescription());
                ACC.setTitleLabel("Edit Category");
                ACC.setButtonText("Edit");
                ACC.setNameDisable(true);
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Edit Category");
                            dialog.initStyle(StageStyle.TRANSPARENT);
                dialog.setDialogPane(AddCategoryDialog);
                Optional<ButtonType> Result = dialog.showAndWait();
            } catch (IOException e) {
            }
        } else {
            Tools.DialogAlert(stackPane, anchorPane, "No row selected!");
        }
    }

    @FXML
    void Refresh_Categories(MouseEvent event) {
        FillCategoryTable();
    }

    public void FillCategoryTable() {
        Con = MySql.GetConnection();
        RefrechCategoryTable();
        IdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        NameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        NbrBooksCol.setCellValueFactory(new PropertyValueFactory<>("Number_Of_Books"));
        DescriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        ShowBooksCol.setCellValueFactory(new PropertyValueFactory<>("ShowBooks"));

    }

    public void RefrechCategoryTable() {
        try {
            CategoryList.clear();
            Query = "SELECT * FROM `categories`";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();

            while (resultSet.next()) {
                CategoryList.add(new Category(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        new JFXButton("ShowBooks")
                ));
                CategoryTable.setItems(CategoryList);
            }
        } catch (SQLException ex) {
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tooltip tooltip = new Tooltip("Refrech Table");
        Tooltip.install(Refrech, tooltip);
        AddCategory.setTooltip(new Tooltip("Add a new category"));
        EditCategory.setTooltip(new Tooltip("Edit a category"));
        DeleteCategory.setTooltip(new Tooltip("Delete a category"));
        SearchFieldCategory.setTooltip(new Tooltip("Search for categories"));
        FillCategoryTable();
        category = new Category();
        category.SearchCategory(SearchFieldCategory, CategoryList, CategoryTable);
    }

}
