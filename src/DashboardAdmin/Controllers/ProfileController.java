package DashboardAdmin.Controllers;

import Data_Base.MySql;
import Models.Tools;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class ProfileController implements Initializable {

    @FXML
    private JFXTextField CNE;

    @FXML
    private JFXTextField FirstName;

    @FXML
    private JFXTextField LastName;

    @FXML
    private JFXTextField Mobile;

    @FXML
    private JFXTextField CEmail;

    @FXML
    private JFXTextField Email;

    @FXML
    private JFXTextField Username;

    @FXML
    private JFXPasswordField Password;

    @FXML
    private JFXPasswordField CPassword;

    @FXML
    private JFXTextField Address;

    private HBox hbox;

    @FXML
    private JFXButton Update;

    @FXML
    private AnchorPane anchorePane;

    @FXML
    private StackPane stackPane;

    @FXML
    private Circle UserImage;

    private JFXSnackbar snackbar;

    private File file;
    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;
    InputStream is = null;

    @FXML
    void Update_Profile(ActionEvent event) throws FileNotFoundException {
        String cne = CNE.getText();
        String Fname = FirstName.getText();
        String Lname = LastName.getText();
        String email = Email.getText();
        String cemail = CEmail.getText();
        String mobile = Mobile.getText();
        String address = Address.getText();
        String username = Username.getText();
        String Newpassword = CPassword.getText();
        String OldPassword = Password.getText();
        Tools T = new Tools();
        int Id = T.getUsernameId("admins");
        String CheckPassword = Tools.getOldPassword();

        if (cne.isEmpty() || Fname.isEmpty() || Lname.isEmpty() || email.isEmpty() || cemail.isEmpty() || mobile.isEmpty() || username.isEmpty() || Newpassword.isEmpty() || OldPassword.isEmpty() || (!(OldPassword.equals(CheckPassword)) || (!(email.equals(cemail))) || (!(username.matches("[a-zA-Z0-9_]{4,}"))) || (!(email.matches("[a-zA-Z0-9_@.]{10,}"))) || (!(mobile.matches("[0-9+]{9,}"))))) {
            if (cne.isEmpty()) {
                CNE.setFocusColor(Color.RED);
                CNE.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(CNE).play();
                Message();
            } else {
                CNE.setFocusColor(Color.valueOf("#00b4d8"));
                CNE.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
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

            if (email.isEmpty()) {
                Email.setFocusColor(Color.RED);
                Email.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(Email).play();
                Message();
            } else {
                Email.setFocusColor(Color.valueOf("#00b4d8"));
                Email.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (cemail.isEmpty()) {
                CEmail.setFocusColor(Color.RED);
                CEmail.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(CEmail).play();
                Message();
            } else {
                CEmail.setFocusColor(Color.valueOf("#00b4d8"));
                CEmail.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }

            if (Newpassword.isEmpty()) {
                Password.setFocusColor(Color.RED);
                Password.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(Password).play();
                Message();
            } else {
                Password.setFocusColor(Color.valueOf("#00b4d8"));
                Password.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (Username.getText().isEmpty()) {
                Username.setFocusColor(Color.RED);
                Username.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(Username).play();
                Message();
            } else {
                Username.setFocusColor(Color.valueOf("#00b4d8"));
                Username.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (address.isEmpty()) {
                Address.setFocusColor(Color.RED);
                Address.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(Address).play();
                Message();
            } else {
                Address.setFocusColor(Color.valueOf("#00b4d8"));
                Address.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (Mobile.getText().isEmpty()) {
                Mobile.setFocusColor(Color.RED);
                Mobile.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(Mobile).play();
                Message();
            } else {
                Mobile.setFocusColor(Color.valueOf("#00b4d8"));
                Mobile.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (CPassword.getText().isEmpty()) {
                CPassword.setFocusColor(Color.RED);
                CPassword.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(CPassword).play();
                Message();
            } else {
                CPassword.setFocusColor(Color.valueOf("#00b4d8"));
                CPassword.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (!(email.equals(cemail) && (!(email.isEmpty())) && (!(cemail.isEmpty())))) {
                Email.setFocusColor(Color.RED);
                Email.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(Email).play();
                CEmail.setFocusColor(Color.RED);
                CEmail.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(CEmail).play();
                if ((email.isEmpty()) || (cemail.isEmpty())) {
                    Message();
                } else {
                    snackbar.show("Those emails didn’t match!", 2000);
                }

            } else {
                CEmail.setFocusColor(Color.valueOf("#00b4d8"));
                CEmail.setUnFocusColor(Color.valueOf("#4d4d4d"));
                Email.setFocusColor(Color.valueOf("#00b4d8"));
                Email.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }

            if (!(OldPassword.equals(CheckPassword))) {
                Password.setFocusColor(Color.RED);
                Password.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(Password).play();
                if ((Password.getText().isEmpty())) {
                    Message();
                } else {
                    snackbar.show("Old password is incorrect!", 2000);
                }

            } else {
                CPassword.setFocusColor(Color.valueOf("#00b4d8"));
                CPassword.setUnFocusColor(Color.valueOf("#4d4d4d"));
                Password.setFocusColor(Color.valueOf("#00b4d8"));
                Password.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }

            if (!(username.matches("[a-zA-Z0-9_]{4,}")) && (!(username.isEmpty()))) {
                snackbar.show("Username is incorrect!", 2000);
                Username.setFocusColor(Color.RED);
                Username.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(Username).play();
            }
            if (!(email.matches("[a-zA-Z0-9_@.]{10,}")) && (!(email.isEmpty()))) {
                snackbar.show("Email is incorrect!", 2000);
                Email.setFocusColor(Color.RED);
                Email.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(LastName).play();
            }
            if (!(mobile.matches("[0-9+]{9,}")) && (!(mobile.isEmpty()))) {
                snackbar.show("Mobile is incorrect!", 2000);
                Mobile.setFocusColor(Color.RED);
                Mobile.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(Mobile).play();
            }
        } else {
            FileInputStream fin = null;
            int len = 0;
            if (file != null) {
                fin = new FileInputStream(file);
                len = (int) file.length();

                Con = MySql.GetConnection();
                Query = "UPDATE `admins` SET `CNE`=?,"
                        + "`First_Name`=?,`Last_Name`=?,"
                        + "`Email`=?,`Address`=?,`Mobile`=?,"
                        + "`Username`=?,`Password`=?, `Profile_Image`=? "
                        + "WHERE `Id`='" + Id + "'";
            } else {
                Query = "UPDATE `admins` SET `CNE`=?,"
                        + "`First_Name`=?,`Last_Name`=?,"
                        + "`Email`=?,`Address`=?,`Mobile`=?,"
                        + "`Username`=?,`Password`=?"
                        + "WHERE `Id`='" + Id + "'";
            }
            try {
                PreStatment = Con.prepareStatement(Query);
                PreStatment.setString(1, cne);
                PreStatment.setString(2, Fname);
                PreStatment.setString(3, Lname);
                PreStatment.setString(4, email);
                PreStatment.setString(5, address);
                PreStatment.setString(6, mobile);
                PreStatment.setString(7, username);
                PreStatment.setString(8, Tools.EncryptPassword(Newpassword));
                if (file != null) {
                    PreStatment.setBinaryStream(9, fin, len);
                }
                //  PreStatment.setBinaryStream(9, fin, len);
                PreStatment.execute();
            } catch (SQLException ex) {
            }
            Tools.DialogAlert(stackPane, anchorePane, "You have been update your account successfully");
            try {
                Tools.SendEmail(email, cne, Fname, Lname, "", "", "", "User Update Account");
            } catch (Exception ex) {
            }
            Password.setText(null);
            CPassword.setText(null);
            CEmail.setText(null);
            CEmail.setFocusColor(Color.valueOf("#00b4d8"));
            CEmail.setUnFocusColor(Color.valueOf("#4d4d4d"));
            Email.setFocusColor(Color.valueOf("#00b4d8"));
            Email.setUnFocusColor(Color.valueOf("#4d4d4d"));
            CNE.setFocusColor(Color.valueOf("#00b4d8"));
            CNE.setUnFocusColor(Color.valueOf("#4d4d4d"));
            FirstName.setFocusColor(Color.valueOf("#00b4d8"));
            FirstName.setUnFocusColor(Color.valueOf("#4d4d4d"));
            LastName.setFocusColor(Color.valueOf("#00b4d8"));
            LastName.setUnFocusColor(Color.valueOf("#4d4d4d"));
            Mobile.setFocusColor(Color.valueOf("#00b4d8"));
            Mobile.setUnFocusColor(Color.valueOf("#4d4d4d"));
            Address.setFocusColor(Color.valueOf("#00b4d8"));
            Address.setUnFocusColor(Color.valueOf("#4d4d4d"));
            CPassword.setFocusColor(Color.valueOf("#00b4d8"));
            CPassword.setUnFocusColor(Color.valueOf("#4d4d4d"));
            Password.setFocusColor(Color.valueOf("#00b4d8"));
            Password.setUnFocusColor(Color.valueOf("#4d4d4d"));
            Username.setFocusColor(Color.valueOf("#00b4d8"));
            Username.setUnFocusColor(Color.valueOf("#4d4d4d"));
        }
    }

    public void FillTextsFields() throws FileNotFoundException, IOException {
        try {

            Tools T = new Tools();
            int Id = T.getUsernameId("admins");
            Con = MySql.GetConnection();
            Query = "SELECT * FROM `admins` WHERE Id = " + Id;
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            resultSet.next();
            CNE.setText(resultSet.getString(2));
            FirstName.setText(resultSet.getString(3));
            LastName.setText(resultSet.getString(4));
            Email.setText(resultSet.getString(5));
            Address.setText(resultSet.getString(6));
            Mobile.setText(resultSet.getString(7));
            Username.setText(resultSet.getString(8));

        } catch (SQLException e) {

        }
    }

    public void Message() {
        snackbar.show("All Fields are required!", 2000);
    }

    public void FillUserImage() {
        Con = MySql.GetConnection();
        Statement St;
        try {
            //System.out.println("User id " + new Tools().getUsernameId("admins"));
            ResultSet rs = Con.createStatement().executeQuery("SELECT * FROM `admins` WHERE Id = " + new Tools().getUsernameId("admins"));
            rs.next();
            InputStream imageFile = rs.getBinaryStream(10);
            if (imageFile != null) {
                Image image = new Image(imageFile);
                UserImage.setStroke(null);
                UserImage.setFill(new ImagePattern(image));
                UserImage.setEffect(new DropShadow(+25d, 0d, +2d, Color.AZURE));
            } else {
                UserImage.setStroke(null);
                UserImage.setFill(new ImagePattern(new Image("/Images/user_100px.png")));
                //UserImage.setEffect(new DropShadow(+25d, 0d, +2d, Color.PURPLE));
            }
        } catch (SQLException ex) {
        }
    }

    @FXML
    private void SelectImage(MouseEvent event) {
        try {
            FileChooser FC = new FileChooser();
            FileChooser.ExtensionFilter ExF1 = new FileChooser.ExtensionFilter("JPG files(*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter ExF2 = new FileChooser.ExtensionFilter("PNG files(*.png)", "*.PNG");
            FC.getExtensionFilters().addAll(ExF1, ExF2);
            file = FC.showOpenDialog(new Stage());
            BufferedImage BF;
            BF = ImageIO.read(file);
            Image image = SwingFXUtils.toFXImage(BF, null);
            UserImage.setStroke(null);
            UserImage.setFill(new ImagePattern(image));
            UserImage.setEffect(new DropShadow(+25d, 0d, +2d, Color.AZURE));
        } catch (IOException e) {
        }
    }

    @FXML
    private void Checked(MouseEvent event) {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tooltip.install(UserImage, new Tooltip("Change Profile Image"));
        snackbar = new JFXSnackbar(hbox);
        FillUserImage();
        try {
            FillTextsFields();
        } catch (IOException ex) {
        }

    }

}
