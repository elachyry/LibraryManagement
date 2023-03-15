package DashboardMember.Controllers;

import Models.Book;
import Models.Books_Borrowed;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DialogConfirmationController implements Initializable {

    @FXML
    private ImageView Close;
    @FXML
    private Label Message;

    public static int Id_Book = 0;
    public static int Id_Loan = 0;
    public static String Status = null;
    public static int Cancel_Loan = 0;
    public static int Admin = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void SetMessage(String Message) {
        this.Message.setText(Message);
    }

    @FXML
    private void Close(MouseEvent event) {
        Stage stage = (Stage) Close.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void YesButton(MouseEvent event) throws IOException {
        Books_Borrowed BB = new Books_Borrowed();
        Book b = new Book();
        if (Cancel_Loan == 0) {
            Books_Borrowed.Admin = Admin;
            BB.addLoan();
        } else {
            BB.EditStatusLoan(Id_Loan, "Canceled");
        }

        Stage stage = (Stage) Close.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void NoButton(MouseEvent event) {
        Stage stage = (Stage) Close.getScene().getWindow();
        stage.close();
    }

}
