package DashboardAdmin.Pages.Manage;

import Data_Base.MySql;
import Models.Author;
import Models.Book;
import Models.Category;
import Models.Tools;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;

public class AddBookController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane anchorePane;

    @FXML
    private JFXTextField ISBN;

    @FXML
    private JFXTextField Language;

    @FXML
    private JFXTextField Title;

    @FXML
    private JFXComboBox<String> Author;

    @FXML
    private JFXTextField Publisher;

    @FXML
    private JFXTextField ReleaseDate;

    @FXML
    private Label TitleLabel;

    @FXML
    private JFXComboBox<String> Category;

    @FXML
    private JFXTextField NbrPages;

    @FXML
    private JFXTextField NbrCopies;

    @FXML
    private JFXTextField BookShelf;

    @FXML
    private JFXButton AddButton;

    @FXML
    private ImageView AddAuthor;

    @FXML
    private ImageView AddCategory;

    @FXML
    private ImageView BookImage;

    private JFXSnackbar snackbar;

    private File file;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;
    Book book = null;
    public static int BookId;
    public static boolean Edit;
    ObservableList<String> authorList = FXCollections.observableArrayList();
    @FXML
    private JFXButton CloseButton;

    public boolean isEdit() {
        return Edit;
    }

    public void setEdit(boolean Edit) {
        this.Edit = Edit;
    }

    @FXML
    void Add_Book(ActionEvent event) throws SQLException {
        Con = MySql.GetConnection();
        String isbn = ISBN.getText();
        String title = Title.getText();
        String author = String.valueOf(Author.getSelectionModel().getSelectedItem());
        String publisher = Publisher.getText();
        String releaseDate = ReleaseDate.getText();
        String language = Language.getText();
        String category = String.valueOf(Category.getSelectionModel().getSelectedItem());
        String bookShelf = BookShelf.getText();
        String nbrPages = NbrPages.getText();
        String nbrCopies = NbrCopies.getText();

        if (isbn.isEmpty() || title.isEmpty() || Author.getSelectionModel().isEmpty() || publisher.isEmpty() || releaseDate.isEmpty() || language.isEmpty() || Category.getSelectionModel().isEmpty() || bookShelf.isEmpty() || nbrPages.isEmpty() || nbrCopies.isEmpty() || (!(nbrCopies.matches("[0-9]{1,}")) || (!(nbrPages.matches("[0-9]{1,}")))) || ((file == null) && (Edit == false))) {

            if (isbn.isEmpty()) {
                ISBN.setFocusColor(Color.RED);
                ISBN.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(ISBN).play();
                Message();
            } else {
                ISBN.setFocusColor(Color.valueOf("#00b4d8"));
                ISBN.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (title.isEmpty()) {
                Title.setFocusColor(Color.RED);
                Title.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(Title).play();
                Message();
            } else {
                Title.setFocusColor(Color.valueOf("#00b4d8"));
                Title.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (Author.getSelectionModel().isEmpty()) {
                Author.setFocusColor(Color.RED);
                Author.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(Author).play();
                new animatefx.animation.Shake(AddAuthor).play();
                Message();
            } else {
                Author.setFocusColor(Color.valueOf("#00b4d8"));
                Author.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (bookShelf.isEmpty()) {
                BookShelf.setFocusColor(Color.RED);
                BookShelf.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(BookShelf).play();
                Message();
            } else {
                BookShelf.setFocusColor(Color.valueOf("#00b4d8"));
                BookShelf.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (publisher.isEmpty()) {
                Publisher.setFocusColor(Color.RED);
                Publisher.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(Publisher).play();
                Message();
            } else {
                Publisher.setFocusColor(Color.valueOf("#00b4d8"));
                Publisher.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (nbrCopies.isEmpty()) {
                NbrCopies.setFocusColor(Color.RED);
                NbrCopies.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(NbrCopies).play();
                Message();
            } else {
                NbrCopies.setFocusColor(Color.valueOf("#00b4d8"));
                NbrCopies.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (releaseDate.isEmpty()) {
                ReleaseDate.setFocusColor(Color.RED);
                ReleaseDate.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(ReleaseDate).play();
                Message();
            } else {
                ReleaseDate.setFocusColor(Color.valueOf("#00b4d8"));
                ReleaseDate.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (language.isEmpty()) {
                Language.setFocusColor(Color.RED);
                Language.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(Language).play();
                Message();
            } else {
                Language.setFocusColor(Color.valueOf("#00b4d8"));
                Language.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (Category.getSelectionModel().isEmpty()) {
                Category.setFocusColor(Color.RED);
                Category.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(Category).play();
                new animatefx.animation.Shake(AddCategory).play();
                Message();
            } else {
                Category.setFocusColor(Color.valueOf("#00b4d8"));
                Category.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (nbrPages.isEmpty()) {
                NbrPages.setFocusColor(Color.RED);
                NbrPages.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(NbrPages).play();
                Message();
            } else {
                NbrPages.setFocusColor(Color.valueOf("#00b4d8"));
                NbrPages.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }

            if (!(nbrCopies.matches("[0-9]{1,}")) && (!(nbrCopies.isEmpty()))) {
                snackbar.show("Number Of Pages must be numeric!", 2000);
                NbrCopies.setFocusColor(Color.RED);
                NbrCopies.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(NbrCopies).play();
            }

            if (!(nbrPages.matches("[0-9]{1,}")) && (!(nbrPages.isEmpty()))) {
                snackbar.show("Number Of Pages must be numeric!", 2000);
                NbrPages.setFocusColor(Color.RED);
                NbrPages.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(NbrPages).play();
            }
            if (file == null && Edit == false) {
                snackbar.show("Please select an image!", 2000);
                new animatefx.animation.Shake(BookImage).play();
            }

        } else {
            book = new Book();
            Author aut = new Author();
            Category cat = new Category();
            if (Edit == false) {
                book.addBook(isbn, title, author, publisher, releaseDate, language, category, bookShelf, Integer.parseInt(nbrPages), Integer.parseInt(nbrCopies), file);
                //Increment Number Of Books in category and author
                aut.Incr_Dec_nbrOfBooks(author, '+');
                cat.Incr_Dec_nbrOfBooks(category, '+');
                Tools.DialogAlert(stackPane, anchorePane, "Book has been added successfully");
                Clean();
            } else {
                //get Old Author
                Query = "SELECT `Author` FROM `books` WHERE `id` = '" + BookId + "'";
                PreStatment = Con.prepareStatement(Query);
                resultSet = PreStatment.executeQuery();
                resultSet.next();
                String OldAuthor = resultSet.getString(1);
                //System.out.println("OldAuthor " + OldAuthor);
                //System.out.println("Author " + author);
                //Decrement Old Author And Increment new Author
                //get Old Category
                Query = "SELECT `Category` FROM `books` WHERE `id` = '" + BookId + "'";
                PreStatment = Con.prepareStatement(Query);
                resultSet = PreStatment.executeQuery();
                resultSet.next();
                String OldCategory = resultSet.getString(1);
                if (!OldAuthor.equals(author)) {
                    aut.Incr_Dec_nbrOfBooks(OldAuthor, '-');
                }
                if (!OldCategory.equals(category)) {
                    cat.Incr_Dec_nbrOfBooks(OldCategory, '-');
                }
                //System.out.println("OldCategory " + OldCategory);
                //System.out.println("Category " + category);
                //Decrement Old Category And Increment new Category
                book.UpdateBook(BookId, isbn, title, author, publisher, releaseDate, language, category, bookShelf, Integer.parseInt(nbrPages), Integer.parseInt(nbrCopies), file);
                if (!OldAuthor.equals(author)) {
                    aut.Incr_Dec_nbrOfBooks(author, '+');
                }
                if (!OldCategory.equals(category)) {
                    cat.Incr_Dec_nbrOfBooks(category, '+');
                }
                Tools.DialogAlert(stackPane, anchorePane, "Book has been update successfully");

            }

        }
    }

    @FXML
    private void BrowseImage(MouseEvent event) {
        try {
            FileChooser FC = new FileChooser();
            FileChooser.ExtensionFilter ExF1 = new FileChooser.ExtensionFilter("JPG files(*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter ExF2 = new FileChooser.ExtensionFilter("PNG files(*.png)", "*.PNG");
            FC.getExtensionFilters().addAll(ExF1, ExF2);
            file = FC.showOpenDialog(new Stage());
            if (file != null) {
                BufferedImage BF;
                BF = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(BF, null);
                BookImage.setImage(image);
            }
        } catch (IOException e) {
        }
    }

    @FXML
    void Cancel(ActionEvent event) {
        Stage stage = (Stage) Title.getScene().getWindow();
        stage.close();

        /* try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BooksController.class.getResource("/Dashboard/Pages/Books.fxml"));
            StackPane imageOverview = (StackPane) loader.load();

            BooksController controller = loader.getController();
            controller.RefrechBookTable();

            /* FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard/Pages/Books.fxml"));
            Parent root = loader.load();
            BooksController AC = loader.getController();
            AC.RefrechBookTable();
        } catch (IOException e) {
        } */
    }

    public void EditData(int id, String isbn, String title, String author, String publisher, String releaseDate, String language, String category, String bookShelf, int nbrPages, int nbrCopies) {
        BookId = id;
        ISBN.setText(isbn);
        Title.setText(title);
        Author.getSelectionModel().select(author);
        Publisher.setText(publisher);
        ReleaseDate.setText(releaseDate);
        Language.setText(language);
        Category.getSelectionModel().select(category);
        BookShelf.setText(bookShelf);
        NbrPages.setText(String.valueOf(nbrPages));
        NbrCopies.setText(String.valueOf(nbrCopies));
    }

    public Label getTitleLabel() {
        return TitleLabel;
    }

    public void setTitleLabel(String TitleLabel) {
        this.TitleLabel.setText(TitleLabel);
    }

    public void setButtonText(String ButtonText) {
        this.AddButton.setText(ButtonText);
    }

    public void Clean() {
        ISBN.setText(null);
        Title.setText(null);
        Author.setValue(null);
        Publisher.setText(null);
        ReleaseDate.setText(null);
        Language.setText(null);
        Category.setValue(null);
        NbrPages.setText(null);
        NbrCopies.setText(null);
        BookShelf.setText(null);
        BookImage.setImage(new Image("/Images/book.png"));
    }

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
        FillAuthorsComboBox();
    }

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
        FillCategoriesComboBox();
    }

    public void FillAuthorsComboBox() {
        Con = MySql.GetConnection();
        Statement St;
        authorList.clear();
        try {
            ResultSet rs = Con.createStatement().executeQuery("SELECT * FROM `authors`");
            while (rs.next()) {
                authorList.add(rs.getString(2) + " " + rs.getString(3));
            }
        } catch (SQLException ex) {
        }
        Author.setItems(null);
        Author.setItems(authorList);
    }

    public void FillCategoriesComboBox() {
        Con = MySql.GetConnection();
        Statement St;
        ObservableList<String> categoryList = FXCollections.observableArrayList();
        try {
            ResultSet rs = Con.createStatement().executeQuery("SELECT * FROM `categories`");
            while (rs.next()) {
                categoryList.add(rs.getString(2));
            }
        } catch (SQLException ex) {
        }
        Category.setItems(null);
        Category.setItems(categoryList);
    }

    public void Message() {
        snackbar.show("All Fields are required!", 2000);
    }

    public void FillBookImage() {
        Con = MySql.GetConnection();
        Statement St;
        try {
            //System.out.println("book id " + BookId);
            ResultSet rs = Con.createStatement().executeQuery("SELECT * FROM `books` WHERE Id = " + BookId);
            rs.next();
            InputStream imageFile = rs.getBinaryStream(14);
            Image image = new Image(imageFile);
            BookImage.setImage(image);
        } catch (SQLException ex) {
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        snackbar = new JFXSnackbar(anchorePane);
        Tooltip.install(BookImage, new Tooltip("Add an Image"));
        Tooltip.install(AddAuthor, new Tooltip("Add a new Author"));
        Tooltip.install(AddCategory, new Tooltip("Add a new Category"));
        //System.out.println(Edit);
        if (Edit == true) {
            FillBookImage();
        }
        FillAuthorsComboBox();
        FillCategoriesComboBox();
    }

}
