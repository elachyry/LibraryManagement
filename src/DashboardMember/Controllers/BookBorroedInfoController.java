package DashboardMember.Controllers;

import DashboardAdmin.Controllers.BooksController;
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

public class BookBorroedInfoController implements Initializable {

    @FXML
    private ImageView BookImage;
    @FXML
    private Label Title;
    @FXML
    private Label Author;
    @FXML
    private Label BorrowingDate;
    @FXML
    private Label NbrDays;
    @FXML
    private Label ReturnDate;
    @FXML
    private Label Status;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;
    public static int BookId;
    public static int MemberId;
    @FXML
    private ImageView Close;

    public void FillBookInfo() {
        try {
            Con = MySql.GetConnection();
            Query = "SELECT DISTINCT Title, Author, Borrowing_Date, Number_Of_Days, Return_Date, "
                    + "Status , Book_Image FROM books_borrowed bb, books b, members m WHERE bb.Book_Id = b.Id "
                    + "AND bb.Member_Id = " + MemberId + "  AND b.Id = " + BookId;
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            resultSet.next();
            Title.setText(resultSet.getString(1));
            Author.setText(resultSet.getString(2));
            NbrDays.setText(resultSet.getString(4));
            BorrowingDate.setText(String.valueOf(resultSet.getDate(3)));
            ReturnDate.setText(String.valueOf(resultSet.getDate(5)));
            Status.setText(resultSet.getString(6));
            InputStream imageFile = resultSet.getBinaryStream(7);
            Image image = new Image(imageFile);
            BookImage.setImage(image);
            if ("Out Of Stock".equals(Status)) {
                Status.setStyle("-fx-text-fill : red;");
            } else {
                Status.setStyle("-fx-text-fill : green;");
            }

        } catch (SQLException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tooltip tooltip = new Tooltip("Exit");
        Tooltip.install(Close, tooltip);
        FillBookInfo();
    }

    @FXML
    private void Close(MouseEvent event) {
        Stage stage = (Stage) Title.getScene().getWindow();
        stage.close();
    }

}
