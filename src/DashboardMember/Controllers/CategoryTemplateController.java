package DashboardMember.Controllers;

import static DashboardMember.Controllers.DialogConfirmationController.Status;
import Data_Base.MySql;
import Models.Tools;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CategoryTemplateController implements Initializable {

    @FXML
    private JFXButton CategoryName;

    @FXML
    private HBox Template;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;

    public static int Author = 0;

    private final String[] colors = {"EC505C", "FDDB87", "46CFC3", "A7D1B1", "FF6C30", "6F6F6F"};

    public void SetCategory(String Category) {
        CategoryName.setText(Category);
        CategoryName.setTooltip(new Tooltip(Category));
        CategoryName.setStyle("-fx-background-color: #" + colors[(int) (Math.random() * colors.length)] + "; "
                + "-fx-background-radios: 15;"
                + "-fx-effect: dropshadow(three-pass-box, rgb(0, 0,0, 0.3), 10, 0, 0, 10);");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void ButtonClick(MouseEvent event) throws IOException {
        if (Author == 1) {
            Con = MySql.GetConnection();
            int col = 0;
            int row = 1;

            try {
                Tools T = new Tools();
                int Id = T.getUsernameId("members");
                Query = "SELECT DISTINCT b.Id, b.Title, b.Author, b.Book_Image, bb.Status, bb.Id, b.Nbr_Copies FROM `books` b , books_borrowed bb, members m WHERE b.Id = bb.Book_Id  AND m.Id = " + Id + " AND bb.Member_Id = " + Id + " AND Status = '" + Status + "'";
                PreStatment = Con.prepareStatement(Query);
                resultSet = PreStatment.executeQuery();
                while (resultSet.next()) {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/DashboardMember/Pages/BookTemplate.fxml"));
                    VBox vbox = loader.load();
                    BookTemplateController BTC = loader
                            .getController();
                    BookTemplateController.Borrow_Book = 1;
                    BTC.SetBooks(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(5), resultSet.getBinaryStream(4), resultSet.getInt(6));

                    if (col == 6) {
                        col = 0;
                        ++row;
                    }
                    GridPane.setMargin(vbox, new Insets(20));
                }

            } catch (SQLException ex) {
            }
        }
    }

}
