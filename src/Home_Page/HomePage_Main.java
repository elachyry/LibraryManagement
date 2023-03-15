/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home_Page;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author ELACHYRY
 */
public class HomePage_Main extends Application {
    
    double X,Y = 0;
    
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        root.setOnMousePressed((event) -> {
            X = event.getSceneX();
            Y = event.getSceneY();
        });
        
        root.setOnMouseDragged((event) -> {
            stage.setX(event.getSceneX() - X);
            stage.setY(event.getSceneY() - Y);
        });
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
