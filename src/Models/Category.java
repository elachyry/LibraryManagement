package Models;

import DashboardAdmin.Controllers.CategoriesController;
import DashboardAdmin.Pages.Manage.ShowBooksController;
import DashboardMember.DashboardController;
import Data_Base.MySql;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableView;
import javafx.stage.StageStyle;

public class Category {

    private int Id;
    private String Name;
    private int Number_Of_Books;
    private String Description;
    private JFXButton ShowBooks;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;

    public Category() {
    }

    public Category(String Name) {
        this.Name = Name;
    }

    public Category(int Id, String Name, int Number_Of_Books, String Description, JFXButton ShowBooks) {
        this.Id = Id;
        this.Name = Name;
        this.Number_Of_Books = Number_Of_Books;
        this.Description = Description;
        this.ShowBooks = ShowBooks;

        ShowBooks.setCursor(Cursor.HAND);
        ShowBooks.setFocusTraversable(false);
        ShowBooks.setOnAction((event) -> {
            ObservableList<Category> CategoryList = null;
            if (DashboardController.IsMember) {
                //CategoryList = MemberCategoriesController.CategoryList;
            } else {
                CategoryList = CategoriesController.CategoryList;
            }
            for (Category category : CategoryList) {
                if (category.getShowBooks() == this.ShowBooks) {
                    ShowBooksController.FiterBy = "Category";
                    ShowBooksController.CategoryId = category.getId();
                    DashboardController.AdminShowBooks = true;
                    DashboardController.MemberShowBooks = true;
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/DashboardAdmin/Pages/Manage/ShowBooks.fxml"));
                        DialogPane ShowBooksDialog = loader.load();
                        ShowBooksController SBC = loader.getController();
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setTitle(category.getName() + "'s Books");
                        dialog.initStyle(StageStyle.TRANSPARENT);
                        dialog.setDialogPane(ShowBooksDialog);
                        Optional<ButtonType> Result = dialog.showAndWait();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getNumber_Of_Books() {
        return Number_Of_Books;
    }

    public void setNumber_Of_Books(int Number_Of_Books) {
        this.Number_Of_Books = Number_Of_Books;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public void addCategory(String Name, int nbrBooks, String description) {
        try {
            Con = MySql.GetConnection();
            Query = "INSERT INTO `categories` (`Name`, `Number_Of_Books`, `Description`) VALUES (?,?,?)";
            PreStatment = Con.prepareStatement(Query);
            PreStatment.setString(1, Name);
            PreStatment.setInt(2, nbrBooks);
            PreStatment.setString(3, description);
            PreStatment.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void UpdateCategory(int id, String Name, int nbrBooks, String description) {

        try {
            Con = MySql.GetConnection();
            Query = "UPDATE `categories` SET `Name`=?,`Number_Of_Books`=?,`Description`=? WHERE `Id`='" + id + "'";
            PreStatment = Con.prepareStatement(Query);
            PreStatment.setString(1, Name);
            PreStatment.setInt(2, nbrBooks);
            PreStatment.setString(3, description);
            PreStatment.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void DeleteCategory(int CategoryId) {
        try {
            Query = "DELETE FROM `categories` WHERE id  =" + CategoryId;
            Con = MySql.GetConnection();
            PreStatment = Con.prepareStatement(Query);
            PreStatment.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SearchCategory(JFXTextField SearchFieldCategory, ObservableList<Category> CategoryList, TableView<Category> CategoryTable) {
        FilteredList<Category> filteredDataCategory = new FilteredList<>(CategoryList, e -> true);
        SearchFieldCategory.setOnKeyReleased(e -> {
            SearchFieldCategory.textProperty().addListener((observableValue, OldValue, newValue) -> {
                filteredDataCategory.setPredicate((Predicate<? super Category>) category -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String LowerCaseFilter = newValue.toLowerCase();
                    if (category.getName().toLowerCase().contains(LowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Category> sortedListCategory = new SortedList<>(filteredDataCategory);
            sortedListCategory.comparatorProperty().bind(CategoryTable.comparatorProperty());
            CategoryTable.setItems(sortedListCategory);
        });
    }

    public void Incr_Dec_nbrOfBooks(String CategoryName, char Operation) {

        try {
            Con = MySql.GetConnection();
            Query = "UPDATE `categories` SET `Number_Of_Books`= `Number_Of_Books` " + Operation + " 1 WHERE `Id` = (SELECT DISTINCT Category_Id From `books` WHERE `Category` = '" + CategoryName + "')";
            PreStatment = Con.prepareStatement(Query);
            PreStatment.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        return Name;
    }

    public JFXButton getShowBooks() {
        return ShowBooks;
    }

    public void setShowBooks(JFXButton ShowBooks) {
        this.ShowBooks = ShowBooks;
    }

}
