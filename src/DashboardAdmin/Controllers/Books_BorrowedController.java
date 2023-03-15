package DashboardAdmin.Controllers;

import Data_Base.MySql;
import Models.Book;
import Models.Books_Borrowed;
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
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class Books_BorrowedController implements Initializable {

    @FXML
    private JFXTextField SearchFieldBook;

    @FXML
    private TableView<Books_Borrowed> BooksBorrowed;

    @FXML
    private TableColumn<Books_Borrowed, Integer> IdCol;

    @FXML
    private TableColumn<Books_Borrowed, String> MemberNameCol;

    @FXML
    private TableColumn<Books_Borrowed, String> BookTitleCol;

    @FXML
    private TableColumn<Books_Borrowed, String> BorrowingDateCol;

    @FXML
    private TableColumn<Books_Borrowed, Integer> NbrOfDaysCol;

    @FXML
    private TableColumn<Books_Borrowed, String> ReturnDateCol;

    @FXML
    private TableColumn<Books_Borrowed, String> StatusCol;

    @FXML
    private TableColumn<Books_Borrowed, JFXButton> ActionCol;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;
    Books_Borrowed BooksB;
    public static ObservableList<Books_Borrowed> BorrowedBookList = FXCollections.observableArrayList();
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
    private JFXButton Accept;
    @FXML
    private JFXButton Denied;
    @FXML
    private JFXButton Returned;
    @FXML
    private JFXButton Export;

    @FXML
    private void Accept_Loan(MouseEvent event) {
        BooksB = BooksBorrowed.getSelectionModel().getSelectedItem();
        if (BooksB != null) {
            if ("Accepted".equals(BooksB.getStatus())) {
                Tools.DialogAlert(stackPane, anchorPane, "This Loan is already accepted!");
            } else if ("Denied".equals(BooksB.getStatus())) {
                Tools.DialogAlert(stackPane, anchorPane, "You can't accept this Loan because is already denied!");
            } else if ("Canceled".equals(BooksB.getStatus())) {
                Tools.DialogAlert(stackPane, anchorPane, "You can't accept this Loan because is already canceled!");
            } else if ("Returned".equals(BooksB.getStatus())) {
                Tools.DialogAlert(stackPane, anchorPane, "You can't accept this Loan because is already returned!");
            } else {
                BoxBlur Blur = new BoxBlur(3, 3, 3);
                anchorPane.setEffect(Blur);
                YesButton.setPrefSize(140, 40);
                YesButton.setFont(new Font(17));
                YesButton.setCursor(Cursor.HAND);
                NoButton.setPrefSize(140, 40);
                NoButton.setFont(new Font(17));
                NoButton.setCursor(Cursor.HAND);
                DialogLabel.setText("Do you want to accept this Loan?");
                DialogLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-font-size: 20px; -fx-text-fill : #52438F;");
                Dialog2.setTransitionType(JFXDialog.DialogTransition.TOP);
                Dialog2.setDialogContainer(stackPane);
                Dialog2.show();

                YesButton.setOnAction((Event) -> {
                    try {
                        String[] NameSplit = BooksB.getMember_Name().split(" ");
                        System.out.println(NameSplit[0]);
                        System.out.println(NameSplit[NameSplit.length - 1]);
                        Con = MySql.GetConnection();
                        String Query1 = "SELECT Email FROM Members WHERE First_Name = '" + NameSplit[0] + "' AND Last_Name = '" + NameSplit[NameSplit.length - 1] + "'";
                        PreparedStatement PreparStm = Con.prepareStatement(Query1);
                        ResultSet resultSet1 = PreparStm.executeQuery();
                        resultSet1.next();
                        String Email = resultSet1.getString(1);

                        BooksB.EditStatusLoan(BooksB.getId(), "Accepted");
                        Book b = new Book();
                        b.Incr_Dec_nbrOfBooks(BooksB.getId(), '-');
                        anchorPane.setEffect(null);
                        Dialog2.close();
                        FillBorrowedBookTable();
                        Tools.SendEmail(Email, "", NameSplit[0], NameSplit[NameSplit.length - 1], BooksB.getBook_Title(), BooksB.getBorrowing_Date(), BooksB.getReturn_Date(), "New Loan");
                    } catch (SQLException ex) {
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

            }
        } else {
            Tools.DialogAlert(stackPane, anchorPane, "No row selected!");
        }
    }

    @FXML
    private void Denied_Loan(MouseEvent event) {
        BooksB = BooksBorrowed.getSelectionModel().getSelectedItem();
        if (BooksB != null) {
            if ("Denied".equals(BooksB.getStatus())) {
                Tools.DialogAlert(stackPane, anchorPane, "This Loan is already denied!");
            } else if ("Accepted".equals(BooksB.getStatus())) {
                Tools.DialogAlert(stackPane, anchorPane, "You can't denied this Loan because is already accepted!");
            } else if ("Canceled".equals(BooksB.getStatus())) {
                Tools.DialogAlert(stackPane, anchorPane, "You can't denied this Loan because is already canceled!");
            } else if ("Returned".equals(BooksB.getStatus())) {
                Tools.DialogAlert(stackPane, anchorPane, "You can't denied this Loan because is already returned!");
            } else {
                BoxBlur Blur = new BoxBlur(3, 3, 3);
                anchorPane.setEffect(Blur);
                YesButton.setPrefSize(140, 40);
                YesButton.setFont(new Font(17));
                YesButton.setCursor(Cursor.HAND);
                NoButton.setPrefSize(140, 40);
                NoButton.setFont(new Font(17));
                NoButton.setCursor(Cursor.HAND);
                DialogLabel.setText("Do you want to denied this Loan?");
                DialogLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-font-size: 20px; -fx-text-fill : #52438F;");
                Dialog2.setTransitionType(JFXDialog.DialogTransition.TOP);
                Dialog2.setDialogContainer(stackPane);
                Dialog2.show();

                YesButton.setOnAction((Event) -> {
                    try {
                        String[] NameSplit = BooksB.getMember_Name().split(" ");
                        System.out.println(NameSplit[0]);
                        System.out.println(NameSplit[NameSplit.length - 1]);
                        Con = MySql.GetConnection();
                        String Query1 = "SELECT Email FROM Members WHERE First_Name = '" + NameSplit[0] + "' AND Last_Name = '" + NameSplit[NameSplit.length - 1] + "'";
                        PreparedStatement PreparStm = Con.prepareStatement(Query1);
                        ResultSet resultSet1 = PreparStm.executeQuery();
                        resultSet1.next();
                        String Email = resultSet1.getString(1);
                        BooksB.EditStatusLoan(BooksB.getId(), "Denied");
                        anchorPane.setEffect(null);
                        Dialog2.close();
                        FillBorrowedBookTable();
                        Tools.SendEmail(Email, "", NameSplit[0], NameSplit[NameSplit.length - 1], "", "", "", "Denied Loan");
                    } catch (SQLException ex) {
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
            }
        } else {
            Tools.DialogAlert(stackPane, anchorPane, "No row selected!");
        }
    }

    @FXML
    private void Returned_Loan(MouseEvent event) {
        BooksB = BooksBorrowed.getSelectionModel().getSelectedItem();
        if (BooksB != null) {
            if ("Returned".equals(BooksB.getStatus())) {
                Tools.DialogAlert(stackPane, anchorPane, "This Loan is already returned!");
            } else if ("Denied".equals(BooksB.getStatus())) {
                Tools.DialogAlert(stackPane, anchorPane, "This Loan because is already denied!");
            } else if ("Canceled".equals(BooksB.getStatus())) {
                Tools.DialogAlert(stackPane, anchorPane, "This Loan because is already canceled!");
            } else if ("Pending".equals(BooksB.getStatus())) {
                Tools.DialogAlert(stackPane, anchorPane, "This Loan is still on pending!");
            } else {
                BoxBlur Blur = new BoxBlur(3, 3, 3);
                anchorPane.setEffect(Blur);
                YesButton.setPrefSize(140, 40);
                YesButton.setFont(new Font(17));
                YesButton.setCursor(Cursor.HAND);
                NoButton.setPrefSize(140, 40);
                NoButton.setFont(new Font(17));
                NoButton.setCursor(Cursor.HAND);
                DialogLabel.setText("Has this book been returned?");
                DialogLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-font-size: 20px; -fx-text-fill : #52438F;");
                Dialog2.setTransitionType(JFXDialog.DialogTransition.TOP);
                Dialog2.setDialogContainer(stackPane);
                Dialog2.show();

                YesButton.setOnAction((Event) -> {
                    BooksB.EditStatusLoan(BooksB.getId(), "Returned");
                    Book b = new Book();
                    b.Incr_Dec_nbrOfBooks(BooksB.getId(), '+');
                    anchorPane.setEffect(null);
                    Dialog2.close();
                    FillBorrowedBookTable();
                });
                Dialog2.setOnDialogClosed((JFXDialogEvent Event) -> {
                    anchorPane.setEffect(null);
                });
                NoButton.setOnAction((Event) -> {
                    anchorPane.setEffect(null);
                    Dialog2.close();
                });

            }
        } else {
            Tools.DialogAlert(stackPane, anchorPane, "No row selected!");
        }
    }

    @FXML
    private void Export(MouseEvent event) throws IOException {
        BooksB = new Books_Borrowed();
        BooksB.Export();
        Tools.DialogAlert(stackPane, anchorPane, "Books Borrowed have been exported successfully!");
        Desktop.getDesktop().open(new File("Excel\\BooksBorrowed.xlsx"));
    }

    public void FillBorrowedBookTable() {
        try {
            BorrowedBookList.clear();
            Con = MySql.GetConnection();
            Query = "SELECT * FROM `books_borrowed` ORDER BY Id DESC";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            while (resultSet.next()) {
                BorrowedBookList.add(new Books_Borrowed(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        new Tools().getBookTitle(resultSet.getInt(7)),
                        new Tools().getMemberName(resultSet.getInt(6)),
                        new JFXButton("Send Email")
                ));
                BooksBorrowed.setItems(BorrowedBookList);
            }

        } catch (SQLException ex) {
        }
        IdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        BookTitleCol.setCellValueFactory(new PropertyValueFactory<>("Book_Title"));
        MemberNameCol.setCellValueFactory(new PropertyValueFactory<>("Member_Name"));
        BorrowingDateCol.setCellValueFactory(new PropertyValueFactory<>("Borrowing_Date"));
        NbrOfDaysCol.setCellValueFactory(new PropertyValueFactory<>("Number_Of_Days"));
        ReturnDateCol.setCellValueFactory(new PropertyValueFactory<>("Return_Date"));
        StatusCol.setCellValueFactory(new PropertyValueFactory<>("Status"));
        ActionCol.setCellValueFactory(new PropertyValueFactory<>("SenEmail"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Accept.setTooltip(new Tooltip("Accept Loan"));
        Denied.setTooltip(new Tooltip("Denied Loan"));
        Returned.setTooltip(new Tooltip("Returned Loan"));
        Export.setTooltip(new Tooltip("Export all Loans To Excel file"));
        SearchFieldBook.setTooltip(new Tooltip("Search for Loans"));
        FillBorrowedBookTable();
        BooksB = new Books_Borrowed();
        BooksB.SearchBook(SearchFieldBook, BorrowedBookList, BooksBorrowed);
    }

}
