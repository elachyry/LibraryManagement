package DashboardMember.Controllers;

import DashboardAdmin.Controllers.BooksController;
import static DashboardAdmin.Controllers.FullBookController.Nbr_Pages;
import Data_Base.MySql;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FullBookInfoController implements Initializable {

    @FXML
    private ImageView Close;
    @FXML
    private ImageView BookImage;
    @FXML
    private Label ISBN;
    @FXML
    private Label Title;
    @FXML
    private Label Author;
    @FXML
    private Label Publisher;
    @FXML
    private Label ReleaseDate;
    @FXML
    private Label Language;
    @FXML
    private Label Category;
    @FXML
    private Label NbrOfPages;
    @FXML
    private Label BookShelf;
    @FXML
    private Label Status;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;
    public static int BookId;

    @FXML
    private void Close(MouseEvent event) {
        Stage stage = (Stage) Title.getScene().getWindow();
        stage.close();
    }

    public void FillBookInfo() {
        try {
            Con = MySql.GetConnection();
            Query = "SELECT * FROM `books` WHERE Id = " + BookId;
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            resultSet.next();
            Nbr_Pages = resultSet.getInt(10);
            ISBN.setText(resultSet.getString(2));
            Title.setText(resultSet.getString(3));
            Author.setText(resultSet.getString(4));
            Publisher.setText(resultSet.getString(5));
            ReleaseDate.setText(resultSet.getString(6));
            Language.setText(resultSet.getString(7));
            Category.setText(resultSet.getString(8));
            BookShelf.setText(resultSet.getString(9));
            NbrOfPages.setText(String.valueOf(resultSet.getInt(10)));
            InputStream imageFile = resultSet.getBinaryStream(14);
            Image image = new Image(imageFile);
            BookImage.setImage(image);
            if (resultSet.getInt(13) > 0) {
                Status.setText("In Stock");
                Status.setStyle("-fx-text-fill : green;");
            } else {
                Status.setText("Out Of Stock");
                Status.setStyle("-fx-text-fill : red;");
            }

        } catch (SQLException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Tooltip tooltip = new Tooltip("Exit");
        Tooltip.install(Close, tooltip);
        FillBookInfo();
    }

}
