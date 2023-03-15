package DashboardAdmin;

import DashboardAdmin.Pages.Manage.ShowBooksController;
import Data_Base.MySql;
import Models.Tools;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class DashboardController implements Initializable {

    @FXML
    private StackPane PagesArea;

    @FXML
    private JFXButton Members;

    @FXML
    private Circle ProfileImage;

    @FXML
    private Label FullName;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;

    @FXML
    private JFXButton Overview;

    @FXML
    private JFXButton Books;

    @FXML
    private JFXButton Authors;

    @FXML
    private JFXButton Categories;

    @FXML
    private JFXButton BooksBorrowed;

    @FXML
    private JFXButton Profile;

    @FXML
    private JFXButton Settings;

    @FXML
    private JFXButton Logout;

    /*  public void ChangePageArea(String Name) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/Dashboard/Pages/" + Name + ".fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
    }*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
                Tools.setSendEmail(false);
            } else {
                Tools.setSendEmail(true);
            }

        } catch (SQLException e) {

        }
        DashboardMember.DashboardController.AdminShowBooks = true;
        DashboardMember.DashboardController.MemberShowBooks = false;
        DashboardMember.DashboardController.IsMember = false;
        FillUserImage();
        Tools T = new Tools();
        T.getUsernameId("admins");
        FullName.setText(Tools.getFullName());
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardAdmin/Pages/Overview.fxml"));
            PagesArea.getChildren().removeAll();
            PagesArea.getChildren().setAll(fxml);

        } catch (IOException e) {
        }
        Overview.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
        Books.setStyle(null);
        Authors.setStyle(null);
        Categories.setStyle(null);
        BooksBorrowed.setStyle(null);
        Members.setStyle(null);
        Profile.setStyle(null);
        Settings.setStyle(null);
        Logout.setStyle(null);
    }

    @FXML
    public void Overview(javafx.event.ActionEvent actionEvent) throws IOException {
        FillUserImage();
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardAdmin/Pages/Overview.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
        Overview.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
        Books.setStyle(null);
        Authors.setStyle(null);
        Categories.setStyle(null);
        BooksBorrowed.setStyle(null);
        Members.setStyle(null);
        Profile.setStyle(null);
        Settings.setStyle(null);
        Logout.setStyle(null);
    }

    @FXML
    public void Books(javafx.event.ActionEvent actionEvent) throws IOException {
        FillUserImage();
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardAdmin/Pages/Books.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
        DashboardMember.DashboardController.AdminShowBooks = false;
        DashboardMember.DashboardController.MemberShowBooks = false;
        Overview.setStyle(null);
        Books.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
        Authors.setStyle(null);
        Categories.setStyle(null);
        BooksBorrowed.setStyle(null);
        Members.setStyle(null);
        Profile.setStyle(null);
        Settings.setStyle(null);
        Logout.setStyle(null);
    }

    @FXML
    public void Authors(javafx.event.ActionEvent actionEvent) throws IOException {
        FillUserImage();
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardAdmin/Pages/Authors.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
        DashboardMember.DashboardController.AdminShowBooks = true;
        DashboardMember.DashboardController.MemberShowBooks = false;
        ShowBooksController.Group = "Author";
        Overview.setStyle(null);
        Books.setStyle(null);
        Authors.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
        Categories.setStyle(null);
        BooksBorrowed.setStyle(null);
        Members.setStyle(null);
        Profile.setStyle(null);
        Settings.setStyle(null);
        Logout.setStyle(null);
    }

    @FXML
    public void Categories(javafx.event.ActionEvent actionEvent) throws IOException {
        FillUserImage();
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardAdmin/Pages/Categories.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
        DashboardMember.DashboardController.AdminShowBooks = true;
        DashboardMember.DashboardController.MemberShowBooks = false;
        ShowBooksController.Group = "Category";
        Overview.setStyle(null);
        Books.setStyle(null);
        Authors.setStyle(null);
        Categories.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
        BooksBorrowed.setStyle(null);
        Members.setStyle(null);
        Profile.setStyle(null);
        Settings.setStyle(null);
        Logout.setStyle(null);
    }

    @FXML
    public void Books_Borrowed(javafx.event.ActionEvent actionEvent) throws IOException {
        FillUserImage();
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardAdmin/Pages/Books_Borrowed.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
        Overview.setStyle(null);
        Books.setStyle(null);
        Authors.setStyle(null);
        Categories.setStyle(null);
        BooksBorrowed.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
        Members.setStyle(null);
        Profile.setStyle(null);
        Settings.setStyle(null);
        Logout.setStyle(null);
    }

    @FXML
    public void Memebers(javafx.event.ActionEvent actionEvent) throws IOException {
        FillUserImage();
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardAdmin/Pages/Members.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
        Overview.setStyle(null);
        Books.setStyle(null);
        Authors.setStyle(null);
        Categories.setStyle(null);
        BooksBorrowed.setStyle(null);
        Members.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
        Profile.setStyle(null);
        Settings.setStyle(null);
        Logout.setStyle(null);

    }

    @FXML
    public void Profile(javafx.event.ActionEvent actionEvent) throws IOException {
        FillUserImage();
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardAdmin/Pages/Profile.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
        Overview.setStyle(null);
        Books.setStyle(null);
        Authors.setStyle(null);
        Categories.setStyle(null);
        BooksBorrowed.setStyle(null);
        Members.setStyle(null);
        Profile.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
        Settings.setStyle(null);
        Logout.setStyle(null);
    }

    @FXML
    public void Settings(javafx.event.ActionEvent actionEvent) throws IOException {
        FillUserImage();
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardAdmin/Pages/Settings.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
        Overview.setStyle(null);
        Books.setStyle(null);
        Authors.setStyle(null);
        Categories.setStyle(null);
        BooksBorrowed.setStyle(null);
        Members.setStyle(null);
        Profile.setStyle(null);
        Settings.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
        Logout.setStyle(null);
    }

    @FXML
    public void Logout(MouseEvent event) {
        Overview.setStyle(null);
        Books.setStyle(null);
        Authors.setStyle(null);
        Categories.setStyle(null);
        BooksBorrowed.setStyle(null);
        Members.setStyle(null);
        Profile.setStyle(null);
        Settings.setStyle(null);
        Logout.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
        Stage stage1 = (Stage) PagesArea.getScene().getWindow();
        stage1.close();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Loging_Page/LoginPage.fxml"));
        try {
            loader.load();
            Stage stage = new Stage();
            Parent parent = loader.getRoot();
            stage.setScene(new Scene(parent));
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
        }
    }

    public void FillUserImage() {
        Connection Con = MySql.GetConnection();
        Statement St;
        try {
            ResultSet rs = Con.createStatement().executeQuery("SELECT * FROM `admins` WHERE Id = " + new Tools().getUsernameId("admins"));
            rs.next();
            InputStream imageFile = rs.getBinaryStream(10);
            if (imageFile != null) {
                Image image = new Image(imageFile);
                ProfileImage.setStroke(null);
                ProfileImage.setFill(new ImagePattern(image));
                ProfileImage.setEffect(new DropShadow(+25d, 0d, +2d, Color.WHITE));
            } else {
                ProfileImage.setStroke(null);
                ProfileImage.setFill(new ImagePattern(new Image("/Images/user_100px.png")));
                // ProfileImage.setEffect(new DropShadow(+25d, 0d, +2d, Color.PURPLE));
            }
        } catch (SQLException ex) {
        }
    }

    @FXML
    private void Profile2(MouseEvent event) throws IOException {
        FillUserImage();
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardAdmin/Pages/Profile.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
        Overview.setStyle(null);
        Books.setStyle(null);
        Authors.setStyle(null);
        Categories.setStyle(null);
        BooksBorrowed.setStyle(null);
        Members.setStyle(null);
        Profile.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
        Settings.setStyle(null);
        Logout.setStyle(null);
    }

}
