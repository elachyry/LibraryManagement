package DashboardAdmin.Pages.Manage;

import Data_Base.MySql;
import Models.Tools;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * FXML Controller class
 *
 * @author ELACHYRY
 */
public class SendEmailsController implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private AnchorPane anchorePane;
    @FXML
    private JFXTextArea Email;
    @FXML
    private JFXButton ColseButton;
    @FXML
    private JFXRadioButton ReminEmail;
    @FXML
    private JFXRadioButton CustomEmail;

    private ToggleGroup group = new ToggleGroup();
    private JFXSnackbar snackbar;

    public static String MemberName = null;
    public static String BookTitle = null;
    public static String BorringDate = null;
    public static String ReturnDate = null;
    String MessageRemaind = "Hello, " + MemberName + ": \n\nWe remind you that a book [" + BookTitle + "]  "
            + "that you borrowed on [" + BorringDate + "] is still not returned yet. "
            + "\nTo avoid additional fees, please return the book until [" + ReturnDate + "]. \n\nRegards, "
            + "Gestion Library.";
    @FXML
    private JFXRadioButton FinalEmail;
    @FXML
    private JFXRadioButton StrongEmail;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        snackbar = new JFXSnackbar(anchorePane);
        ReminEmail.setToggleGroup(group);
        CustomEmail.setToggleGroup(group);
        FinalEmail.setToggleGroup(group);
        StrongEmail.setToggleGroup(group);
        ReminEmail.setSelectedColor(Color.valueOf("#00b7d3"));
        CustomEmail.setSelectedColor(Color.valueOf("#00b7d3"));
        StrongEmail.setSelectedColor(Color.valueOf("#00b7d3"));
        FinalEmail.setSelectedColor(Color.valueOf("#00b7d3"));
        ReminEmail.setSelected(true);
        Email.setText(MessageRemaind);
        SelectMode();
    }

    @FXML
    private void Send_Email(ActionEvent event) throws SQLException, MessagingException {
        if (Tools.netIsAvailable()) {
            if (Email.getText().trim().isEmpty() == false) {
                String[] NameSplit = MemberName.split(" ");
                System.out.println(NameSplit[0]);
                System.out.println(NameSplit[NameSplit.length - 1]);
                Connection Con = MySql.GetConnection();
                String Query = "SELECT Email FROM Members WHERE First_Name = '" + NameSplit[0] + "' AND Last_Name = '" + NameSplit[NameSplit.length - 1] + "'";
                PreparedStatement PreparStm = Con.prepareStatement(Query);
                ResultSet resultSet = PreparStm.executeQuery();
                resultSet.next();
                String Email = resultSet.getString(1);
                System.out.println("email " + Email);
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
                Message message = preapareMessage(session, MyEmail, Email);
                Transport.send(message);
                DialogAlert("Email sent!");
                System.out.println("message sent!");
                this.Email.setFocusColor(Color.valueOf("#8e05c2"));
                this.Email.setUnFocusColor(Color.valueOf("#4d4d4d"));
            } else {
                new animatefx.animation.Shake(Email).play();
                snackbar.show("Email box is empty!", 2000);
                Email.setFocusColor(Color.RED);
                Email.setUnFocusColor(Color.RED);
            }
        } else {
            Tools.DialogAlert(stackPane, anchorePane, "Please Check Your Internet Connection!");
        }
    }

    private Message preapareMessage(Session session, String MyEmail, String Recipient) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MyEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(Recipient));
            message.setSubject("Gestion Biblio");
            message.setText(MessageRemaind);
            return message;
        } catch (MessagingException ex) {
        }
        return null;
    }

    @FXML
    private void Cancel(ActionEvent event) {
        Stage stage = (Stage) CustomEmail.getScene().getWindow();
        stage.close();
    }

    public void SelectMode() {
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ob,
                    Toggle o, Toggle n) {

                JFXRadioButton rb = (JFXRadioButton) group.getSelectedToggle();

                if (rb != null) {
                    if ("Remind Email".equals(rb.getText())) {
                        MessageRemaind = "Hello, " + MemberName + ": \n\nWe remind you that a book [" + BookTitle + "]  "
                                + "that you borrowed on [" + BorringDate + "] is still not returned yet. "
                                + "\nTo avoid additional fees, please return the book until [" + ReturnDate + "]. \n\nRegards, "
                                + "Gestion Library.";
                        Email.setText(MessageRemaind);
                    } else if ("Strong Email".equals(rb.getText())) {
                        MessageRemaind = "Dear, " + MemberName + ": \n\nwe have still not received the book that you "
                                + "borrowed on [" + BorringDate + "]. Its due date has been [" + ReturnDate + "].\n"
                                + "Should the book fail to arrive in 2 days, \nwe will pursue legal action. "
                                + "We urge you to return the book as soon as possible. \n\n"
                                + "Gestion Library.";
                        Email.setText(MessageRemaind);
                    } else if ("Final Email".equals(rb.getText())) {
                        MessageRemaind = "Dear, " + MemberName + ": \n\nAfter several attempts at communication, "
                                + "we have not received the book. We are now forced to take legal action."
                                + "If you think this is a mistake, call us on +212680346100. \n\nGestion Library.";
                        Email.setText(MessageRemaind);
                    } else if ("Custom Email".equals(rb.getText())) {
                        MessageRemaind = "";
                        Email.setText(MessageRemaind);
                    }
                }
            }
        });
    }

    public void DialogAlert(String Message) {
        BoxBlur Blur = new BoxBlur(3, 3, 3);
        JFXDialogLayout DialogLayout = new JFXDialogLayout();
        JFXButton Button = new JFXButton("Okay");
        Button.setStyle(Message);
        Button.setPrefSize(140, 40);
        Button.setFocusTraversable(false);
        Button.setFont(new Font(17));
        Button.setCursor(Cursor.HAND);
        DialogLayout.setActions(Button);
        JFXDialog Dialog = new JFXDialog(stackPane, DialogLayout, JFXDialog.DialogTransition.TOP);
        // DialogLayout.setPrefSize(300, 50);
        Button.addEventHandler(MouseEvent.MOUSE_CLICKED, ((MouseEvent Event) -> {
            Dialog.close();
            Stage stage = (Stage) CustomEmail.getScene().getWindow();
            stage.close();
        }));
        Label label = new Label(Message);
        label.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-font-size: 20px; -fx-text-fill : #52438F;");
        DialogLayout.setBody(label);
        Dialog.show();
        anchorePane.setEffect(Blur);
        Dialog.setOnDialogClosed((JFXDialogEvent Event) -> {
            anchorePane.setEffect(null);
        });
    }

}
