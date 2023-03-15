package DashboardMember.Controllers;

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
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

public class BookTemplateController implements Initializable {

    @FXML
    private ImageView BookImage;
    @FXML
    private Label BookTilte;
    @FXML
    private Label BookAuthor;
    @FXML
    private Label BookStatus;
    @FXML
    private ImageView Fav2;
    @FXML
    private ImageView Fav1;
    @FXML
    private JFXButton button;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;
    int Id_Book = 0;
    int Id_Loan = 0;
    String Loan_Status = null;
    public static int Borrow_Book = 0;
    public static int Admin = 0;
    @FXML
    private VBox vbox;

    public void SetBooks(int Id, String Title, String Author, String Status, InputStream imageFile, int Id_Loan) {
        Id_Book = Id;
        int Id_member;
        if (Admin == 0) {
            Tools T = new Tools();
            Id_member = T.getUsernameId("members");
        } else {
            Id_member = Admin;
        }
        System.out.println("Member Id = " + Id_member);
        this.Id_Loan = Id_Loan;
        Loan_Status = Status;
        BookTilte.setText(Title);
        BookAuthor.setText(Author);
        BookStatus.setText(Status);
        try {
            Query = "SELECT * FROM `favorite` WHERE Member_Id = " + Id_member;
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getInt(3) == Id) {
                    Fav2.setVisible(false);
                    Fav1.setVisible(true);
                }
            }
        } catch (SQLException ex) {
        }
        if ("Out Of Stock".equals(Status)) {
            BookStatus.setStyle("-fx-text-fill : red;");
        } else if ("Pending".equals(Status)) {
            BookStatus.setStyle("-fx-text-fill : #FDDB87;");
        } else if ("Canceled".equals(Status)) {
            BookStatus.setStyle("-fx-text-fill : #6F6F6F;");
        } else if ("Accepted".equals(Status)) {
            BookStatus.setStyle("-fx-text-fill : #A7D1B1;");
        } else if ("Denied".equals(Status)) {
            BookStatus.setStyle("-fx-text-fill : #EC505C;");
        } else if ("Returned".equals(Status)) {
            BookStatus.setStyle("-fx-text-fill : #BDB2FE;");
        } else {
            BookStatus.setStyle("-fx-text-fill : green;");
        }
        Image image = new Image(imageFile);
        BookImage.setImage(image);

        if (Borrow_Book == 0) {
            button.setText("Borrow");
        } else {
            button.setText("Cancel");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Con = MySql.GetConnection();
    }

    @FXML
    private void AddToFav(MouseEvent event) {
        Fav2.setVisible(false);
        Fav1.setVisible(true);
        int Id;
        if (Admin == 0) {
            Tools T = new Tools();
            Id = T.getUsernameId("members");
        } else {
            Id = Admin;
        }
        System.out.println("Id Memebr " + Id);
        System.out.println("Id Book " + Id_Book);
        Query = "INSERT INTO `favorite`(`Member_Id`, `Book_Id`) VALUES (?,?)";
        try {
            PreStatment = Con.prepareStatement(Query);
            PreStatment.setInt(1, Id);
            PreStatment.setInt(2, Id_Book);
            PreStatment.execute();

        } catch (SQLException ex) {
        }

    }

    @FXML
    private void RemoveFromFav(MouseEvent event) {
        Fav2.setVisible(true);
        Fav1.setVisible(false);

        Query = "DELETE FROM `favorite` WHERE Book_Id = " + Id_Book;
        try {
            PreStatment = Con.prepareStatement(Query);
            PreStatment.execute();

        } catch (SQLException ex) {
        }
    }

    @FXML
    private void BookClick(MouseEvent event) {
        if (Borrow_Book == 1) {
            try {
                int Id;
                if (Admin == 0) {
                    Tools T = new Tools();
                    Id = T.getUsernameId("members");
                } else {
                    Id = Admin;
                }
                FXMLLoader loader = new FXMLLoader();
                BookBorroedInfoController.BookId = Id_Book;
                BookBorroedInfoController.MemberId = Id;
                loader.setLocation(getClass().getResource("/DashboardMember/Pages/BookBorroedInfo.fxml"));
                DialogPane AddBookDialog = loader.load();
                BookBorroedInfoController BBIC = loader.getController();
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initStyle(StageStyle.TRANSPARENT);
                dialog.setDialogPane(AddBookDialog);
                Optional<ButtonType> Result = dialog.showAndWait();

            } catch (IOException e) {
            }
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                FullBookInfoController.BookId = Id_Book;
                loader.setLocation(getClass().getResource("/DashboardMember/Pages/FullBookInfo.fxml"));
                DialogPane AddBookDialog = loader.load();
                FullBookInfoController FBIC = loader.getController();
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initStyle(StageStyle.TRANSPARENT);
                dialog.setDialogPane(AddBookDialog);
                Optional<ButtonType> Result = dialog.showAndWait();

            } catch (IOException e) {
            }
        }
    }

    @FXML
    private void ButtonClick(MouseEvent event) throws IOException, SQLException {
        int CountAccepted = 0;
        int DoubleBook = 0;
        int Id_member;
        if (Admin == 0) {
            Tools T = new Tools();
            Id_member = T.getUsernameId("members");
        } else {
            Id_member = Admin;
        }
        Con = MySql.GetConnection();
        Query = "SELECT COUNT(*) FROM books_borrowed WHERE Member_Id = " + Id_member + " AND (status = 'Accepted' OR status = 'Pending')";
        PreStatment = Con.prepareStatement(Query);
        resultSet = PreStatment.executeQuery();
        resultSet.next();
        CountAccepted = resultSet.getInt(1);
        Query = "SELECT COUNT(*) FROM books_borrowed WHERE Member_Id = " + Id_member + " AND Book_Id = " + Id_Book + " AND (status = 'Accepted' OR status = 'Pending')";
        PreStatment = Con.prepareStatement(Query);
        resultSet = PreStatment.executeQuery();
        resultSet.next();
        DoubleBook = resultSet.getInt(1);
        if (Borrow_Book == 1) {
            int Pending = 0;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/DashboardMember/Pages/DialogAlert.fxml"));
            DialogPane AddBookDialog = loader.load();
            DialogAlertController DAC = loader.getController();
            if ("Pending".equals(Loan_Status)) {
                Pending = 1;
                System.out.println("pending");
                FXMLLoader loader2 = new FXMLLoader();
                loader2.setLocation(getClass().getResource("/DashboardMember/Pages/DialogConfirmation.fxml"));
                DialogPane AddBookDialog2 = loader2.load();
                DialogConfirmationController DCC = loader2.getController();
                DCC.SetMessage("Do You Want To Cancel This Loan?");
                DialogConfirmationController.Id_Loan = Id_Loan;
                DialogConfirmationController.Cancel_Loan = 1;
                Dialog<ButtonType> dialog2 = new Dialog<>();
                dialog2.initStyle(StageStyle.TRANSPARENT);
                dialog2.setDialogPane(AddBookDialog2);
                Optional<ButtonType> Result2 = dialog2.showAndWait();

            } else if ("Canceled".equals(Loan_Status)) {
                DAC.SetMessageText("You can't cancel this Loan because\nis already canceled!");
            } else if ("Accepted".equals(Loan_Status)) {
                DAC.SetMessageText("You can't cancel this Loan because\nis already accepted!");
            } else if ("Denied".equals(Loan_Status)) {
                DAC.SetMessageText("You can't cancel this Loan because\nis already denied!");
            } else if ("Returned".equals(Loan_Status)) {
                DAC.SetMessageText("You can't cancel this Loan because\nis already returned!");
            } else {

            }
            if (Pending == 0) {
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initStyle(StageStyle.TRANSPARENT);
                dialog.setDialogPane(AddBookDialog);
                Optional<ButtonType> Result = dialog.showAndWait();
            }
        } else {
             if (DoubleBook != 0) {
                System.out.println("DoubleBook" + DoubleBook);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/DashboardMember/Pages/DialogAlert.fxml"));
                DialogPane AddBookDialog = loader.load();
                DialogAlertController DAC = loader.getController();
                DAC.SetMessageText("You have already borrowed \nthis book!");
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initStyle(StageStyle.TRANSPARENT);
                dialog.setDialogPane(AddBookDialog);
                Optional<ButtonType> Result = dialog.showAndWait();
            } else if (CountAccepted > 3) {
                System.out.println("CountAccepted" + CountAccepted);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/DashboardMember/Pages/DialogAlert.fxml"));
                DialogPane AddBookDialog = loader.load();
                DialogAlertController DAC = loader.getController();
                DAC.SetMessageText("You can't borrow three books \nat the same time!");
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initStyle(StageStyle.TRANSPARENT);
                dialog.setDialogPane(AddBookDialog);
                Optional<ButtonType> Result = dialog.showAndWait();
            }
            else if ("In Stock".equals(BookStatus.getText())) {
                FXMLLoader loader = new FXMLLoader();
                BookBorroedInfoController.BookId = Id_Book;
                DialogConfirmationController.Admin = Admin;
                loader.setLocation(getClass().getResource("/DashboardMember/Pages/DialogConfirmation.fxml"));
                DialogPane AddBookDialog = loader.load();
                DialogConfirmationController DCC = loader.getController();
                DialogConfirmationController.Id_Book = Id_Book;
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initStyle(StageStyle.TRANSPARENT);
                dialog.setDialogPane(AddBookDialog);
                Optional<ButtonType> Result = dialog.showAndWait();
            } else if ("Out Of Stock".equals(BookStatus.getText())) {
                System.out.println(BookStatus.getText());
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/DashboardMember/Pages/DialogAlert.fxml"));
                DialogPane AddBookDialog = loader.load();
                DialogAlertController DAC = loader.getController();
                DAC.SetMessageText("You can't Borrow this Book because\nIt's Out Of Stock!");
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initStyle(StageStyle.TRANSPARENT);
                dialog.setDialogPane(AddBookDialog);
                Optional<ButtonType> Result = dialog.showAndWait();

            } 

        }
    }

}
