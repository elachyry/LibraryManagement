
package DashboardMember.Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class DialogAlertController implements Initializable {

    @FXML
    private ImageView Close;
    @FXML
    private Label Message;

    public void SetMessageText(String Messgae){
        Message.setText(Messgae);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tooltip tooltip = new Tooltip("Exit");
        Tooltip.install(Close, tooltip);
    }    

    @FXML
    private void Close(MouseEvent event) {
        Stage stage = (Stage) Close.getScene().getWindow();
        stage.close();
    }
    
}
