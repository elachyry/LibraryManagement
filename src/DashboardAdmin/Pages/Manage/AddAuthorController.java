package DashboardAdmin.Pages.Manage;

import Data_Base.MySql;
import Models.Author;
import Models.Tools;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AddAuthorController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane anchorePane;

    @FXML
    private JFXTextField LastName;

    @FXML
    private JFXTextField FirstName;

    @FXML
    private JFXTextField Nationality;

    @FXML
    private JFXButton AddButton;

    @FXML
    private JFXButton CloseButton;

    @FXML
    private Label TitleLabel;

    private JFXSnackbar snackbar;

    @FXML
    private JFXTextArea Description;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;
    Author author = null;
    int AuthorId;
    private boolean Edit;

    @FXML
    void Add_Author(ActionEvent event) {
        Con = MySql.GetConnection();
        String Fname = FirstName.getText();
        String Lname = LastName.getText();
        String nationality = Nationality.getText();
        String description = Description.getText();

        if (Fname.isEmpty() || Lname.isEmpty() || nationality.isEmpty()) {
            if (Fname.isEmpty()) {
                FirstName.setFocusColor(Color.RED);
                FirstName.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(FirstName).play();
                Message();
            } else {
                FirstName.setFocusColor(Color.valueOf("#00b4d8"));
                FirstName.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (Lname.isEmpty()) {
                LastName.setFocusColor(Color.RED);
                LastName.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(LastName).play();
                Message();
            } else {
                LastName.setFocusColor(Color.valueOf("#00b4d8"));
                LastName.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }

            if (nationality.isEmpty()) {
                Nationality.setFocusColor(Color.RED);
                Nationality.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(Nationality).play();
                Message();
            } else {
                Nationality.setFocusColor(Color.valueOf("#00b4d8"));
                Nationality.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
        } else {
            author = new Author();
            if (Edit == false) {
                author.addAuthor(Fname, Lname, nationality, 0, description);
                Tools.DialogAlert(stackPane, anchorePane,"Author has been added successfully");
                Clean();
            } else {
                author.UpdateAuthor(AuthorId, Fname, Lname, nationality, 0, description);
                Tools.DialogAlert(stackPane, anchorePane,"Author has been update successfully");

            }
        }

    }

    @FXML
    void Cancel(ActionEvent event) {
        Stage stage = (Stage) Nationality.getScene().getWindow();
        stage.close();
    }

    public void Clean() {
        FirstName.setText(null);
        LastName.setText(null);
        Nationality.setText(null);
        Description.setText(null);

    }

    public void EditData(int id, String Fname, String Lname, String nationality, int nbrBooks, String description) {
        AuthorId = id;
        FirstName.setText(Fname);
        LastName.setText(Lname);
        Nationality.setText(nationality);
        Description.setText(description);
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

    public boolean isEdit() {
        return Edit;
    }

    public void setEdit(boolean Edit) {
        this.Edit = Edit;
    }
    
     public void setFNameDisable(boolean Disable) {
        this.FirstName.setDisable(Disable);
    }
     public void setLNameDisable(boolean Disable) {
        this.LastName.setDisable(Disable);
    }

    public void Message() {
        snackbar.show("All Fields with (*) are required!", 2000);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        snackbar = new JFXSnackbar(anchorePane);
    }

}
