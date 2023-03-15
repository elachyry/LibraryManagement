package DashboardMember.Controllers;

import Data_Base.MySql;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class TemplateController implements Initializable {

    @FXML
    private Label Name;
    @FXML
    private GridPane Container;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;

    public static int Category;
    @FXML
    public Pane pane;
    @FXML
    private HBox body;
    @FXML
    private Pane pane1;

    public void SetCategory(String Category) {
        Name.setText(Category);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Con = MySql.GetConnection();
        int col = 0;
        int row = 1;
        int i = 0;
        try {
            Query = "SELECT * FROM `books` WHERE Category_Id = " + Category ;
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            String Status;
                while(resultSet.next()){
                    if(i == 3){
                        break;
                    }
                if (resultSet.getInt(13) > 0) {
                    Status = "In Stock";
                } else {
                    Status = "Out Of Stock";
                }
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/DashboardMember/Pages/BookTemplate.fxml"));
                VBox vbox = loader.load();
                BookTemplateController BTC = loader.getController();
                BTC.SetBooks(resultSet.getInt(1), resultSet.getString(3), resultSet.getString(4), Status, resultSet.getBinaryStream(14), 0);
                if (col == 6) {
                    col = 0;
                    ++row;
                }
                Container.add(vbox, col++, row);
                GridPane.setMargin(vbox, new Insets(8));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MemberNewBooksController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TemplateController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void ShowMore(MouseEvent event) throws IOException {
        body.setPrefSize(1460, 800);
        Con = MySql.GetConnection();
        int col = 0;
        int row = 1;
        try {
            Query = "SELECT * FROM `books` ";
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
                loader.setLocation(getClass().getResource("/DashboardMember/Pages/BookTemplate.fxml"));
                VBox vbox = loader.load();
                BookTemplateController BTC = loader.getController();
                BTC.SetBooks(resultSet.getInt(1),resultSet.getString(3), resultSet.getString(4), Status, resultSet.getBinaryStream(14),0);
                if(col == 6){
                    col = 0;
                    ++row;
                }
                Container.add(vbox,col++,row);
                GridPane.setMargin(vbox, new Insets(20));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MemberAuthorsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
