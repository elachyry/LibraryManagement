package DashboardMember.Controllers;

import static DashboardMember.Controllers.FavoriteController.Admin;
import Data_Base.MySql;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MemberNewBooksController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXDialog Dialog2;

    @FXML
    private Label DialogLabel;

    @FXML
    private JFXButton YesButton;

    @FXML
    private JFXButton NoButton;

    @FXML
    private GridPane Container;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;

    @FXML
    private JFXTextField SearchFieldBook;
    @FXML
    private StackPane PagesArea;
    @FXML
    private VBox vb;
    @FXML
    private HBox hbLayout;
    public static int Admin = 0;

    void Refresh_Books(MouseEvent event) throws IOException {
        FillBookTable();
    }

    public void FillBookTable() throws IOException {
        Con = MySql.GetConnection();
        int col = 0;
        int row = 1;
        try {
            Query = "SELECT * FROM `books` ORDER BY Id DESC LIMIT 5";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            String Status;
            while (resultSet.next()) {
                if (resultSet.getInt(13) > 0) {
                    Status = "In Stock";
                } else {
                    Status = "Out Of Stock";
                }
                FXMLLoader loader = new FXMLLoader();
                BookTemplateController.Admin = Admin;
                loader.setLocation(getClass().getResource("/DashboardMember/Pages/BookTemplate.fxml"));
                VBox vbox = loader.load();
                BookTemplateController BTC = loader.getController();
                BTC.SetBooks(resultSet.getInt(1), resultSet.getString(3), resultSet.getString(4), Status, resultSet.getBinaryStream(14), 0);
                if (col == 5) {
                    col = 0;
                    ++row;
                }
                Container.add(vbox, col++, row);
                GridPane.setMargin(vbox, new Insets(5, 50, 5, 40));
            }
        } catch (SQLException ex) {
        }
    }

    public void FillRecommended() throws IOException {
        int count = getCountBooks();
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i < count; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        for (int i = 0; i < 4; i++) {
            System.out.println(list.get(i));
            try {
                Con = MySql.GetConnection();
                Query = "SELECT * FROM `books` WHERE Id = " + list.get(i);
                PreStatment = Con.prepareStatement(Query);
                resultSet = PreStatment.executeQuery();
                String Status;
                while (resultSet.next()) {
                    if (resultSet.getInt(13) > 0) {
                        Status = "In Stock";
                    } else {
                        Status = "Out Of Stock";
                    }
                    FXMLLoader loader = new FXMLLoader();
                    BookTemplate2Controller.Admin = Admin;
                    loader.setLocation(getClass().getResource("/DashboardMember/Pages/BookTemplate2.fxml"));
                    HBox hbox = loader.load();
                    BookTemplate2Controller BT2C = loader.getController();
                    BT2C.SetBooks(resultSet.getInt(1), resultSet.getString(3), resultSet.getString(4), Status, resultSet.getBinaryStream(14), 0);
                    hbLayout.getChildren().add(hbox);
                }
            } catch (SQLException ex) {
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            FillRecommended();
            FillBookTable();
        } catch (IOException ex) {
        }
        //book.SearchBook(SearchFieldBook, BookList, BookTable);
    }

    public int getCountBooks() {
        try {
            Con = MySql.GetConnection();
            String Query2 = "SELECT  COUNT(*) FROM `books`";
            PreparedStatement PreStatment2 = Con.prepareStatement(Query2);
            ResultSet resultSet2 = PreStatment2.executeQuery();
            resultSet2.next();
            return resultSet2.getInt(1);

        } catch (SQLException ex) {
        }
        return 0;
    }

    /*private void AllBooks(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardMember/Pages/AllBooks.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
        FXMLLoader loader = new FXMLLoader();
        AddBookController.Edit = false;
    }

    private void Authors(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardMember/Pages/Authors.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
    }

    private void Categories(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardMember/Pages/Categories.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
    }

    private void BooksBorrowed(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardMember/Pages/Books_Borrowed.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
    }

    private void Favorite(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/DashboardMember/Pages/Favorite.fxml"));
        PagesArea.getChildren().removeAll();
        PagesArea.getChildren().setAll(fxml);
    }*/
}
