package DashboardMember.Controllers;

import Data_Base.MySql;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MemberAuthorsController implements Initializable {

    @FXML
    private StackPane stackPane;

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
    public GridPane Container;

    private GridPane gp1;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;
    int Author;
    public static int Admin = 0;
    @FXML
    private Label name;
    @FXML
    private ImageView undo;

    void Refresh_Books(MouseEvent event) throws IOException {
        FillBookTable();
    }

    public void FillBookTable() throws IOException {
        Con = MySql.GetConnection();
        int col = 0;
        int row = 1;
        try {
            Query = "SELECT * FROM `authors`";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            String Status;
            while (resultSet.next()) {
                if (resultSet.getInt(5) > 0) {
                    Author = resultSet.getInt(1);
                    /*TemplateController.Category = resultSet.getInt(1);
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/DashboardMember/Pages/Template.fxml"));
                    HBox vbox = loader.load();
                    TemplateController TC = loader.getController();
                    TC.SetCategory(resultSet.getString(2));*/
                    HBox hbox = Template(resultSet.getString(2) + " " + resultSet.getString(3));
                    if (col == 1) {
                        //TC.pane.setVisible(false);                        
                    }
                    if (col == 2) {
                        col = 0;
                        ++row;
                    }
                    Container.add(hbox, col++, row);
                    GridPane.setMargin(hbox, new Insets(5));
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(MemberNewBooksController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HBox Template(String Cname) throws IOException {
        Label lb1 = new Label(Cname);
        lb1.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        lb1.setTextFill(Paint.valueOf("#023e8a"));
        HBox hb1 = new HBox(lb1);
        hb1.setPadding(new Insets(0, 0, 0, 10));
        hb1.setPrefSize(704, 38);

        gp1 = new GridPane();

        HBox hb2 = new HBox(gp1);
        hb2.setPrefSize(714, 432);
        hb2.setAlignment(Pos.CENTER);

        JFXButton btn1 = new JFXButton("Show More");
        btn1.setPrefSize(300, 45);
        btn1.setFont(new Font("Century Gothic", 24));
        btn1.setFocusTraversable(false);
        btn1.setCursor(Cursor.HAND);
        btn1.setOnMouseClicked((Event event) -> {
            undo.setVisible(true);
            gp1.getChildren().clear();
            Container.getChildren().clear();
            name.setText(lb1.getText());
            Connection Con2 = MySql.GetConnection();
            int col = 0;
            int row = 1;
            int i = 0;
            try {
                String[] NameSplit = lb1.getText().split(" ");
                String Query2 = "SELECT * FROM `books` WHERE Author_Id = (SELECT Id FROM authors WHERE `First_Name` = '" + NameSplit[0]
                        + "' AND `Last_Name` = '" + NameSplit[NameSplit.length - 1] + "')";
                PreparedStatement PreStatment2 = Con2.prepareStatement(Query2);
                ResultSet resultSet2 = PreStatment2.executeQuery();
                String Status;
                while (resultSet2.next()) {
                    if (i == 3) {
                        break;
                    }
                    if (resultSet2.getInt(13) > 0) {
                        Status = "In Stock";
                    } else {
                        Status = "Out Of Stock";
                    }
                    FXMLLoader loader = new FXMLLoader();
                    BookTemplateController.Admin = Admin;
                    loader.setLocation(getClass().getResource("/DashboardMember/Pages/BookTemplate.fxml"));
                    VBox vbox2 = loader.load();
                    BookTemplateController BTC = loader.getController();
                    BTC.SetBooks(resultSet2.getInt(1), resultSet2.getString(3), resultSet2.getString(4), Status, resultSet2.getBinaryStream(14), 0);
                    if (col == 6) {
                        col = 0;
                        ++row;
                    }
                    i++;
                    Container.add(vbox2, col++, row);
                    GridPane.setMargin(vbox2, new Insets(5, 50, 5, 40));
                }
            } catch (SQLException | IOException ex) {
            }
        });
        HBox hb3 = new HBox(btn1);
        hb3.setPrefSize(734, 62);
        hb3.setAlignment(Pos.CENTER);

        VBox vb1 = new VBox(hb1, hb2, hb3);
        vb1.setPrefSize(734, 559);

        HBox hb = new HBox(vb1);
        hb.setPrefSize(736, 583);
        Template2();
        return hb;
    }

    public void Template2() throws IOException {
        Connection Con2 = MySql.GetConnection();
        int col = 0;
        int row = 1;
        int i = 0;
        try {
            String Query2 = "SELECT * FROM `books` WHERE Author_Id = " + Author;
            PreparedStatement PreStatment2 = Con2.prepareStatement(Query2);
            ResultSet resultSet2 = PreStatment2.executeQuery();
            String Status;
            while (resultSet2.next()) {
                if (i == 3) {
                    break;
                }
                if (resultSet2.getInt(13) > 0) {
                    Status = "In Stock";
                } else {
                    Status = "Out Of Stock";
                }
                FXMLLoader loader = new FXMLLoader();
                BookTemplateController.Admin = Admin;
                loader.setLocation(getClass().getResource("/DashboardMember/Pages/BookTemplate.fxml"));
                VBox vbox2 = loader.load();
                BookTemplateController BTC = loader.getController();
                BTC.SetBooks(resultSet2.getInt(1), resultSet2.getString(3), resultSet2.getString(4), Status, resultSet2.getBinaryStream(14), 0);
                if (col == 6) {
                    col = 0;
                    ++row;
                }
                i++;
                System.out.println("done");
                gp1.add(vbox2, col++, row);
                GridPane.setMargin(vbox2, new Insets(8));
            }
        } catch (SQLException ex) {
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            FillBookTable();
        } catch (IOException ex) {
            Logger.getLogger(MemberNewBooksController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //book.SearchBook(SearchFieldBook, BookList, BookTable);
    }

    @FXML
    private void undo(MouseEvent event) throws IOException {
        undo.setVisible(false);
        gp1.getChildren().clear();
        Container.getChildren().clear();
        name.setText("Authors:");
        FillBookTable();
    }

}
