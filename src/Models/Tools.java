package Models;

import Data_Base.MySql;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.DocumentException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class Tools {

    private static String Username;
    private static String OldPassword;
    private static String FullName;
    private static boolean SendEmail;
    private static int AdminId;
    static String NAEmail;
    static String RLIEmail;
    static String DAEmail;
    static String MUAEmail;
    static String NLEmail;
    static String DLEmail;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;

    public Tools() {

    }

    public Tools(String Username) {
        Tools.Username = Username;
    }

    public static String getOldPassword() {
        return OldPassword;
    }

    public static void setOldPassword(String OldPassword) {
        Tools.OldPassword = OldPassword;
    }

    public static String getUsername() {
        return Username;
    }

    public static void setUsername(String Username) {
        Tools.Username = Username;
    }

    public static String getFullName() {
        return FullName;
    }

    public static boolean isSendEmail() {
        return SendEmail;
    }

    public static void setSendEmail(boolean SendEmail) {
        Tools.SendEmail = SendEmail;
    }

    public static int getAdminId() {
        return AdminId;
    }

    public static void setAdminId(int AdminId) {
        Tools.AdminId = AdminId;
    }

    public static String getNAEmail() {
        return NAEmail;
    }

    public static void setNAEmail(String NAEmail) {
        Tools.NAEmail = NAEmail;
    }

    public static String getRLIEmail() {
        return RLIEmail;
    }

    public static void setRLIEmail(String RLIEmail) {
        Tools.RLIEmail = RLIEmail;
    }

    public static String getDAEmail() {
        return DAEmail;
    }

    public static void setDAEmail(String DAEmail) {
        Tools.DAEmail = DAEmail;
    }

    public static String getMUAEmail() {
        return MUAEmail;
    }

    public static void setMUAEmail(String MUAEmail) {
        Tools.MUAEmail = MUAEmail;
    }

    /*public  String getFullName(String TableName) {
        String Name = "";
        try {
            Con = MySql.GetConnection();
            Query = "SELECT * FROM `" + TableName + "` WHERE Username = '" + Username + "'";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            resultSet.next();
            UserId = resultSet.getInt(1);
            OldPassword = resultSet.getString(9);
        } catch (SQLException e) {

        }
        return Name;
     */
    public int getUsernameId(String TableName) {
        int UserId = 0;
        try {
            Con = MySql.GetConnection();
            Query = "SELECT * FROM `" + TableName + "` WHERE Username = '" + Username + "'";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            resultSet.next();
            UserId = resultSet.getInt(1);
            FullName = resultSet.getString(3) + " " + resultSet.getString(4);
            OldPassword = DecryptPassword(resultSet.getString(9));
        } catch (SQLException e) {

        }
        AdminId = UserId;
        return UserId;

    }

    public int getLastId() {
        int Id = 0;
        try {
            Con = MySql.GetConnection();
            Query = "SELECT Id FROM `admins` ORDER BY Id ASC";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            while (resultSet.next()) {
                Id = resultSet.getInt(1);
            }
        } catch (SQLException e) {

        }
        return Id;

    }

    public void getmessages() {
        try {
            Con = MySql.GetConnection();
            int AdminId = getUsernameId("admins");
            //System.out.println(AdminId);
            //System.out.println(AdminId);
            Query = "SELECT * FROM `settings` WHERE `Admin_id` = " + AdminId;
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            resultSet.next();
            NAEmail = resultSet.getString(4);
            RLIEmail = resultSet.getString(5);
            DAEmail = resultSet.getString(6);
            MUAEmail = resultSet.getString(7);
            NLEmail = resultSet.getString(8);
            DLEmail = resultSet.getString(9);
        } catch (SQLException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void SendEmail(String Recipient, String cne, String Fname, String Lname, String BookTitle, String BorringDate, String ReturnDate, String Type) throws Exception {
        if (SendEmail == true) {
            if (netIsAvailable()) {
                System.out.println("Praparing sent email");
                Properties properties = new Properties();
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");

                String MyEmail = "gestionbiblioth@gmail.com";
                String MyPassword = "gestionbiblio";

                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(MyEmail, MyPassword);
                    }
                });
                Message message = preapareMessage(session, MyEmail, Recipient, cne, Fname, Lname, BookTitle, BorringDate, ReturnDate, Type);
                Transport.send(message);
                System.out.println("message sent!");
            }
        }

    }

    private static Message preapareMessage(Session session, String MyEmail, String Recipient, String cne, String Fname, String Lname, String BookTitle, String BorringDate, String ReturnDate, String Type) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MyEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(Recipient));
            if ("New Account".equals(Type)) {
                message.setSubject("Welcom to Our Library");
                message.setText("Hello " + Fname + " " + Lname + ", \n\n"
                        + NAEmail
                        + "\n\nUsername: " + cne + "\n"
                        + "Password: " + cne + "\n"
                        + "Note: You can change your Username and Password from your account.");
            }
            if ("Update Account".equals(Type)) {
                message.setSubject("Your account have been updated!");
                message.setText("Hello " + Fname + " " + Lname + ", \n\n"
                        + RLIEmail
                        + "\n\nUsername: " + cne + "\n"
                        + "Password: " + cne + "\n"
                        + "Note: You can change your Username and Password from your account.");
            }
            if ("User Update Account".equals(Type)) {
                message.setSubject("Your account have been updated!");
                message.setText("Hello " + Fname + " " + Lname + ", \n\n"
                        + MUAEmail);
            }
            if ("Delete Account".equals(Type)) {
                message.setSubject("Your account have been deleted!");
                message.setText("Hello " + Fname + " " + Lname + ", \n\n"
                        + DAEmail);
            }
            if ("New Loan".equals(Type)) {
                message.setSubject("You borrowed a book!");
                message.setText("Hello " + Fname + " " + Lname + ", \n\n"
                        + NLEmail
                        + "Book Title: " + BookTitle
                        + "\nBorring Date: " + BorringDate
                        + "\nReturn Date: " + ReturnDate);
            }
            if ("Denied Loan".equals(Type)) {
                message.setSubject("You borrowed a book!");
                message.setText("Hello " + Fname + " " + Lname + ", \n\n"
                        + DLEmail);
            }
            return message;
        } catch (MessagingException ex) {
        }
        return null;
    }

    public int TestColumn(String TableName, String ColumnNmae, String Value) {
        int Count = 0;
        try {
            Con = MySql.GetConnection();
            Query = "SELECT COUNT(*) FROM " + TableName + " WHERE " + ColumnNmae + " = '" + Value + "'";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            resultSet.next();
            Count = resultSet.getInt(1);
        } catch (SQLException ex) {
        }
        return Count;
    }

    public static void DialogAlert(StackPane stackPane, AnchorPane anchorPane, String Message) {
        BoxBlur Blur = new BoxBlur(3, 3, 3);
        JFXDialogLayout DialogLayout = new JFXDialogLayout();
        JFXButton Button = new JFXButton("Okay");
        Button.setStyle(Message);
        Button.setFocusTraversable(false);
        Button.setPrefSize(140, 40);
        Button.setFont(new Font(17));
        Button.setCursor(Cursor.HAND);
        DialogLayout.setActions(Button);
        JFXDialog Dialog = new JFXDialog(stackPane, DialogLayout, JFXDialog.DialogTransition.TOP);
        // DialogLayout.setPrefSize(300, 50);
        Button.addEventHandler(MouseEvent.MOUSE_CLICKED, ((MouseEvent Event) -> {
            Dialog.close();
        }));
        Label label = new Label(Message);
        label.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-font-size: 20px; -fx-text-fill : #52438F;");
        DialogLayout.setBody(label);
        Dialog.show();
        anchorPane.setEffect(Blur);
        Dialog.setOnDialogClosed((JFXDialogEvent Event) -> {
            anchorPane.setEffect(null);
        });
    }

    public static boolean netIsAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

    public String getBookTitle(int BookId) throws SQLException {
        Con = MySql.GetConnection();
        Query = "SELECT `Title` FROM `books` WHERE Id = " + BookId;
        PreStatment = Con.prepareStatement(Query);
        resultSet = PreStatment.executeQuery();
        resultSet.next();
        return resultSet.getString(1);
    }

    public String getMemberName(int MemberId) throws SQLException {
        Con = MySql.GetConnection();
        Query = "SELECT `First_Name`, `Last_Name` FROM `members` WHERE Id = " + MemberId;
        PreStatment = Con.prepareStatement(Query);
        resultSet = PreStatment.executeQuery();
        resultSet.next();
        return resultSet.getString(1) + " " + resultSet.getString(2);
    }

    public int TestLoanExist(String BookTitle) {
        int Count = 0;
        try {
            Con = MySql.GetConnection();
            Query = "SELECT COUNT(*) FROM `books_borrowed` WHERE `Book_Id` = "
                    + "(SELECT Id FROM books WHERE Title = '" + BookTitle + "') AND Status IN('Pending','Accepted')";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            resultSet.next();
            Count = resultSet.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Count;
    }

    public int TestLoanCount(int Memberid) {
        int Count = 0;
        try {
            Con = MySql.GetConnection();
            Query = "SELECT COUNT(*) FROM `books_borrowed` WHERE Member_Id = " + Memberid
                    + " AND Status IN('Pending','Accepted')";
            PreStatment = Con.prepareStatement(Query);
            resultSet = PreStatment.executeQuery();
            resultSet.next();
            Count = resultSet.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Count;
    }

    public int getLastMemberId() throws SQLException {
        Con = MySql.GetConnection();
        Query = "SELECT MAX(Id) FROM members";
        PreStatment = Con.prepareStatement(Query);
        resultSet = PreStatment.executeQuery();
        resultSet.next();
        return (resultSet.getInt(1) + 1);
    }

    public void generateQRcode(int Id, String CNE, String First_Name, String Last_Name, String Email, String Mobile) {
        try {
            String qrCodeData = "Id: " + Id
                    + "\nCNE: " + CNE
                    + "\nFirst Name: " + First_Name
                    + "\nLast Name: " + Last_Name
                    + "\nEmail: " + Email
                    + "\nMobile: " + Mobile;
            String filePath = "BareCodes\\" + First_Name + " " + Last_Name + ".png";
            String charset = "UTF-8"; // or "ISO-8859-1"
            Map< EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap< EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            BitMatrix matrix = new MultiFormatWriter().encode(
                    new String(qrCodeData.getBytes(charset), charset),
                    BarcodeFormat.QR_CODE, 200, 200, hintMap);
            MatrixToImageWriter.writeToFile(matrix, filePath.substring(filePath
                    .lastIndexOf('.') + 1), new File(filePath));
            System.out.println("QR Code image created successfully!");
        } catch (WriterException | IOException e) {
            System.err.println(e);
        }
    }

    public void CreatePdfFile(int Id, String CNE, String First_Name, String Last_Name, String Email, String Address, String Mobile, String Username, String Password) throws FileNotFoundException, DocumentException {
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date();

            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contents = new PDPageContentStream(document, page);
            PDFont font = PDType1Font.HELVETICA;
            PDFont font2 = PDType1Font.HELVETICA_BOLD;

            contents.beginText();
            contents.setFont(font, 20);
            contents.newLineAtOffset(250, 750);
            contents.showText(formatter.format(date));
            contents.endText();

            contents.beginText();
            contents.setFont(font2, 30);
            contents.newLineAtOffset(50, 680);
            contents.showText("GESTION BIBLIO");
            contents.endText();

            contents.beginText();
            contents.setFont(font, 20);
            contents.newLineAtOffset(190, 500);
            contents.showText("Id: " + Id);
            contents.endText();

            contents.beginText();
            contents.setFont(font, 20);
            contents.newLineAtOffset(190, 450);
            contents.showText("CNE: " + CNE);
            contents.endText();

            contents.beginText();
            contents.setFont(font, 20);
            contents.newLineAtOffset(190, 400);
            contents.showText("First Name: " + First_Name);
            contents.endText();

            contents.beginText();
            contents.setFont(font, 20);
            contents.newLineAtOffset(190, 350);
            contents.showText("Last Name: " + Last_Name);
            contents.endText();

            contents.beginText();
            contents.setFont(font, 20);
            contents.newLineAtOffset(190, 300);
            contents.showText("Email: " + Email);
            contents.endText();

            contents.beginText();
            contents.setFont(font, 20);
            contents.newLineAtOffset(190, 250);
            contents.showText("Mobile: " + Mobile);
            contents.endText();

            contents.beginText();
            contents.setFont(font, 20);
            contents.newLineAtOffset(190, 200);
            contents.showText("Address: " + Address);
            contents.endText();

            contents.beginText();
            contents.setFont(font, 20);
            contents.newLineAtOffset(190, 150);
            contents.showText("Username: " + Username);
            contents.endText();

            contents.beginText();
            contents.setFont(font, 20);
            contents.newLineAtOffset(190, 100);
            contents.showText("Password: " + Password);
            contents.endText();

            PDImageXObject imageXObject = PDImageXObject.createFromFile("BareCodes\\" + First_Name + " " + Last_Name + ".png", document);
            contents.drawImage(imageXObject, 450, 610, 150, 150);
            contents.close();

            document.save("PDF\\" + First_Name + " " + Last_Name + ".pdf");

            File file = new File("PDF\\" + First_Name + " " + Last_Name + ".pdf");
            if (!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not  
            {
                System.out.println("not supported");
                return;
            }
            Desktop desktop = Desktop.getDesktop();
            if (file.exists()) //checks file exists or not  
            {
                desktop.open(file);              //opens the specified file  
            }

        } catch (IOException e) {
        }
    }

    public static String EncryptPassword(String Password) {
        Encoder encoder = Base64.getEncoder();
        String originalString = Password;
        String encodedString = encoder.encodeToString(originalString.getBytes());
        System.out.println("Encrypted Value :: " + encodedString);

        return encodedString;
    }
    public static String DecryptPassword(String Password) {
        System.out.println("Password " + Password);
        Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(Password);
                 
        System.out.println("Decrypted Value :: " +new String(bytes));
        return new String(bytes);
    }
    
    public static String generatePassword() {
      String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
      String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
      String specialCharacters = "!@#$";
      String numbers = "1234567890";
      String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
      Random random = new Random();
      char[] password = new char[8];

      password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
      password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
      password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
      password[3] = numbers.charAt(random.nextInt(numbers.length()));
   
      for(int i = 4; i< 8 ; i++) {
         password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
      }
      return new String(password);
   }

}
