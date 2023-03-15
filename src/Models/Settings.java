package Models;

import Data_Base.MySql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Settings {

    private int Id;
    private boolean Send_Emails;
    private String New_Account_Email;
    private String Rest_Loging_Information_Email;
    private String Delete_Account_Email;
    private String Member_Update_Account_Email;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;

    public Settings() {
    }

    public Settings(int Id, boolean Send_Emails, String New_Account_Email, String Rest_Loging_Information_Email, String Delete_Account_Email, String Member_Update_Account_Email) {
        this.Id = Id;
        this.Send_Emails = Send_Emails;
        this.New_Account_Email = New_Account_Email;
        this.Rest_Loging_Information_Email = Rest_Loging_Information_Email;
        this.Delete_Account_Email = Delete_Account_Email;
        this.Member_Update_Account_Email = Member_Update_Account_Email;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public boolean isSend_Emails() {
        return Send_Emails;
    }

    public void setSend_Emails(boolean Send_Emails) {
        this.Send_Emails = Send_Emails;
    }

    public String getNew_Account_Email() {
        return New_Account_Email;
    }

    public void setNew_Account_Email(String New_Account_Email) {
        this.New_Account_Email = New_Account_Email;
    }

    public String getRest_Loging_Information_Email() {
        return Rest_Loging_Information_Email;
    }

    public void setRest_Loging_Information_Email(String Rest_Loging_Information_Email) {
        this.Rest_Loging_Information_Email = Rest_Loging_Information_Email;
    }

    public String getDelete_Account_Email() {
        return Delete_Account_Email;
    }

    public void setDelete_Account_Email(String Delete_Account_Email) {
        this.Delete_Account_Email = Delete_Account_Email;
    }

    public String getMember_Update_Account_Email() {
        return Member_Update_Account_Email;
    }

    public void setMember_Update_Account_Email(String Member_Update_Account_Email) {
        this.Member_Update_Account_Email = Member_Update_Account_Email;
    }

    public void addAdminSettings(int AdminId) {
        try {
            Con = MySql.GetConnection();
            Query = "INSERT INTO `settings`(`Admin_id`, `Send_Emails`, `New_Account_Email`, `Reset_Loging_Information_Email`, `Delete_Account_Email`, `Member_Update_Account_Email`) VALUES (?,?,?,?,?,?)";
            PreStatment = Con.prepareStatement(Query);
            PreStatment.setInt(1, AdminId);
            PreStatment.setString(2, "false");
            PreStatment.setString(3, "You are set. Now you can borrow Books, Check Books List From Your account. "
                    + "  Your login informations:");
            PreStatment.setString(4, "An admin reset your login information for you. "
                    + " Your login informations:");
            PreStatment.setString(5, "An admin delete your account.");
            PreStatment.setString(6, "You have update your account informatins successfully.");            
            PreStatment.execute();
        } catch (SQLException ex) {
        }
    }
    
    public void UpdateAdminSettings(int AdminId, String sendEmails, String NAEmail, String RLIEmail, String DAEmail, String MUAEmail , String NLEmail, String RLEmail) {
        try {
            Con = MySql.GetConnection();
            Query = "UPDATE `settings` SET `Send_Emails`=?,"
                    + "`New_Account_Email`=?,`Reset_Loging_Information_Email`=?,"
                    + "`Delete_Account_Email`=?,`Member_Update_Account_Email`=?"
                    + ",`New_Loan_Email`=?,`Denied_Loan_Email`=? "
                    + "WHERE `Admin_Id`=" + AdminId ;
            PreStatment = Con.prepareStatement(Query);
            PreStatment.setString(1, sendEmails);
            PreStatment.setString(2, NAEmail);
            PreStatment.setString(3, RLIEmail);
            PreStatment.setString(4, DAEmail);
            PreStatment.setString(5, MUAEmail);
            PreStatment.setString(6, NLEmail);
            PreStatment.setString(7, RLEmail);
            PreStatment.execute();
        } catch (SQLException ex) {
        }
    }
    
    public void DeleteAdminSettings(int AdminId) {
        
        try {
            Query = "DELETE FROM `settings` WHERE Admin_id  =" + AdminId;
            Con = MySql.GetConnection();
            PreStatment = Con.prepareStatement(Query);
            PreStatment.execute();
        } catch (SQLException ex) {
        }
    }

}
