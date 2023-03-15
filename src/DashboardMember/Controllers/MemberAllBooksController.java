package DashboardMember.Controllers;

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
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class MemberAllBooksController implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private JFXTextField SearchFieldBook;
    @FXML
    private GridPane Container;
    @FXML
    private JFXDialog Dialog2;
    @FXML
    private Label DialogLabel;
    @FXML
    private JFXButton YesButton;
    @FXML
    private JFXButton NoButton;
    private Pagination pagination;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;
    public static int Admin = 0;

    int count;
    int numberPerPager = 10;

    @FXML
    private AnchorPane anchor;



    public void FillBookTable() throws IOException {
        System.out.println("page number: " + (count / numberPerPager));
        int PageCount;
        if ((count % numberPerPager) == 0) {
            PageCount = count / numberPerPager;
        } else {
            PageCount = (count / numberPerPager) + 1;
        }
        pagination.setPageCount(PageCount);
        //pagination.setStyle("-fx-border-color:red;");
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                try {
                    return template(pageIndex);
                } catch (IOException ex) {
                }
                return null;
            }
        });

        AnchorPane.setTopAnchor(pagination, 0.0);
        AnchorPane.setRightAnchor(pagination, 0.0);
        AnchorPane.setBottomAnchor(pagination, -20.0);
        AnchorPane.setLeftAnchor(pagination, 0.0);
        anchor.getChildren().addAll(pagination);
    }

    public void SearchResult(String LowerCaseFilter, int num) {
        System.out.println("page number: " + (num / numberPerPager));
        int PageCount;
        if ((count % numberPerPager) == 0) {
            if ((count / numberPerPager) == 0) {
                PageCount = 1;
            } else {
                PageCount = count / numberPerPager;
            }
        } else {
            PageCount = (count / numberPerPager) + 1;
        }
        pagination.setPageCount(PageCount);
        //pagination.setStyle("-fx-border-color:red;");
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                try {
                    return template2(pageIndex, LowerCaseFilter, num);
                } catch (IOException ex) {
                }
                return null;
            }
        });
        anchor.getChildren().clear(); 
        AnchorPane.setTopAnchor(pagination, 0.0);
        AnchorPane.setRightAnchor(pagination, 0.0);
        AnchorPane.setBottomAnchor(pagination, -20.0);
        AnchorPane.setLeftAnchor(pagination, 0.0);
        anchor.getChildren().addAll(pagination);
    }

    public int CountSerchValues(String LowerCaseFilter) throws SQLException {
        Con = MySql.GetConnection();
        String Query3 = "SELECT COUNT(*) FROM `books`WHERE Title LIKE '" + LowerCaseFilter + "%' OR ISBN LIKE '" + LowerCaseFilter + "%'"
                + "OR Author LIKE '" + LowerCaseFilter + "%'";
        PreparedStatement PreStatment3 = Con.prepareStatement(Query3);
        ResultSet resultSet3 = PreStatment3.executeQuery();
        resultSet3.next();
        System.out.println("LowerCaseFilter: " + LowerCaseFilter);
        System.out.println("num 2 = " + resultSet3.getInt(1));
        return resultSet3.getInt(1);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pagination = new Pagination();

        try {
            Con = MySql.GetConnection();
            String Query2 = "SELECT  COUNT(*) FROM `books`";
            PreparedStatement PreStatment2 = Con.prepareStatement(Query2);
            ResultSet resultSet2 = PreStatment2.executeQuery();
            resultSet2.next();
            count = resultSet2.getInt(1);
            System.out.println(count);

            FillBookTable();
        } catch (IOException | SQLException ex) {
        }
        search();
    }

    public GridPane template(int pageIndex) throws IOException {
        Container.getChildren().clear();
        System.out.println("page index " + pageIndex);
        int page = pageIndex * numberPerPager;
        System.out.println("page  " + page);
        System.out.println("page + numberPerPager  " + (page + numberPerPager));

        int col = 0;
        int row = 0;
        for (int i = page + 1; i <= (page + numberPerPager); i++) {
            System.out.println("i = " + i);
            Con = MySql.GetConnection();
            try {
                Query = "SELECT *  FROM `books`WHERE Id = " + i;
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
        return Container;
    }

    public GridPane template2(int pageIndex, String LowerCaseFilter, int num) throws IOException {
        System.out.println("num = " + num);
        Container.getChildren().clear();
        System.out.println("page index " + pageIndex);
        int page = pageIndex * numberPerPager;
        System.out.println("page  " + page);
        System.out.println("page + numberPerPager  " + (page + num));

        int col = 0;
        int row = 0;
        int j = 0;
        for (int i = page + 1; i <= (page + num); i++) {
            System.out.println("i = " + i);
            Con = MySql.GetConnection();
            try {
                Query = "SELECT * FROM `books`WHERE Title LIKE '" + LowerCaseFilter + "%' OR ISBN LIKE '" + LowerCaseFilter + "%'"
                        + "OR Author LIKE '" + LowerCaseFilter + "%'";
                PreStatment = Con.prepareStatement(Query);
                resultSet = PreStatment.executeQuery();
                String Status;
                while (resultSet.next()) {
                    if (j >= num) {
                        break;
                    }
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
                    j++;

                }
            } catch (SQLException ex) {
            }

        }
        return Container;
    }

    public void search() {

        SearchFieldBook.textProperty().addListener((observable, oldValue, newValue) -> {
            String LowerCaseFilter = newValue.toLowerCase();
            try {
                int num = CountSerchValues(LowerCaseFilter);
                SearchResult(LowerCaseFilter, num);
            } catch (SQLException ex) {
            }
        });

    }
}
