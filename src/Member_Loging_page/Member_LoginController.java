package Member_Loging_page;

import static Data_Base.MySql.con;
import Models.Tools;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ELACHYRY
 */
public class Member_LoginController implements Initializable {

    @FXML
    private VBox Right_VB;

    @FXML
    private FontAwesomeIconView User;

    @FXML
    private JFXTextField Txt_User;

    @FXML
    private JFXPasswordField Txt_PW;

    JFXSnackbar snackbar;

    @FXML
    private StackPane stackPane;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    void Btn_Connecter(ActionEvent event) {
        String Username = Txt_User.getText();
        String Password = Txt_PW.getText();
        if (Username.isEmpty()) {
            Txt_User.setFocusColor(Color.RED);
            Txt_User.setUnFocusColor(Color.RED);
            new animatefx.animation.Shake(Txt_User).play();
        } else {
            Txt_User.setFocusColor(Color.valueOf("#4059a9"));
            Txt_User.setUnFocusColor(Color.valueOf("#4d4d4d"));
        }
        if (Password.isEmpty()) {
            Txt_PW.setFocusColor(Color.RED);
            Txt_PW.setUnFocusColor(Color.RED);
            new animatefx.animation.Shake(Txt_PW).play();
        } else {
            Txt_PW.setFocusColor(Color.valueOf("#4059a9"));
            Txt_PW.setUnFocusColor(Color.valueOf("#4d4d4d"));
        }
        if (Username.isEmpty()) {
            snackbar.show("Username is empty!", 2000);
            return;
        }
        if (Password.isEmpty()) {
            snackbar.show("Password is empty!", 2000);
            return;
        }

        int status = Check_Login(Username.trim().toLowerCase(), Password);
        switch (status) {
            case 0: {

//                System.out.println(T.getUsernameId("admins"));
                /* try {
                    Stage stage = (Stage) Txt_PW.getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource("/Dashboard/FXMLDocument.fxml"));
                    stage.setTitle("Gestion Biblio");
                    stage.setFullScreen(true);
                    stage.setMaximized(true);
                    stage.setScene(new Scene(root));
                } catch (IOException ioex) {
                }*/
                //Send username to Tools calss for using in ProfileController
                Tools T = new Tools(Username);
                try {
                    Stage stage1 = (Stage) Txt_PW.getScene().getWindow();
                    stage1.close();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/DashboardMember/DashboardMember.fxml"));
                    loader.load();
                    Stage stage = new Stage();
                    Parent parent = loader.getRoot();
                    stage.setScene(new Scene(parent));
                    stage.getIcons().add(new Image("/Images/reading_127px.png"));
                    stage.setMaximized(true);
                    stage.setResizable(false);
                    stage.show();
                } catch (IOException ex) {
                }

            }
            break;
            case 1: {
                snackbar.show("Username Or Password incorrect!", 2000);
            }
            break;
            case -1: {
                snackbar.show("Login failed!", 2000);
            }
            break;
        }
    }

    public int Check_Login(String Username, String Password) {
        Connection Con = con;
        String SQL = null;

        if (Con == null) {
            return -1;
        }
        SQL = "SELECT * FROM members WHERE Username = ? AND Password = ?";

        try {
            PreparedStatement prest = Con.prepareStatement(SQL);
            prest.setString(1, Username);
            prest.setString(2, Tools.EncryptPassword(Password));
            ResultSet res = prest.executeQuery();
            while (res.next()) {
                return 0;
            }

        } catch (SQLException ex) {
        }
        return 1;
    }

    @FXML
    void btn_click(ActionEvent event) {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        snackbar = new JFXSnackbar(Right_VB);

    }

    @FXML
    private void FPass(MouseEvent event) {
        Tools.DialogAlert(stackPane, anchorPane, "Please contact the administrator!");
    }

}
