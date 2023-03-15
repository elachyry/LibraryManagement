package DashboardAdmin.Pages.Manage;

import Data_Base.MySql;
import Models.Admin;
import Models.Member;
import Models.Settings;
import Models.Tools;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AddMemberController implements Initializable {

    @FXML
    private AnchorPane anchorePane;

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField CNE;

    @FXML
    private JFXTextField Email;

    @FXML
    private JFXTextField FirstName;

    @FXML
    private JFXTextField LastName;

    @FXML
    private JFXTextField Address;

    @FXML
    private JFXTextField Mobile;

    @FXML
    private JFXTextField Cemail;

    @FXML
    private JFXButton ButtonText;

    @FXML
    private Label TitleLabel;
    @FXML
    private JFXSpinner Spinner;

    public void setButtonText(String ButtonText) {
        this.ButtonText.setText(ButtonText);
    }

    public void setTitleLabel(String TitleLabel) {
        this.TitleLabel.setText(TitleLabel);
    }

    private JFXSnackbar snackbar;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;
    Member member = null;
    Admin admin = null;
    Settings settings = null;
    private String Username;
    private String Password;
    private int MemberId;
    private boolean Edit;
    private boolean is_Admin;
    private String Recipient;
    private String RecipientFname;
    private String RecipientLname;
    private String RecipientCne;

    public void setIs_Admin(boolean is_Admin) {
        this.is_Admin = is_Admin;
    }

    public void setEdit(boolean Edit) {
        this.Edit = Edit;
    }

    public void Message() {
        snackbar.show("All Fields are required!", 2000);
    }

    @FXML
    void Add_Member(ActionEvent event) throws InterruptedException, Exception {

        Con = MySql.GetConnection();
        String cne = CNE.getText().trim().toLowerCase();
        String Fname = FirstName.getText();
        String Lname = LastName.getText();
        String email = Email.getText().trim().toLowerCase();
        String mobile = Mobile.getText();
        String address = Address.getText();
        String cemail = Cemail.getText().trim().toLowerCase();

        if (cne.isEmpty() || Fname.isEmpty() || Lname.isEmpty() || email.isEmpty() || mobile.isEmpty() || cemail.isEmpty() || (!(email.equals(cemail))) || (!(email.matches("[a-zA-Z0-9_@.]{10,}"))) || (!(mobile.matches("[0-9+]{9,}")))) {
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
            if (cemail.isEmpty()) {
                Cemail.setFocusColor(Color.RED);
                Cemail.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(Cemail).play();
                Message();
            } else {
                Cemail.setFocusColor(Color.valueOf("#00b4d8"));
                Cemail.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }
            if (!(email.equals(cemail) && (!(email.isEmpty())) && (!(cemail.trim().toLowerCase().isEmpty())))) {
                Email.setFocusColor(Color.RED);
                Email.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(Email).play();
                Cemail.setFocusColor(Color.RED);
                Cemail.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(Cemail).play();
                if ((email.isEmpty()) || (cemail.isEmpty())) {
                    Message();
                } else {
                    snackbar.show("Those emails didn’t match!", 2000);
                }

            } else {
                Cemail.setFocusColor(Color.valueOf("#00b4d8"));
                Cemail.setUnFocusColor(Color.valueOf("#4d4d4d"));
                Email.setFocusColor(Color.valueOf("#00b4d8"));
                Email.setUnFocusColor(Color.valueOf("#4d4d4d"));
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
            member = new Member();
            admin = new Admin();
            settings = new Settings();
            int CountCNEAdmin = 0;
            int CountTeleAdmin = 0;
            int CountCNEMember = 0;
            int CountEmailAdmin = 0;
            int CountEmailMember = 0;
            int CountTeleMember = 0;
            if (Edit == false) {
                if (is_Admin == false) {
                    Tools T = new Tools();
                    CountCNEMember = T.TestColumn("members", "CNE", cne.trim().toLowerCase());
                    CountTeleMember = T.TestColumn("members", "Mobile", mobile);
                    CountEmailMember = T.TestColumn("members", "Email", email.trim().toLowerCase());
                    //System.out.println(CountCNEMember);
                    //System.out.println(CountTeleMember);
                    if (CountCNEMember > 0 || CountTeleMember > 0 || CountEmailMember > 0) {
                        if (CountCNEMember > 0) {
                            Tools.DialogAlert(stackPane, anchorePane, "This CNE is already exist!");
                        }
                        if (CountTeleMember > 0) {
                            Tools.DialogAlert(stackPane, anchorePane, "This Mobile is already exist!");
                        }
                        if (CountEmailMember > 0) {
                            Tools.DialogAlert(stackPane, anchorePane, "This Email is already exist!");
                        }
                    } else {
                        String Password = Tools.generatePassword();
                        member.addMember(cne, Fname, Lname, email, address, mobile, cne, Password);
                        int id = T.getLastMemberId();
                        T.generateQRcode(id, cne, Fname, Lname, email, mobile);
                        T.CreatePdfFile(id, cne, Fname, Lname, email, address, mobile, cne, Password);
                    }
                } else {
                    ///Test CNE And Moblie if are already exist
                    Tools T = new Tools();
                    CountCNEAdmin = T.TestColumn("admins", "CNE", cne.trim().toLowerCase());
                    CountTeleAdmin = T.TestColumn("admins", "Mobile", mobile);
                    CountTeleAdmin = T.TestColumn("admins", "Email", email.trim().toLowerCase());
                    //System.out.println(CountCNEAdmin);
                    //System.out.println(CountTeleAdmin);
                    if (CountCNEAdmin > 0 || CountTeleAdmin > 0 || CountEmailAdmin > 0) {
                        if (CountCNEAdmin > 0) {
                            Tools.DialogAlert(stackPane, anchorePane, "This CNE is already exist!");
                        }
                        if (CountTeleAdmin > 0) {
                            Tools.DialogAlert(stackPane, anchorePane, "This Moblie is already exist!");
                        }
                        if (CountEmailAdmin > 0) {
                            Tools.DialogAlert(stackPane, anchorePane, "This Email is already exist!");
                        }
                    } else {
                        admin.addAdmin(cne, Fname, Lname, email, address, mobile, cne, Tools.generatePassword());
                        settings.addAdminSettings(T.getLastId());
                    }
                }
            } else {
                if (is_Admin == false) {
                    member.UpdateMember(MemberId, cne, Fname, Lname, email, address, mobile, Username, Password);
                } else {
                    admin.UpdateAdmin(MemberId, cne, Fname, Lname, email, address, mobile, Username, Password);
                }
            }
            //InserMember();
            if (!(CountCNEAdmin > 0 || CountCNEMember > 0 || CountTeleAdmin > 0 || CountTeleMember > 0 || CountEmailMember > 0 || CountEmailAdmin > 0)) {
                if (Edit == false) {

                    if (is_Admin == false) {
                        Tools.DialogAlert(stackPane, anchorePane, "Member has been added successfully");
                    } else {
                        Tools.DialogAlert(stackPane, anchorePane, "Admin has been added successfully");
                    }

                    Recipient = Email.getText();
                    RecipientCne = CNE.getText();
                    RecipientFname = FirstName.getText();
                    RecipientLname = LastName.getText();
                    Clean();
                    Tools.SendEmail(Recipient, RecipientCne, RecipientFname, RecipientLname, "", "", "", "New Account");
                } else {

                    if (is_Admin == false) {
                        Tools.DialogAlert(stackPane, anchorePane, "Member has been update successfully");
                    } else {
                        Tools.DialogAlert(stackPane, anchorePane, "Admin has been update successfully");
                    }
                }
            }
        }
    }

    /*    public void InsertOrEdit() {
        if (Edit == false) {
            if (is_Admin == false) {
                Query = "INSERT INTO `members`(`CNE`, `First_Name`, `Last_Name`, `Email`, `Address`,"
                        + " `Mobile`, `Username`, `Password`) VALUES (?,?,?,?,?,?,?,?)";
            } else {
                Query = "INSERT INTO `admins`(`CNE`, `First_Name`, `Last_Name`, `Email`, `Address`,"
                        + " `Mobile`, `Username`, `Password`) VALUES (?,?,?,?,?,?,?,?)";
            }
        } else {
            if (is_Admin == false) {
                Query = "UPDATE `members` SET `CNE`=?,"
                        + "`First_Name`=?,`Last_Name`=?,"
                        + "`Email`=?,`Address`=?,`Mobile`=?,"
                        + "`Username`=?,`Password`=? "
                        + "WHERE `Id`='" + MemberId + "'";
            } else {
                Query = "UPDATE `admins` SET `CNE`=?,"
                        + "`First_Name`=?,`Last_Name`=?,"
                        + "`Email`=?,`Address`=?,`Mobile`=?,"
                        + "`Username`=?,`Password`=? "
                        + "WHERE `Id`='" + MemberId + "'";
            }
        }
    }*/
    public void InserMember() {

        try {
            PreStatment = Con.prepareStatement(Query);
            PreStatment.setString(1, CNE.getText());
            PreStatment.setString(2, FirstName.getText());
            PreStatment.setString(3, LastName.getText());
            PreStatment.setString(4, Email.getText());
            PreStatment.setString(5, Address.getText());
            PreStatment.setString(6, Mobile.getText());
            PreStatment.setString(7, CNE.getText());
            PreStatment.setString(8, CNE.getText());
            PreStatment.execute();

        } catch (SQLException ex) {
        }
    }

    public void Clean() {
        CNE.setText(null);
        FirstName.setText(null);
        LastName.setText(null);
        Email.setText(null);
        Mobile.setText(null);
        Address.setText(null);
        Cemail.setText(null);
        Cemail.setFocusColor(Color.valueOf("#00b4d8"));
        Cemail.setUnFocusColor(Color.valueOf("#4d4d4d"));
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
    }

    @FXML
    void Cancel(ActionEvent event) {

        Stage stage = (Stage) CNE.getScene().getWindow();
        stage.close();
    }

    public void EditData(int id, String Cne, String First_Name, String Last_Name, String email, String address, String mobile, String username, String password) {
        MemberId = id;
        CNE.setText(Cne);
        FirstName.setText(First_Name);
        LastName.setText(Last_Name);
        Email.setText(email);
        Mobile.setText(mobile);
        Address.setText(address);
        Username = username;
        Password = password;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        snackbar = new JFXSnackbar(anchorePane);

    }
}
