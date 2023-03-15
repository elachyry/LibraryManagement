package Models;

import Data_Base.MySql;
import com.jfoenix.controls.JFXTextField;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Predicate;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Member {

    private int Id;
    private String CNE;
    private String First_Name;
    private String Last_Name;
    private String Email;
    private String Address;
    private String Mobile;
    private String Username;
    private String Password;

    public Member() {

    }

    public Member(int Id, String CNE, String First_Name, String Last_Name, String Email, String Address, String Mobile, String Username, String Password) {
        this.Id = Id;
        this.CNE = CNE;
        this.First_Name = First_Name;
        this.Last_Name = Last_Name;
        this.Email = Email;
        this.Address = Address;
        this.Mobile = Mobile;
        this.Username = Username;
        this.Password = Password;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getCNE() {
        return CNE;
    }

    public void setCNE(String CNE) {
        this.CNE = CNE;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String First_Name) {
        this.First_Name = First_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Name(String LastName) {
        this.Last_Name = LastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;

    public void addMember(String Cne, String First_Name, String Last_Name, String email, String address, String mobile, String username, String password) {

        try {
            Con = MySql.GetConnection();
            Query = "INSERT INTO `members`(`CNE`, `First_Name`, `Last_Name`, `Email`, `Address`,"
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
        } catch (SQLException ex) {
        }
    }

    public void UpdateMember(int MemberId, String Cne, String First_Name, String Last_Name, String email, String address, String mobile, String username, String password) {
        try {
            Con = MySql.GetConnection();
            Query = "UPDATE `members` SET `CNE`=?,"
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
        } catch (SQLException ex) {
        }
    }

    public void DeleteMember(int MemberId) {

        try {
            Query = "DELETE FROM `members` WHERE id  =" + MemberId;
            Con = MySql.GetConnection();
            PreStatment = Con.prepareStatement(Query);
            PreStatment.execute();
        } catch (SQLException ex) {
        }
    }

    public void SearchMemebr(JFXTextField SearchFieldMember, ObservableList<Member> MemberList, TableView<Member> MemberTable) {
        FilteredList<Member> filteredDataMember = new FilteredList<>(MemberList, e -> true);
        SearchFieldMember.setOnKeyReleased(e -> {
            SearchFieldMember.textProperty().addListener((observableValue, OldValue, newValue) -> {
                filteredDataMember.setPredicate((Predicate<? super Member>) member -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String LowerCaseFilter = newValue.toLowerCase();
                    if (member.getCNE().toLowerCase().contains(LowerCaseFilter)) {
                        return true;
                    } else if (member.getFirst_Name().toLowerCase().contains(LowerCaseFilter)) {
                        return true;
                    } else if (member.getLast_Name().toLowerCase().contains(LowerCaseFilter)) {
                        return true;
                    } else if (member.getMobile().toLowerCase().contains(LowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Member> sortedListMember = new SortedList<>(filteredDataMember);
            sortedListMember.comparatorProperty().bind(MemberTable.comparatorProperty());
            MemberTable.setItems(sortedListMember);
        });
    }

    public void Export(String Type) throws IOException {
        try {
            Query = "SELECT * FROM `" + Type + "`";
            Con = MySql.GetConnection();
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();

            XSSFWorkbook XFWB = new XSSFWorkbook();
            XSSFSheet XFSheet = XFWB.createSheet(Type + " List");
            XSSFRow HeaderRow = XFSheet.createRow(0);
            HeaderRow.createCell(0).setCellValue("Id");
            HeaderRow.createCell(1).setCellValue("CNE");
            HeaderRow.createCell(2).setCellValue("First Name");
            HeaderRow.createCell(3).setCellValue("Last Name");
            HeaderRow.createCell(4).setCellValue("Email");
            HeaderRow.createCell(5).setCellValue("Address");
            HeaderRow.createCell(6).setCellValue("Mobile");
            HeaderRow.createCell(7).setCellValue("Username");
            HeaderRow.createCell(8).setCellValue("Password");

            int RowNum = 1;
            while (resultSet.next()) {
                XSSFRow Row = XFSheet.createRow(RowNum);
                Row.createCell(0).setCellValue(resultSet.getInt(1));
                Row.createCell(1).setCellValue(resultSet.getString(2));
                Row.createCell(2).setCellValue(resultSet.getString(3));
                Row.createCell(3).setCellValue(resultSet.getString(4));
                Row.createCell(4).setCellValue(resultSet.getString(5));
                Row.createCell(5).setCellValue(resultSet.getString(6));
                Row.createCell(6).setCellValue(resultSet.getString(7));
                Row.createCell(7).setCellValue(resultSet.getString(8));
                Row.createCell(8).setCellValue( Tools.DecryptPassword(resultSet.getString(9)));
                RowNum++;
            }
            try (FileOutputStream FileOutStr = new FileOutputStream("Excel\\" + Type + ".xlsx")) {
                XFWB.write(FileOutStr);
            }

        } catch (IOException | SQLException ex) {
        }
        
    }

    public void Reset(int id, String cne, String Fname, String Lname, String email, String TableName) {
        try {
            Con = MySql.GetConnection();
            Query = "UPDATE `" + TableName + "` SET `Username`=?,`Password`=? WHERE `Id`='" + id + "'";
            PreStatment = Con.prepareStatement(Query);
            PreStatment.setString(1, cne);
            PreStatment.setString(2, cne);
            PreStatment.execute();
            Tools.SendEmail(email, cne, Fname, Lname,"","","", "Update Account");
        } catch (SQLException ex) {
        } catch (Exception ex) {
        }
    }

}
