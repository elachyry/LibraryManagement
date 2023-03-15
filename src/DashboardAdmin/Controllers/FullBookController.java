package DashboardAdmin.Controllers;

import DashboardMember.DashboardController;
import Data_Base.MySql;
import Models.Books_Borrowed;
import Models.Tools;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ELACHYRY
 */
public class FullBookController implements Initializable {

    @FXML
    private Label ISBN;

    @FXML
    private Label Author;

    @FXML
    private Label Title;

    @FXML
    private Label Publisher;

    @FXML
    private Label ReleaseDate;

    @FXML
    private Label Category;

    @FXML
    private Label BookShelf;

    @FXML
    private Label Language;

    @FXML
    private Label NbrOfPages;

    @FXML
    private Label NbrOfCopies;

    @FXML
    private ImageView BookImage;

    @FXML
    private JFXButton BorrowBook;

    @FXML
    private HBox VBoxButt;

    public static int BookId;
    public static int Nbr_Pages;
    private int Nbr_Copies;
    private String Book_Title;
    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;
    @FXML
    private JFXDialog Dialog2;
    @FXML
    private Label DialogLabel;
    @FXML
    private JFXButton YesButton;
    @FXML
    private JFXButton NoButton;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private StackPane stackPane;

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
            Nbr_Copies = resultSet.getInt(13);
            Book_Title = resultSet.getString(3);
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
            NbrOfCopies.setText(String.valueOf(resultSet.getInt(13)));
            InputStream imageFile = resultSet.getBinaryStream(14);
            Image image = new Image(imageFile);
            BookImage.setImage(image);

        } catch (SQLException ex) {
        }
    }

    @FXML
    private void Borrow(MouseEvent event) {
        int Count = new Tools().TestLoanExist(Book_Title);
        int CountLoan = new Tools().TestLoanCount(BookId);
        if (Nbr_Copies > 0) {
            if (CountLoan <= 5) {
                if (Count == 0) {
                    BoxBlur Blur = new BoxBlur(3, 3, 3);
                    anchorPane.setEffect(Blur);
                    YesButton.setPrefSize(140, 40);
                    YesButton.setFont(new Font(17));
                    YesButton.setCursor(Cursor.HAND);
                    NoButton.setPrefSize(140, 40);
                    NoButton.setFont(new Font(17));
                    NoButton.setCursor(Cursor.HAND);
                    DialogLabel.setText("Do you want to borrow this Book?");
                    DialogLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-font-size: 20px; -fx-text-fill : #52438F;");
                    Dialog2.setTransitionType(JFXDialog.DialogTransition.TOP);
                    Dialog2.setDialogContainer(stackPane);
                    Dialog2.show();
                    YesButton.setOnAction((Event) -> {
                        Books_Borrowed BB = new Books_Borrowed();
                        BB.addLoan();
                        anchorPane.setEffect(null);
                        Dialog2.close();
                        Stage stage = (Stage) Title.getScene().getWindow();
                        stage.close();
                    });
                    Dialog2.setOnDialogClosed((JFXDialogEvent Event) -> {
                        anchorPane.setEffect(null);
                    });
                    NoButton.setOnAction((Event) -> {
                        anchorPane.setEffect(null);
                        Dialog2.close();
                    });
                } else {
                    Tools.DialogAlert(stackPane, anchorPane, "You can't borrow this book, because You have a loan with the same book that is still in progress!");
                }
            } else {
                Tools.DialogAlert(stackPane, anchorPane, "You can't borrow five books at once, return one book to borrow another!");

            }
        } else {
            Tools.DialogAlert(stackPane, anchorPane, "You can't borrow this book, because it's out of stock!");
        }
    }

    /*public void FillBookImage() {
        Connection Con = MySql.GetConnection();
        Statement St;
        System.out.println("book id fullbook" + BookId);
        try {
            ResultSet rs = Con.createStatement().executeQuery("SELECT * FROM `books` WHERE Id = " + BookId);
            rs.next();
            InputStream imageFile = rs.getBinaryStream(14);
            Image image = new Image(imageFile);
            BookImage.setImage(image);
        } catch (SQLException ex) {
        }
    }*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (DashboardController.IsMember) {
            BorrowBook.setVisible(true);
        } else {
            BorrowBook.setVisible(false);
            VBoxButt.setAlignment(Pos.CENTER_LEFT);
        }
        FillBookInfo();
        //FillBookImage();
    }
}
