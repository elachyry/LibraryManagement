package DashboardMember.Controllers;

import Data_Base.MySql;
import Models.Tools;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class Books_BorrowedController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private GridPane Container;

    @FXML
    private HBox CategoryLayout;

    @FXML
    private JFXDialog Dialog2;

    @FXML
    private Label DialogLabel;

    @FXML
    private JFXButton YesButton;

    @FXML
    private JFXButton NoButton;

    @FXML
    public static StackPane satckPane;

    @FXML
    private AnchorPane anchor;

    @FXML
    private JFXComboBox<String> Periods;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;

    private Pagination pagination;

    int count;
    int numberPerPager = 5;
    int Skip = 0;
    int Skip2 = 0;
    ObservableList<String> PeridsList = FXCollections.observableArrayList();
    int[] SkipTab;
    public static int Admin = 0;

    public void FillBorrowedBookTable() throws IOException, SQLException {
        count = getBooksBorrowedCount("All");
        System.out.println("getBooksBorrowedCount: " + count);
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
        SkipTab = new int[count];
        pagination.setPageCount(PageCount);
        //pagination.setStyle("-fx-border-color:red;");
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                try {
                    return AllLoans(pageIndex);
                } catch (IOException | SQLException ex) {
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

    @FXML
    void AllBooks(MouseEvent event) throws IOException, SQLException {
        FillBorrowedBookTable();
    }

    @FXML
    private void Pending(MouseEvent event) throws IOException, SQLException {
        Skip2 = 0;
        count = getBooksBorrowedCount("Pending");
        System.out.println("getBooksBorrowedCount: " + count);
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
        System.out.println("befor call");
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                try {
                    System.out.println("in call");
                    return StatusFilter("Pending", pageIndex);
                } catch (IOException | SQLException ex) {
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

    @FXML
    private void Accepted(MouseEvent event) throws IOException, SQLException {
        count = getBooksBorrowedCount("Accepted");
        System.out.println("getBooksBorrowedCount: " + count);
        Skip2 = 0;
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
                    return StatusFilter("Accepted", pageIndex);
                } catch (IOException | SQLException ex) {
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

    @FXML
    private void Canceled(MouseEvent event) throws IOException, SQLException {
        count = getBooksBorrowedCount("Canceled");
        System.out.println("getBooksBorrowedCount: " + count);
        Skip2 = 0;
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
                    return StatusFilter("Canceled", pageIndex);
                } catch (IOException | SQLException ex) {
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

    @FXML
    private void Denied(MouseEvent event) throws IOException, SQLException {
        Skip2 = 0;
        count = getBooksBorrowedCount("Denied");
        System.out.println("getBooksBorrowedCount: " + count);
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
                    return StatusFilter("Denied", pageIndex);
                } catch (IOException | SQLException ex) {
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

    @FXML
    private void Returned(MouseEvent event) throws IOException, SQLException {
        count = getBooksBorrowedCount("Returned");
        System.out.println("getBooksBorrowedCount: " + count);
        Skip2 = 0;
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
                    return StatusFilter("Returned", pageIndex);
                } catch (IOException | SQLException ex) {
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

    public GridPane StatusFilter(String Status, int pageIndex) throws IOException, SQLException {
        Container.getChildren().clear();
        int page = pageIndex * numberPerPager;
        List<Integer> list = new ArrayList<Integer>();
        int Id;
        if (Admin == 0) {
            Tools T = new Tools();
            Id = T.getUsernameId("members");
        } else {
            Id = Admin;
        }
        System.out.println("test");
        Con = MySql.GetConnection();
        int col = 0;
        int row = 0;
        System.out.println("status" + Status);
        Query = "SELECT Id FROM books_borrowed WHERE Status = '" + Status + "' AND Member_Id = " + Id;
        PreStatment = Con.prepareStatement(Query);
        resultSet = PreStatment.executeQuery();
        System.out.println("test3");
        while (resultSet.next()) {
            list.add(resultSet.getInt(1));
            System.out.println(resultSet.getInt(1));
        }
        for (int i = page; i < (page + numberPerPager); i++) {
            System.out.println("i = " + i);
            if (i > list.size() - 1) {
                break;
            }
            Con = MySql.GetConnection();
            try {
                Query = "SELECT DISTINCT b.Id, b.Title, b.Author, b.Book_Image, bb.Status, bb.Id FROM `books` b , books_borrowed bb, members m WHERE b.Id = bb.Book_Id AND bb.Id = " + list.get(i);
                PreStatment = Con.prepareStatement(Query);
                resultSet = PreStatment.executeQuery();
                while (resultSet.next()) {
                    FXMLLoader loader = new FXMLLoader();
                    BookTemplateController.Admin = Admin;
                    loader.setLocation(getClass().getResource("/DashboardMember/Pages/BookTemplate.fxml"));
                    VBox vbox = loader.load();
                    BookTemplateController BTC = loader.getController();
                    BookTemplateController.Borrow_Book = 1;
                    BTC.SetBooks(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(5), resultSet.getBinaryStream(4), resultSet.getInt(6));
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

    public GridPane AllLoans(int pageIndex) throws IOException, SQLException {
        Container.getChildren().clear();
        int page = pageIndex * numberPerPager;
        List<Integer> list = new ArrayList<Integer>();
        int Id;
        if (Admin == 0) {
            Tools T = new Tools();
            Id = T.getUsernameId("members");
        } else {
            Id = Admin;
        }
        Con = MySql.GetConnection();
        int col = 0;
        int row = 0;
        int j = (page + 1) + Skip;
        Query = "SELECT Id FROM books_borrowed WHERE Member_Id = " + Id;
        PreStatment = Con.prepareStatement(Query);
        resultSet = PreStatment.executeQuery();
        while (resultSet.next()) {
            list.add(resultSet.getInt(1));
            //System.out.println(resultSet.getInt(1));
        }
        System.out.println("list size " + list.size());
        for (int i = page; i < (page + numberPerPager); i++) {
            System.out.println("i = " + i);
            if (i > list.size() - 1) {
                break;
            }
            Con = MySql.GetConnection();
            try {
                //System.out.println("list[" + i + "] = " + list.get(i));
                Query = "SELECT DISTINCT b.Id, b.Title, b.Author, b.Book_Image, bb.Status, bb.Id FROM `books` b , books_borrowed bb, members m WHERE b.Id = bb.Book_Id AND bb.Id = " + list.get(i);
                PreStatment = Con.prepareStatement(Query);
                resultSet = PreStatment.executeQuery();
                while (resultSet.next()) {
                    FXMLLoader loader = new FXMLLoader();
                    BookTemplateController.Admin = Admin;
                    loader.setLocation(getClass().getResource("/DashboardMember/Pages/BookTemplate.fxml"));
                    VBox vbox = loader.load();
                    BookTemplateController BTC = loader.getController();
                    BookTemplateController.Borrow_Book = 1;
                    BTC.SetBooks(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(5), resultSet.getBinaryStream(4), resultSet.getInt(6));
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

    public int getBooksBorrowedCount(String Status) throws SQLException {
        Con = MySql.GetConnection();
        String Query2;
        int Id;
        if (Admin == 0) {
            Tools T = new Tools();
            Id = T.getUsernameId("members");
        } else {
            Id = Admin;
        }
        if ("All".equals(Status)) {
            Query2 = "SELECT  COUNT(*) FROM `books_borrowed` WHERE Member_Id = " + Id;
        } else {
            Query2 = "SELECT  COUNT(*) FROM `books_borrowed` WHERE Status = '" + Status + "' AND Member_Id = " + Id;
        }
        PreparedStatement PreStatment2 = Con.prepareStatement(Query2);
        ResultSet resultSet2 = PreStatment2.executeQuery();
        resultSet2.next();
        System.out.println(count);
        return resultSet2.getInt(1);
    }

    public int getBooksBorrowedCountPeriod(String Period) throws SQLException {
        Con = MySql.GetConnection();
        String Query2 = null;
        int Id;
        if (Admin == 0) {
            Tools T = new Tools();
            Id = T.getUsernameId("members");
        } else {
            Id = Admin;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("u");
        SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat formatter4 = new SimpleDateFormat("yyyy");
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if ("Today".equals(Period)) {
            System.out.println("date today " + formatter.format(date));
            Query2 = "SELECT  COUNT(*) FROM `books_borrowed` WHERE Borrowing_Date = '" + formatter.format(date) + "' AND Member_Id = " + Id;
        } else if ("This Week".equals(Period)) {
            int dayNbr = Integer.parseInt(formatter2.format(date));
            c.add(Calendar.DATE, -dayNbr + 1);
            Date Week = c.getTime();
            System.out.println("last week date " + formatter.format(Week));
            Query2 = "SELECT  COUNT(*) FROM `books_borrowed` WHERE Borrowing_Date BETWEEN '" + formatter.format(Week) + "' AND '" + formatter.format(date) + "' AND Member_Id = " + Id;
        } else if ("This Month".equals(Period)) {
            Query2 = "SELECT  COUNT(*) FROM `books_borrowed` WHERE Borrowing_Date BETWEEN '" + formatter3.format(date) + "-01' AND '" + formatter3.format(date) + "-31' AND Member_Id = " + Id;
            System.out.println(Query2);
        } else if ("This Year".equals(Period)) {
            Query2 = "SELECT  COUNT(*) FROM `books_borrowed` WHERE Borrowing_Date BETWEEN '" + formatter4.format(date) + "-01-01' AND '" + formatter4.format(date) + "-12-31' AND Member_Id = " + Id;
            System.out.println(Query2);
        }
        PreparedStatement PreStatment2 = Con.prepareStatement(Query2);
        ResultSet resultSet2 = PreStatment2.executeQuery();
        resultSet2.next();
        System.out.println("Number " + resultSet2.getInt(1));
        return resultSet2.getInt(1);
    }

    public void FillCombo() {
        PeridsList.clear();
        PeridsList.addAll("All", "Today", "This Week", "This Month", "This Year");
        Periods.setItems(null);
        Periods.setItems(PeridsList);
    }

    private void ChangePeriods() {
        Periods.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if ("All".equals(newValue)) {
                try {
                    FillBorrowedBookTable();
                } catch (IOException | SQLException ex) {
                }
            } else if ("Today".equals(newValue)) {
                try {
                    count = getBooksBorrowedCountPeriod("Today");
                    FillBorrowedBookPeriods("Today");
                } catch (IOException | SQLException ex) {
                }
            } else if ("This Week".equals(newValue)) {
                try {
                    count = getBooksBorrowedCountPeriod("This Week");
                    FillBorrowedBookPeriods("This Week");
                } catch (IOException | SQLException ex) {
                }
            } else if ("This Month".equals(newValue)) {
                try {
                    count = getBooksBorrowedCountPeriod("This Month");
                    FillBorrowedBookPeriods("This Month");
                } catch (IOException | SQLException ex) {
                }
            } else if ("This Year".equals(newValue)) {
                try {
                    count = getBooksBorrowedCountPeriod("This Year");
                    FillBorrowedBookPeriods("This Year");
                } catch (IOException | SQLException ex) {
                }
            }
        });
    }

    public void FillBorrowedBookPeriods(String Preiod) throws IOException, SQLException {
        System.out.println("getBooksBorrowedCountPeriod " + Preiod + ": " + count);
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
        System.out.println("page Count " + PageCount);
        pagination.setPageCount(PageCount);
        //pagination.setStyle("-fx-border-color:red;");
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                try {
                    return PeriodsFilter(Preiod, pageIndex);
                } catch (IOException | SQLException ex) {
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

    public GridPane PeriodsFilter(String Period, int pageIndex) throws IOException, SQLException {
        Container.getChildren().clear();
        int page = pageIndex * numberPerPager;
        List<Integer> list = new ArrayList<Integer>();
        int Id;
        if (Admin == 0) {
            Tools T = new Tools();
            Id = T.getUsernameId("members");
        } else {
            Id = Admin;
        }
        System.out.println("test");
        Con = MySql.GetConnection();
        int col = 0;
        int row = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("u");
        SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat formatter4 = new SimpleDateFormat("yyyy");
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if ("Today".equals(Period)) {
            System.out.println("date today " + formatter.format(date));
            Query = "SELECT  Id FROM `books_borrowed` WHERE Borrowing_Date = '" + formatter.format(date) + "' AND Member_Id = " + Id;
        } else if ("This Week".equals(Period)) {
            int dayNbr = Integer.parseInt(formatter2.format(date));
            c.add(Calendar.DATE, -dayNbr + 1);
            Date Week = c.getTime();
            System.out.println("last week date " + formatter.format(Week));
            Query = "SELECT  Id FROM `books_borrowed` WHERE Borrowing_Date BETWEEN '" + formatter.format(Week) + "' AND '" + formatter.format(date) + "' AND Member_Id = " + Id;
        } else if ("This Month".equals(Period)) {
            Query = "SELECT  Id FROM `books_borrowed` WHERE Borrowing_Date BETWEEN '" + formatter3.format(date) + "-01' AND '" + formatter3.format(date) + "-31' AND Member_Id = " + Id;
            System.out.println(Query);
        } else if ("This Year".equals(Period)) {
            Query = "SELECT  Id FROM `books_borrowed` WHERE Borrowing_Date BETWEEN '" + formatter4.format(date) + "-01-01' AND '" + formatter4.format(date) + "-12-31' AND Member_Id = " + Id;
            System.out.println(Query);
        }
        PreStatment = Con.prepareStatement(Query);
        resultSet = PreStatment.executeQuery();
        System.out.println("test3");
        while (resultSet.next()) {
            list.add(resultSet.getInt(1));
            System.out.println(resultSet.getInt(1));
        }
        for (int i = page; i < (page + numberPerPager); i++) {
            System.out.println("i = " + i);
            if (i > list.size() - 1) {
                break;
            }
            Con = MySql.GetConnection();
            try {
                Query = "SELECT DISTINCT b.Id, b.Title, b.Author, b.Book_Image, bb.Status, bb.Id FROM `books` b , books_borrowed bb, members m WHERE b.Id = bb.Book_Id AND bb.Id = " + list.get(i);
                PreStatment = Con.prepareStatement(Query);
                resultSet = PreStatment.executeQuery();
                while (resultSet.next()) {
                    FXMLLoader loader = new FXMLLoader();
                    BookTemplateController.Admin = Admin;
                    loader.setLocation(getClass().getResource("/DashboardMember/Pages/BookTemplate.fxml"));
                    VBox vbox = loader.load();
                    BookTemplateController BTC = loader.getController();
                    BookTemplateController.Borrow_Book = 1;
                    BTC.SetBooks(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(5), resultSet.getBinaryStream(4), resultSet.getInt(6));
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pagination = new Pagination();
        FillCombo();
        try {
            FillBorrowedBookTable();
        } catch (IOException | SQLException ex) {
        }
        ChangePeriods();

    }

}
