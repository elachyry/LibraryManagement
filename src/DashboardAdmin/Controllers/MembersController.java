package DashboardAdmin.Controllers;

import DashboardAdmin.Pages.Manage.AddMemberController;
import DashboardMember.DashboardController2;
import Data_Base.MySql;
import Models.Admin;
import Models.Member;
import Models.Settings;
import Models.Tools;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;

public class MembersController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXDialog Dialog2;

    @FXML
    private JFXButton YesButton;

    @FXML
    private JFXButton NoButton;

    @FXML
    private Label DialogLabel;

    @FXML
    private TableView<Member> MemberTable;

    @FXML
    private TableColumn<Member, Integer> IdCol;

    @FXML
    private TableColumn<Member, String> CneCol;

    @FXML
    private TableColumn<Member, String> FNameCol;

    @FXML
    private TableColumn<Member, String> LNameCol;

    @FXML
    private TableColumn<Member, String> EmailCol;

    @FXML
    private TableColumn<Member, String> AddressCol;

    @FXML
    private TableColumn<Member, String> MobileCol;

    @FXML
    private TableColumn<Member, String> UsernameCol;

    @FXML
    private TableColumn<Member, String> PasswordCol;

    @FXML
    private TableView<Admin> AdminTable;

    @FXML
    private TableColumn<Admin, Integer> IdCol1;

    @FXML
    private TableColumn<Admin, String> CneCol1;

    @FXML
    private TableColumn<Admin, String> FNameCol1;

    @FXML
    private TableColumn<Admin, String> LNameCol1;

    @FXML
    private TableColumn<Admin, String> EmailCol1;

    @FXML
    private TableColumn<Admin, String> AddressCol1;

    @FXML
    private TableColumn<Admin, String> MobileCol1;

    @FXML
    private TableColumn<Admin, String> UsernameCol1;

    @FXML
    private TableColumn<Admin, String> PasswordCol1;

    @FXML
    private JFXTextField SearchFieldMember, SearchFieldAdmin;

    @FXML
    private BorderPane borderPane;

    @FXML
    private ImageView RefrechMembers;

    @FXML
    private JFXButton AddMember;

    @FXML
    private JFXButton EditMember;

    @FXML
    private JFXButton DeleteMember;

    @FXML
    private JFXButton ExportMembers;

    @FXML
    private JFXButton ResetMember;

    @FXML
    private ImageView RefrechAdmins;

    @FXML
    private JFXButton AddAdmin;

    @FXML
    private JFXButton EditAdmin;

    @FXML
    private JFXButton DeleteAdmin;

    @FXML
    private JFXButton ExportAdmins;

    @FXML
    private JFXButton ResetAdmin;
    @FXML
    private Tab TabAdmin;

    int Id;

    @FXML
    void Add_Admin(MouseEvent event) {
        is_Admin = true;
        Add_MemberOrAdmin();
    }

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;
    Member member = null;
    Admin admin = null;
    Settings settings = null;
    ObservableList<Member> MembertList = FXCollections.observableArrayList();
    ObservableList<Admin> AdmintList = FXCollections.observableArrayList();
    private boolean is_Admin;

    @FXML
    void Add_Member(MouseEvent event) {
        is_Admin = false;
        Add_MemberOrAdmin();
    }

    @FXML
    void Edit_Member(MouseEvent event) throws IOException {
        member = MemberTable.getSelectionModel().getSelectedItem();
        if (member != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/DashboardAdmin/Pages/Manage/AddMember.fxml"));
                DialogPane AddMemberDialog = loader.load();
                AddMemberController AMC = loader.getController();
                AMC.setEdit(true);
                AMC.setIs_Admin(false);
                AMC.EditData(member.getId(), member.getCNE(), member.getFirst_Name(), member.getLast_Name(),
                        member.getEmail(), member.getAddress(), member.getMobile(),
                        member.getUsername(), member.getPassword());

                AMC.setTitleLabel("Edit Member");
                AMC.setButtonText("Edit");
                //  AMC.setSpinner(false);
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Edit member");
                dialog.initStyle(StageStyle.TRANSPARENT);
                dialog.setDialogPane(AddMemberDialog);
                Optional<ButtonType> Result = dialog.showAndWait();
            } catch (IOException e) {
            }

            /*Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Edit member");
            stage.getIcons().add(new Image("/Images/reading_127px.png"));
            stage.setScene(new Scene(parent));
            stage.show();*/
        } else {
            Tools.DialogAlert(stackPane, anchorPane, "No row selected!");
        }
    }

    @FXML
    void Edit_Admin(MouseEvent event) {
        is_Admin = true;
        admin = AdminTable.getSelectionModel().getSelectedItem();
        if (admin != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/DashboardAdmin/Pages/Manage/AddMember.fxml"));
                DialogPane AddAdminDialog = loader.load();
                AddMemberController AMC = loader.getController();
                AMC.setEdit(true);
                AMC.setIs_Admin(true);
                AMC.EditData(admin.getId(), admin.getCNE(), admin.getFirst_Name(), admin.getLast_Name(),
                        admin.getEmail(), admin.getAddress(), admin.getMobile(),
                        admin.getUsername(), admin.getPassword());

                AMC.setTitleLabel("Edit admin");
                AMC.setButtonText("Edit");
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Edit admin");
                dialog.initStyle(StageStyle.TRANSPARENT);
                dialog.setDialogPane(AddAdminDialog);
                Optional<ButtonType> Result = dialog.showAndWait();
            } catch (IOException e) {
            }
        } else {
            Tools.DialogAlert(stackPane, anchorPane, "No row selected!");
            //DialogAlert("No row selected!");
        }
    }

    public void RefrechMemberTable() {
        try {
            MembertList.clear();
            Query = "SELECT * FROM `members` ";

            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();

            while (resultSet.next()) {
                MembertList.add(new Member(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        Tools.DecryptPassword(resultSet.getString(9))
                ));
                MemberTable.setItems(MembertList);
            }
        } catch (SQLException ex) {
        }
    }

    public void RefrechAdminTable() {
        try {
            AdmintList.clear();
            Query = "SELECT * FROM `admins` WHERE `Id` NOT IN ( " + new Tools().getUsernameId("admins") + ")";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();

            while (resultSet.next()) {
                AdmintList.add(new Admin(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        Tools.DecryptPassword(resultSet.getString(9))
                ));
                AdminTable.setItems(AdmintList);
            }
        } catch (SQLException ex) {
        }
    }

    public void FillMemberTable() {
        Con = MySql.GetConnection();
        RefrechMemberTable();

        IdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        CneCol.setCellValueFactory(new PropertyValueFactory<>("CNE"));
        FNameCol.setCellValueFactory(new PropertyValueFactory<>("First_Name"));
        LNameCol.setCellValueFactory(new PropertyValueFactory<>("Last_Name"));
        EmailCol.setCellValueFactory(new PropertyValueFactory<>("Email"));
        AddressCol.setCellValueFactory(new PropertyValueFactory<>("Address"));
        MobileCol.setCellValueFactory(new PropertyValueFactory<>("Mobile"));
        UsernameCol.setCellValueFactory(new PropertyValueFactory<>("Username"));
        PasswordCol.setCellValueFactory(new PropertyValueFactory<>("Password"));
    }

    public void FillAdminTable() {
        Con = MySql.GetConnection();
        RefrechAdminTable();

        IdCol1.setCellValueFactory(new PropertyValueFactory<>("Id"));
        CneCol1.setCellValueFactory(new PropertyValueFactory<>("CNE"));
        FNameCol1.setCellValueFactory(new PropertyValueFactory<>("First_Name"));
        LNameCol1.setCellValueFactory(new PropertyValueFactory<>("Last_Name"));
        EmailCol1.setCellValueFactory(new PropertyValueFactory<>("Email"));
        AddressCol1.setCellValueFactory(new PropertyValueFactory<>("Address"));
        MobileCol1.setCellValueFactory(new PropertyValueFactory<>("Mobile"));
        UsernameCol1.setCellValueFactory(new PropertyValueFactory<>("Username"));
        PasswordCol1.setCellValueFactory(new PropertyValueFactory<>("Password"));
    }

    @FXML
    void Delete_Member(MouseEvent event) {
        member = MemberTable.getSelectionModel().getSelectedItem();
        if (member != null) {
            BoxBlur Blur = new BoxBlur(3, 3, 3);
            anchorPane.setEffect(Blur);
            YesButton.setPrefSize(140, 40);
            YesButton.setFont(new Font(17));
            YesButton.setCursor(Cursor.HAND);
            NoButton.setPrefSize(140, 40);
            NoButton.setFont(new Font(17));
            NoButton.setCursor(Cursor.HAND);
            DialogLabel.setText("Do you want to delete this member?");
            DialogLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-font-size: 20px; -fx-text-fill : #52438F;");
            Dialog2.setTransitionType(JFXDialog.DialogTransition.TOP);
            Dialog2.setDialogContainer(stackPane);
            Dialog2.show();
            YesButton.setOnAction((Event) -> {
                try {
                    member.DeleteMember(member.getId());
                    RefrechMemberTable();
                    anchorPane.setEffect(null);
                    Dialog2.close();
                    Tools.SendEmail(member.getEmail(), member.getEmail(), member.getFirst_Name(), member.getLast_Name(), "", "", "", "Delete Account");
                } catch (Exception ex) {
                }

            });
            Dialog2.setOnDialogClosed((JFXDialogEvent Event) -> {
                anchorPane.setEffect(null);
            });
            NoButton.setOnAction((Event) -> {
                anchorPane.setEffect(null);
                Dialog2.close();
            });

            /*Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Delet Confirmation");
            alert.setContentText("Do you want to delete this member?");
            Optional<ButtonType> Result = alert.showAndWait();
            if (Result.get() == ButtonType.OK) {*/
 /*  }
            else
                anchorPane.setEffect(null);*/
        } else {
            Tools.DialogAlert(stackPane, anchorPane, "No row selected!");
        }
    }

    @FXML
    void Delete_Admin(MouseEvent event) {
        admin = AdminTable.getSelectionModel().getSelectedItem();
        if (admin != null) {
            BoxBlur Blur = new BoxBlur(3, 3, 3);
            anchorPane.setEffect(Blur);
            YesButton.setPrefSize(140, 40);
            YesButton.setFont(new Font(17));
            YesButton.setCursor(Cursor.HAND);
            NoButton.setPrefSize(140, 40);
            NoButton.setFont(new Font(17));
            NoButton.setCursor(Cursor.HAND);
            DialogLabel.setText("Do you want to delete this admin?");
            DialogLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-font-size: 20px; -fx-text-fill : #52438F;");
            Dialog2.setTransitionType(JFXDialog.DialogTransition.TOP);
            Dialog2.setDialogContainer(stackPane);
            Dialog2.show();
            YesButton.setOnAction((Event) -> {
                try {
                    admin.DeleteAdmin(admin.getId());
                    RefrechAdminTable();
                    anchorPane.setEffect(null);
                    Dialog2.close();
                    Tools.SendEmail(admin.getEmail(), admin.getEmail(), admin.getFirst_Name(), admin.getLast_Name(), "", "", "", "Delete Account");
                    settings = new Settings();
                    System.out.println("Delete Id " + admin.getId());
                    settings.DeleteAdminSettings(admin.getId());
                } catch (Exception ex) {
                }
            });
            Dialog2.setOnDialogClosed((JFXDialogEvent Event) -> {
                anchorPane.setEffect(null);
            });
            NoButton.setOnAction((Event) -> {
                anchorPane.setEffect(null);
                Dialog2.close();
            });
        } else {
            Tools.DialogAlert(stackPane, anchorPane, "No row selected!");
        }
    }

    @FXML
    void Refresh_Members(MouseEvent event) {
        RefrechMemberTable();
    }

    @FXML
    void Refresh_Admins(MouseEvent event) {
        RefrechAdminTable();
    }

    @FXML
    void Export_Members(MouseEvent event) throws IOException {
        member = new Member();
        member.Export("members");
        Desktop.getDesktop().open(new File("Excel\\members.xlsx"));
        Tools.DialogAlert(stackPane, anchorPane, "Members have been exported successfully!");

    }

    @FXML
    void Export_Admins(MouseEvent event) throws IOException {
        admin.Export("admins");
        Desktop.getDesktop().open(new File("Excel\\admins.xlsx"));
        Tools.DialogAlert(stackPane, anchorPane, "Admins have been exported successfully!");
    }

    @FXML
    private void Reset_Member(MouseEvent event) {
        member = MemberTable.getSelectionModel().getSelectedItem();
        if (member != null) {
            BoxBlur Blur = new BoxBlur(3, 3, 3);
            anchorPane.setEffect(Blur);
            YesButton.setPrefSize(140, 40);
            YesButton.setFont(new Font(17));
            YesButton.setCursor(Cursor.HAND);
            NoButton.setPrefSize(140, 40);
            NoButton.setFont(new Font(17));
            NoButton.setCursor(Cursor.HAND);
            DialogLabel.setText("Do you want to reset Username and Password for this member?");
            DialogLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-font-size: 20px; -fx-text-fill : #52438F;");
            Dialog2.setTransitionType(JFXDialog.DialogTransition.TOP);
            Dialog2.setDialogContainer(stackPane);
            Dialog2.show();
            YesButton.setOnAction((Event) -> {
                member.Reset(member.getId(), member.getCNE(), member.getFirst_Name(), member.getLast_Name(), member.getEmail(), "members");
                RefrechMemberTable();
                anchorPane.setEffect(null);
                Dialog2.close();
            });
            Dialog2.setOnDialogClosed((JFXDialogEvent Event) -> {
                anchorPane.setEffect(null);
            });
            NoButton.setOnAction((Event) -> {
                anchorPane.setEffect(null);
                Dialog2.close();
            });
        } else {
            Tools.DialogAlert(stackPane, anchorPane, "No row selected!");
        }

    }

    @FXML
    private void Reset_Admin(MouseEvent event) {
        admin = AdminTable.getSelectionModel().getSelectedItem();
        if (admin != null) {
            BoxBlur Blur = new BoxBlur(3, 3, 3);
            anchorPane.setEffect(Blur);
            YesButton.setPrefSize(140, 40);
            YesButton.setFont(new Font(17));
            YesButton.setCursor(Cursor.HAND);
            NoButton.setPrefSize(140, 40);
            NoButton.setFont(new Font(17));
            NoButton.setCursor(Cursor.HAND);
            DialogLabel.setText("Do you want to reset Username and Password for this admin?");
            DialogLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-font-size: 20px; -fx-text-fill : #52438F;");
            Dialog2.setTransitionType(JFXDialog.DialogTransition.TOP);
            Dialog2.setDialogContainer(stackPane);
            Dialog2.show();
            YesButton.setOnAction((Event) -> {
                admin.Reset(admin.getId(), admin.getCNE(), admin.getFirst_Name(), admin.getLast_Name(), admin.getEmail(), "admins");
                RefrechAdminTable();
                anchorPane.setEffect(null);
                Dialog2.close();
            });
            Dialog2.setOnDialogClosed((JFXDialogEvent Event) -> {
                anchorPane.setEffect(null);
            });
            NoButton.setOnAction((Event) -> {
                anchorPane.setEffect(null);
                Dialog2.close();
            });
        } else {
            Tools.DialogAlert(stackPane, anchorPane, "No row selected!");
        }

    }

    public void Add_MemberOrAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/DashboardAdmin/Pages/Manage/AddMember.fxml"));
            DialogPane AddMemberDialog = loader.load();
            AddMemberController AMC = loader.getController();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initStyle(StageStyle.TRANSPARENT);
            if (is_Admin) {
                AMC.setIs_Admin(true);
                AMC.setTitleLabel("Add New Admin");
                AMC.setButtonText("Add admin");
                dialog.setTitle("Add new admin");
            } else {
                AMC.setIs_Admin(false);
                dialog.setTitle("Add new member");
            }
            dialog.setDialogPane(AddMemberDialog);
            Optional<ButtonType> Result = dialog.showAndWait();

        } catch (IOException e) {
        }
        /*Parent root = FXMLLoader.load(getClass().getResource("/Dashboard/Pages/Manage/AddMember.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Add new member");
        stage.getIcons().add(new Image("/Images/reading_127px.png"));
        stage.setScene(scene);
        stage.show();*/
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tooltip tooltip = new Tooltip("Refrech Table");
        Tooltip.install(RefrechAdmins, tooltip);
        Tooltip.install(RefrechMembers, tooltip);
        AddAdmin.setTooltip(new Tooltip("Add a new admin"));
        EditAdmin.setTooltip(new Tooltip("Edit an admin"));
        DeleteAdmin.setTooltip(new Tooltip("Delete admin"));
        ExportAdmins.setTooltip(new Tooltip("Export all admins To Excel file"));
        ResetAdmin.setTooltip(new Tooltip("Reset Username and Password"));
        AddMember.setTooltip(new Tooltip("Add a new member"));
        EditMember.setTooltip(new Tooltip("Edit a member"));
        DeleteMember.setTooltip(new Tooltip("Delete a memebr"));
        ExportMembers.setTooltip(new Tooltip("Export all members To Excel file"));
        ResetMember.setTooltip(new Tooltip("Reset Username and Password"));
        SearchFieldMember.setTooltip(new Tooltip("Search for members"));
        SearchFieldAdmin.setTooltip(new Tooltip("Search for admins"));
        FillMemberTable();
        FillAdminTable();
        //Search field member
        member = new Member();
        admin = new Admin();
        member.SearchMemebr(SearchFieldMember, MembertList, MemberTable);
        //Search field admin
        admin.SearchAdmin(SearchFieldAdmin, AdmintList, AdminTable);
        Tools T = new Tools();
        Id = T.getUsernameId("admins");
        System.out.println("id admin " + Id);
        if (Id != 1) {
            TabAdmin.setDisable(true);
        } else {
            TabAdmin.setDisable(false);
        }
    }

    @FXML
    private void Go_Account(MouseEvent event) throws IOException {
        member = MemberTable.getSelectionModel().getSelectedItem();
        if (member != null) {
            DashboardController2.Admin = member.getId();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/DashboardMember/DashboardMember2.fxml"));
            DialogPane AddMemberDialog = loader.load();
            DashboardController2 DC = loader.getController();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initStyle(StageStyle.TRANSPARENT);
            dialog.setDialogPane(AddMemberDialog);
            Optional<ButtonType> Result = dialog.showAndWait();

        } else {
            Tools.DialogAlert(stackPane, anchorPane, "No row selected!");
        }
    }
}
