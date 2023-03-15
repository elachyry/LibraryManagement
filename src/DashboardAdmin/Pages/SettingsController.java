package DashboardAdmin.Pages;

import Data_Base.MySql;
import Models.Settings;
import Models.Tools;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author ELACHYRY
 */
public class SettingsController implements Initializable {

    @FXML
    private JFXCheckBox ActivateEmails;

    @FXML
    private JFXTextArea NAccountEmail;

    @FXML
    private JFXTextArea RAccountEmail;

    @FXML
    private JFXTextArea DAccountEmail;

    @FXML
    private JFXTextArea MUAccountEmail;

    @FXML
    private JFXButton Update;

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXTextArea NLEmail;

    @FXML
    private JFXTextArea RLEmail;

    @FXML
    private JFXButton Default;

    private JFXSnackbar snackbar;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;

    @FXML
    private void Checked(ActionEvent event) {
        if (!(ActivateEmails.isSelected())) {
            NAccountEmail.setDisable(true);
            RAccountEmail.setDisable(true);
            DAccountEmail.setDisable(true);
            MUAccountEmail.setDisable(true);
            NLEmail.setDisable(true);
            RLEmail.setDisable(true);
            Tools.setSendEmail(false);
        } else {
            if (Tools.netIsAvailable()) {
                NAccountEmail.setDisable(false);
                RAccountEmail.setDisable(false);
                DAccountEmail.setDisable(false);
                MUAccountEmail.setDisable(false);
                NLEmail.setDisable(false);
                RLEmail.setDisable(false);
                Tools.setSendEmail(true);
            } else {
                ActivateEmails.setSelected(false);
                Tools.DialogAlert(stackPane, anchorPane, "You will not be able to send emails.\n"
                        + "Please check your internet connection");
            }
        }
    }

