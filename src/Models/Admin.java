package Models;

import DashboardAdmin.Controllers.MembersController;
import DashboardAdmin.Pages.Manage.AddMemberController;
import Data_Base.MySql;
import com.jfoenix.controls.JFXTextField;
import java.sql.SQLException;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;

public class Admin extends Member {

    public Admin() {
    }

    public Admin(int Id, String CNE, String First_Name, String Last_Name, String Email, String Address, String Mobile, String Username, String Password) {
        super(Id, CNE, First_Name, Last_Name, Email, Address, Mobile, Username, Password);
    }

    public void addAdmin(String Cne, String First_Name, String Last_Name, String email, String address, String mobile, String username, String password) {

        try {
            Con = MySql.GetConnection();
            Query = "INSERT INTO `admins`(`CNE`, `First_Name`, `Last_Name`, `Email`, `Address`,"
                    + " `Mobile`, `Username`, `Password`) VALUES (?,?,?,?,?,?,?,?)";
            PreStatment = Con.prepareStatement(Query);
            PreStatment.setString(1, Cne);
            PreStatment.setString(2, First_Name);
            PreStatment.setString(3, Last_Name);
            PreStatment.setString(4, email);
            PreStatment.setString(5, address);
            PreStatment.setString(6, mobile);
            PreStatment.setString(7, username);
            PreStatment.setString(8, Tools.EncryptPassword(password));
            PreStatment.execute();
        } catch (Exception ex) {
            Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void UpdateAdmin(int MemberId, String Cne, String First_Name, String Last_Name, String email, String address, String mobile, String username, String password) {
        try {
            Con = MySql.GetConnection();
            Query = "UPDATE `admins` SET `CNE`=?,"
                    + "`First_Name`=?,`Last_Name`=?,"
                    + "`Email`=?,`Address`=?,`Mobile`=?,"
                    + "`Username`=?,`Password`=? "
                    + "WHERE `Id`='" + MemberId + "'";
            PreStatment = Con.prepareStatement(Query);
            PreStatment.setString(1, Cne);
            PreStatment.setString(2, First_Name);
            PreStatment.setString(3, Last_Name);
            PreStatment.setString(4, email);
            PreStatment.setString(5, address);
            PreStatment.setString(6, mobile);
            PreStatment.setString(7, username);
            PreStatment.setString(8, Tools.EncryptPassword(password));
            PreStatment.execute();
        } catch (Exception ex) {
            Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void DeleteAdmin(int AdminId) {

        try {
            Query = "DELETE FROM `admins` WHERE id  =" + AdminId;
            Con = MySql.GetConnection();
            PreStatment = Con.prepareStatement(Query);
            PreStatment.execute();

        } catch (SQLException ex) {
            Logger.getLogger(MembersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SearchAdmin(JFXTextField SearchFieldAdmin, ObservableList<Admin> AdminList, TableView<Admin> AdminTable) {
        FilteredList<Admin> filteredDataAdmin = new FilteredList<>(AdminList, e -> true);
        SearchFieldAdmin.setOnKeyReleased(e -> {
            SearchFieldAdmin.textProperty().addListener((observableValue, OldValue, newValue) -> {
                filteredDataAdmin.setPredicate((Predicate<? super Admin>) admin -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String LowerCaseFilter = newValue.toLowerCase();
                    if (admin.getCNE().toLowerCase().contains(LowerCaseFilter)) {
                        return true;
                    } else if (admin.getFirst_Name().toLowerCase().contains(LowerCaseFilter)) {
                        return true;
                    } else if (admin.getLast_Name().toLowerCase().contains(LowerCaseFilter)) {
                        return true;
                    } else if (admin.getMobile().toLowerCase().contains(LowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Admin> sortedListAdmin = new SortedList<>(filteredDataAdmin);
            sortedListAdmin.comparatorProperty().bind(AdminTable.comparatorProperty());
            AdminTable.setItems(sortedListAdmin);
        });
    }

}
