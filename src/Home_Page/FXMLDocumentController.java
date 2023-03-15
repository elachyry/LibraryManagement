/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home_Page;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ELACHYRY
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private ImageView png;

    @FXML
    private ImageView gif;

    @FXML
    private AnchorPane APane1;

    @FXML
    private AnchorPane APane2;

    @FXML
    private FontAwesomeIconView Menu;

    @FXML
    void Mouse_Moved(MouseEvent event) {
        png.setVisible(false);
        gif.setVisible(true);
    }

    @FXML
    void Mouse_Exited(MouseEvent event) {
        png.setVisible(true);
        gif.setVisible(false);
    }

    @FXML
    void Menu_Clicked(MouseEvent event) {
        APane1.setVisible(true);

        FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), APane1);
        fadeTransition1.setFromValue(0);
        fadeTransition1.setToValue(0.15);
        fadeTransition1.play();

        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), APane2);
        translateTransition1.setByX(+600);
        translateTransition1.play();
    }

    @FXML
    void APane1_Clicked(MouseEvent event) {
        FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), APane1);
        fadeTransition1.setFromValue(0.15);
        fadeTransition1.setToValue(0);
        fadeTransition1.play();

        fadeTransition1.setOnFinished(event1 -> {
            APane1.setVisible(false);
        });

        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), APane2);
        translateTransition1.setByX(-600);
        translateTransition1.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        APane1.setVisible(false);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), APane1);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), APane2);
        translateTransition.setByX(-600);
        translateTransition.play();

        
    }

}
