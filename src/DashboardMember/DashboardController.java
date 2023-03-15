package DashboardMember;

import DashboardAdmin.Pages.Manage.AddBookController;
import DashboardMember.Controllers.BookTemplateController;
import DashboardMember.Controllers.Books_BorrowedController;
import DashboardMember.Controllers.FavoriteController;
import DashboardMember.Controllers.MemberAllBooksController;
import DashboardMember.Controllers.MemberAuthorsController;
import DashboardMember.Controllers.MemberCategoriesController;
import DashboardMember.Controllers.MemberNewBooksController;
import DashboardMember.Controllers.ProfileController;
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
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DashboardController implements Initializable {

    @FXML
    private StackPane PagesArea;

    @FXML
    private Circle ProfileImage;

    @FXML
    private Label FullName;

    public static boolean IsMember = false;
    public static boolean AdminShowBooks = false;
    public static boolean MemberShowBooks = false;
    public static int Admin = 0;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;

    @FXML
    private JFXButton Authors;
    @FXML
    private JFXButton Categories;
    @FXML
    private JFXButton BooksBorrowed;
    @FXML
    private JFXButton Profile;
    @FXML
    private JFXButton Logout;
    @FXML
    private JFXButton Favorite;
    @FXML
    private JFXButton NewBooks;
    @FXML
    private JFXButton Books;


    /*  public void ChangePageArea(String Name) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/Dashboard/Pages/" + Name + ".fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
    }*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        IsMember = true;
        AdminShowBooks = false;
        MemberShowBooks = false;
        FillUserImage();
        if (Admin == 0) {
            Tools T = new Tools();
            T.getUsernameId("members");
            FullName.setText(Tools.getFullName());
        } else {
            Con = MySql.GetConnection();
            Query = "SELECT * FROM `members` WHERE Id = " + Admin + "";
            try {
                PreStatment = Con.prepareStatement(Query);
                resultSet = PreStatment.executeQuery();
                resultSet.next();
                FullName.setText(resultSet.getString(3) + " " + resultSet.getString(4));
            } catch (SQLException ex) {
            }
        }

        try {
            MemberNewBooksController.Admin = Admin;
            Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardMember/Pages/NewBooks.fxml"));
            PagesArea.getChildren().removeAll();
            PagesArea.getChildren().setAll(fxml);

        } catch (IOException e) {
        }
        NewBooks.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
        Authors.setStyle(null);
        Categories.setStyle(null);
        Favorite.setStyle(null);
        Books.setStyle(null);
        BooksBorrowed.setStyle(null);
        Profile.setStyle(null);
        Logout.setStyle(null);
    }

    @FXML
    public void Books(javafx.event.ActionEvent actionEvent) throws IOException {
        BookTemplateController.Borrow_Book = 0;
        FillUserImage();
        MemberAllBooksController.Admin = Admin;
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardMember/Pages/AllBooks.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
        AdminShowBooks = false;
        MemberShowBooks = false;
        Books.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
        Authors.setStyle(null);
        Categories.setStyle(null);
        BooksBorrowed.setStyle(null);
        Favorite.setStyle(null);
        NewBooks.setStyle(null);
        Profile.setStyle(null);
        Logout.setStyle(null);
    }

    @FXML
    public void Authors(javafx.event.ActionEvent actionEvent) throws IOException {
        Books.setStyle(null);
        Favorite.setStyle(null);
        NewBooks.setStyle(null);
        Authors.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
        Categories.setStyle(null);
        BooksBorrowed.setStyle(null);
        Profile.setStyle(null);
        Logout.setStyle(null);
        BookTemplateController.Borrow_Book = 0;
        FillUserImage();
        MemberAuthorsController.Admin = Admin;
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardMember/Pages/Authors.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
        AdminShowBooks = false;
        MemberShowBooks = true;

    }

    @FXML
    public void Categories(javafx.event.ActionEvent actionEvent) throws IOException {
        BookTemplateController.Borrow_Book = 0;
        FillUserImage();
        MemberCategoriesController.Admin = Admin;
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardMember/Pages/Categories.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
        IsMember = true;
        AdminShowBooks = false;
        MemberShowBooks = true;
        Books.setStyle(null);
        Favorite.setStyle(null);
        NewBooks.setStyle(null);
        Authors.setStyle(null);
        Categories.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
        BooksBorrowed.setStyle(null);
        Profile.setStyle(null);
        Logout.setStyle(null);
    }

    @FXML
    public void Books_Borrowed(javafx.event.ActionEvent actionEvent) throws IOException {
        BookTemplateController.Borrow_Book = 1;
        FillUserImage();
        Books_BorrowedController.Admin = Admin;
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardMember/Pages/Books_Borrowed.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
        Books.setStyle(null);
        Authors.setStyle(null);
        Categories.setStyle(null);
        Favorite.setStyle(null);
        NewBooks.setStyle(null);
        BooksBorrowed.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
        Profile.setStyle(null);
        Logout.setStyle(null);
    }

    @FXML
    public void Profile(javafx.event.ActionEvent actionEvent) throws IOException {
        FillUserImage();
        BookTemplateController.Borrow_Book = 0;
        ProfileController.Admin = Admin;
        System.out.println("Admin " + Admin);
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardMember/Pages/Profile.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
        Favorite.setStyle(null);
        Books.setStyle(null);
        Authors.setStyle(null);
        Categories.setStyle(null);
        BooksBorrowed.setStyle(null);
        Profile.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
        Logout.setStyle(null);

        NewBooks.setStyle(null);
    }

    @FXML
    public void Logout(MouseEvent event
    ) {
        Stage stage1 = (Stage) PagesArea.getScene().getWindow();
        stage1.close();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Member_Loging_Page/Member_Login.fxml"));
        try {
            loader.load();
            Stage stage = new Stage();
            Parent parent = loader.getRoot();
            stage.setScene(new Scene(parent));
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
        }
        Books.setStyle(null);
        Authors.setStyle(null);
        Categories.setStyle(null);
        BooksBorrowed.setStyle(null);
        Profile.setStyle(null);
        Favorite.setStyle(null);
        NewBooks.setStyle(null);
        Logout.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
    }

    public void FillUserImage() {
        Connection Con = MySql.GetConnection();
        Statement St;
        int Id;
        if (Admin == 0) {
            Id = new Tools().getUsernameId("members");
        } else {
            Id = Admin;
        }
        try {
            ResultSet rs = Con.createStatement().executeQuery("SELECT * FROM `members` WHERE Id = " + Id);
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
    private void NewBooks(ActionEvent event) throws IOException {
        BookTemplateController.Borrow_Book = 0;
        FillUserImage();
        MemberNewBooksController.Admin = Admin;
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardMember/Pages/NewBooks.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
        AdminShowBooks = false;
        MemberShowBooks = false;
        NewBooks.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
        Books.setStyle(null);
        Authors.setStyle(null);
        Categories.setStyle(null);
        BooksBorrowed.setStyle(null);
        Favorite.setStyle(null);
        Profile.setStyle(null);
        Logout.setStyle(null);
    }

    @FXML
    private void Favorite(ActionEvent event) throws IOException {
        BookTemplateController.Borrow_Book = 0;
        FillUserImage();
        FavoriteController.Admin = Admin;
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardMember/Pages/Favorite.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
        AdminShowBooks = false;
        MemberShowBooks = false;
        Favorite.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
        Authors.setStyle(null);
        Categories.setStyle(null);
        BooksBorrowed.setStyle(null);
        Profile.setStyle(null);
        Logout.setStyle(null);
        Books.setStyle(null);
        NewBooks.setStyle(null);
    }

    public void profile() throws IOException {
        FillUserImage();
        BookTemplateController.Borrow_Book = 0;
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardMember/Pages/Profile.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
        Favorite.setStyle(null);
        Books.setStyle(null);
        Authors.setStyle(null);
        Categories.setStyle(null);
        BooksBorrowed.setStyle(null);
        Profile.setStyle("-fx-background-color: linear-gradient(to right, #00b4d8, #caf0f8 );\n"
                + "    -fx-border-color:  #FFF6FA;\n"
                + "    -fx-border-width: 0px 0px 0px 4px;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); ");
        Logout.setStyle(null);

        NewBooks.setStyle(null);

    }

    @FXML
    private void Profile2(MouseEvent event) throws IOException {
        profile();
    }

    public VBox Template(StackPane PagesArea) {
        Label lb1 = new Label("Quick Access:");
        lb1.setFont(Font.font("Century Gothic", FontWeight.BOLD, 41));
        lb1.setTextFill(Paint.valueOf("#023e8a"));

        JFXButton btn1 = new JFXButton("All Books");
        btn1.setPrefSize(270, 150);
        btn1.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 25));
        btn1.setTextFill(Paint.valueOf("#FFF"));
        btn1.setStyle("-fx-background-color: #EC505C;"
                + "-fx-background-radios: 15;\n"
                + "    -fx-effect: dropshadow(three-pass-box, rgb(0, 0,0, 0.3), 10, 0, 0, 10);");
        btn1.setFocusTraversable(false);
        btn1.setCursor(Cursor.HAND);
        btn1.setOnMouseClicked((Event event) -> {
            Parent fxml = null;
            try {
                fxml = FXMLLoader.load(getClass().getResource("/DashboardMember/Pages/AllBooks.fxml"));
            } catch (IOException ex) {
            }
            PagesArea.getChildren().removeAll();
            PagesArea.getChildren().setAll(fxml);
            FXMLLoader loader = new FXMLLoader();
            AddBookController.Edit = false;
        });

        JFXButton btn2 = new JFXButton("Authors");
        btn2.setPrefSize(270, 150);
        btn2.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 25));
        btn2.setTextFill(Paint.valueOf("#FFF"));
        btn2.setStyle("-fx-background-color: #FDDB87;"
                + "-fx-background-radios: 15;\n"
                + "    -fx-effect: dropshadow(three-pass-box, rgb(0, 0,0, 0.3), 10, 0, 0, 10);");
        btn2.setFocusTraversable(false);
        btn2.setCursor(Cursor.HAND);
        btn2.setOnMouseClicked((Event event) -> {
            Parent fxml = null;
            try {
                fxml = FXMLLoader.load(getClass().getResource("/DashboardMember/Pages/Authors.fxml"));
            } catch (IOException ex) {
            }
            PagesArea.getChildren().removeAll();
            PagesArea.getChildren().setAll(fxml);
        });

        JFXButton btn3 = new JFXButton("Categories");
        btn3.setPrefSize(270, 150);
        btn3.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 25));
        btn3.setTextFill(Paint.valueOf("#FFF"));
        btn3.setStyle("-fx-background-color: #46CFC3;"
                + "-fx-background-radios: 15;\n"
                + "    -fx-effect: dropshadow(three-pass-box, rgb(0, 0,0, 0.3), 10, 0, 0, 10);");
        btn3.setFocusTraversable(false);
        btn3.setCursor(Cursor.HAND);
        btn3.setOnMouseClicked((Event event) -> {
            Parent fxml = null;
            try {
                fxml = FXMLLoader.load(getClass().getResource("/DashboardMember/Pages/Categories.fxml"));
            } catch (IOException ex) {
            }
            PagesArea.getChildren().removeAll();
            PagesArea.getChildren().setAll(fxml);
        });

        JFXButton btn4 = new JFXButton("Books Borrowed");
        btn4.setPrefSize(270, 150);
        btn4.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 25));
        btn4.setTextFill(Paint.valueOf("#FFF"));
        btn4.setStyle("-fx-background-color: #A7D1B1;"
                + "-fx-background-radios: 15;\n"
                + "    -fx-effect: dropshadow(three-pass-box, rgb(0, 0,0, 0.3), 10, 0, 0, 10);"
        );
        btn4.setFocusTraversable(false);
        btn4.setCursor(Cursor.HAND);
        btn4.setOnMouseClicked((Event event) -> {
            Parent fxml = null;
            try {
                fxml = FXMLLoader.load(getClass().getResource("/DashboardMember/Pages/Books_Borrowed.fxml"));
            } catch (IOException ex) {
            }
            PagesArea.getChildren().removeAll();
            PagesArea.getChildren().setAll(fxml);
        });

        JFXButton btn5 = new JFXButton("Favorite");
        btn5.setPrefSize(270, 150);
        btn5.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 25));
        btn5.setTextFill(Paint.valueOf("#FFF"));
        btn5.setStyle("-fx-background-color: #FF6C30;"
                + "-fx-background-radios: 15;\n"
                + "    -fx-effect: dropshadow(three-pass-box, rgb(0, 0,0, 0.3), 10, 0, 0, 10);");
        btn5.setFocusTraversable(false);
        btn5.setCursor(Cursor.HAND);
        btn5.setOnMouseClicked((Event event) -> {
            BookTemplateController.Borrow_Book = 0;
            //FillUserImage();
            Parent fxml = null;
            try {
                fxml = FXMLLoader.load(getClass().getResource("/DashboardMember/Pages/Favorite.fxml"));
            } catch (IOException ex) {
            }
            PagesArea.getChildren().removeAll();
            PagesArea.getChildren().setAll(fxml);
        });

        HBox hb1 = new HBox(45, btn1, btn2, btn3, btn4, btn5);
        hb1.setAlignment(Pos.CENTER);
        hb1.setPrefSize(1555, 185);

        VBox vb1 = new VBox(lb1, hb1);
        vb1.setPadding(new Insets(5, 20, 5, 20));
        vb1.setPrefSize(1600, 271);

        return vb1;
    }

}