    @FXML
    private void Update(MouseEvent event) {
        String sendEmails = String.valueOf(ActivateEmails.isSelected());
        String NAEmail = NAccountEmail.getText();
        String RLIEmail = RAccountEmail.getText();
        String DAEmail = DAccountEmail.getText();
        String MUAEmail = MUAccountEmail.getText();
        String RLemail = RLEmail.getText();
        String NLemail = NLEmail.getText();
        /* Tools.setNAEmail(NAEmail);
        Tools.setRLIEmail(RLIEmail);
        Tools.setDAEmail(DAEmail);
        Tools.setMUAEmail(MUAEmail);*/
        if (NAEmail.isEmpty() || RLIEmail.isEmpty() || DAEmail.isEmpty() || MUAEmail.isEmpty() || RLemail.isEmpty() || NLemail.isEmpty()) {
            if (NAEmail.isEmpty()) {
                NAccountEmail.setFocusColor(Color.RED);
                NAccountEmail.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(NAccountEmail).play();
                Message();
            } else {
                NAccountEmail.setFocusColor(Color.valueOf("#00b4d8"));
                NAccountEmail.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (RLIEmail.isEmpty()) {
                RAccountEmail.setFocusColor(Color.RED);
                RAccountEmail.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(RAccountEmail).play();
                Message();
            } else {
                RAccountEmail.setFocusColor(Color.valueOf("#00b4d8"));
                RAccountEmail.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (DAEmail.isEmpty()) {
                DAccountEmail.setFocusColor(Color.RED);
                DAccountEmail.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(DAccountEmail).play();
                Message();
            } else {
                DAccountEmail.setFocusColor(Color.valueOf("#00b4d8"));
                DAccountEmail.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }

            if (MUAEmail.isEmpty()) {
                MUAccountEmail.setFocusColor(Color.RED);
                MUAccountEmail.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(MUAccountEmail).play();
                Message();
            } else {
                MUAccountEmail.setFocusColor(Color.valueOf("#00b4d8"));
                MUAccountEmail.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (RLemail.isEmpty()) {
                RLEmail.setFocusColor(Color.RED);
                RLEmail.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(RLEmail).play();
                Message();
            } else {
                RLEmail.setFocusColor(Color.valueOf("#00b4d8"));
                RLEmail.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (NLemail.isEmpty()) {
                NLEmail.setFocusColor(Color.RED);
                NLEmail.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(NLEmail).play();
                Message();
            } else {
                NLEmail.setFocusColor(Color.valueOf("#00b4d8"));
                NLEmail.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
        } else {
            Tools T = new Tools();
            int AdminId = T.getUsernameId("admins");
            Settings settings = new Settings();
            settings.UpdateAdminSettings(AdminId, sendEmails, NAEmail, RLIEmail, DAEmail, MUAEmail, NLemail, RLemail);
            Tools.DialogAlert(stackPane, anchorPane, "You have been update your settings successfully");
            RAccountEmail.setFocusColor(Color.valueOf("#00b4d8"));
            RAccountEmail.setUnFocusColor(Color.valueOf("#4d4d4d"));
            NAccountEmail.setFocusColor(Color.valueOf("#00b4d8"));
            NAccountEmail.setUnFocusColor(Color.valueOf("#4d4d4d"));
            DAccountEmail.setFocusColor(Color.valueOf("#00b4d8"));
            DAccountEmail.setUnFocusColor(Color.valueOf("#4d4d4d"));
            MUAccountEmail.setFocusColor(Color.valueOf("#00b4d8"));
            MUAccountEmail.setUnFocusColor(Color.valueOf("#4d4d4d"));
            NLEmail.setFocusColor(Color.valueOf("#00b4d8"));
            NLEmail.setUnFocusColor(Color.valueOf("#4d4d4d"));
            RLEmail.setFocusColor(Color.valueOf("#00b4d8"));
            RLEmail.setUnFocusColor(Color.valueOf("#4d4d4d"));
        }
    }

    public void FillTextsFields() {
        try {
            Con = MySql.GetConnection();
            Tools T = new Tools();
            int AdminId = T.getUsernameId("admins");
            // System.out.println(AdminId);
            Query = "SELECT * FROM `settings` WHERE `Admin_id` = " + AdminId;
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            resultSet.next();
            if ("false".equals(resultSet.getString(3))) {
                ActivateEmails.setSelected(false);
                NAccountEmail.setDisable(true);
                RAccountEmail.setDisable(true);
                DAccountEmail.setDisable(true);
                MUAccountEmail.setDisable(true);
                NLEmail.setDisable(true);
                RLEmail.setDisable(true);
                Tools.setSendEmail(false);
            } else {
                ActivateEmails.setSelected(true);
                NAccountEmail.setDisable(false);
                RAccountEmail.setDisable(false);
                DAccountEmail.setDisable(false);
                MUAccountEmail.setDisable(false);
                NLEmail.setDisable(false);
                RLEmail.setDisable(false);
                Tools.setSendEmail(true);
            }
            NAccountEmail.setText(resultSet.getString(4));
            RAccountEmail.setText(resultSet.getString(5));
            DAccountEmail.setText(resultSet.getString(6));
            MUAccountEmail.setText(resultSet.getString(7));
            NLEmail.setText(resultSet.getString(8));
            RLEmail.setText(resultSet.getString(9));

        } catch (SQLException e) {

        }
    }

    @FXML
    private void Default(MouseEvent event) {
        if (ActivateEmails.isSelected()) {
            NAccountEmail.setText("You are set. "
                    + "Now you can borrow Books, Check Books List From Your account.   "
                    + "Your login informations:");
            RAccountEmail.setText("An admin reset your login information for you.  "
                    + "Your login informations:");
            DAccountEmail.setText("An admin delete your account.");
            MUAccountEmail.setText("You have update your account informatins successfully.");
            NLEmail.setText("Congratulations!\n"
                    + "Your request to borrow the book has been approved.\n"
                    + "Loan Informations:\n");
            RLEmail.setText("We are so sorry to hear that!\n"
                    + "But we can't approve you request to borrow the book.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        snackbar = new JFXSnackbar(anchorPane);
        if (!(ActivateEmails.isSelected())) {
            NAccountEmail.setDisable(true);
            RAccountEmail.setDisable(true);
            DAccountEmail.setDisable(true);
            MUAccountEmail.setDisable(true);
            NLEmail.setDisable(true);
            RLEmail.setDisable(true);
            Tools.setSendEmail(false);
        } else {
            NAccountEmail.setDisable(false);
            RAccountEmail.setDisable(false);
            DAccountEmail.setDisable(false);
            MUAccountEmail.setDisable(false);
            NLEmail.setDisable(false);
            RLEmail.setDisable(false);
            Tools.setSendEmail(true);
        }
        FillTextsFields();
    }

    public void Message() {
        snackbar.show("All Fields are required!", 2000);
    }

}
