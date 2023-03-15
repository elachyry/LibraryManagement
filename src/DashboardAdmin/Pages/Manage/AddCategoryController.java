package DashboardAdmin.Pages.Manage;

import Data_Base.MySql;
import Models.Category;
import Models.Tools;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AddCategoryController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane anchorePane;

    @FXML
    private JFXTextField Name;

    @FXML
    private Label TitleLabel;

    @FXML
    private JFXTextArea Description;

    @FXML
    private JFXButton AddButton;
    
    @FXML
    private JFXButton ColseButton;

    private JFXSnackbar snackbar;

    String Query = null;
    Connection Con = null;
    PreparedStatement PreStatment = null;
    ResultSet resultSet = null;
    Category category = null;
    int CategoryId;
    private boolean Edit;

    @FXML
    void Add_Category(ActionEvent event) {
        Con = MySql.GetConnection();
        String name = Name.getText();
        String description = Description.getText();

        if (name.isEmpty()) {
            if (name.isEmpty()) {
                Name.setFocusColor(Color.RED);
                Name.setUnFocusColor(Color.RED);
                new animatefx.animation.Shake(Name).play();
                snackbar.show("Category's name is required!", 2000);
            } else {
                Name.setFocusColor(Color.valueOf("#00b4d8"));
                Name.setUnFocusColor(Color.valueOf("#4d4d4d"));
            }

        } else {
            category = new Category();
            if (Edit == false) {
                category.addCategory(name, 0, description);
                Tools.DialogAlert(stackPane, anchorePane,"Category has been added successfully");
                Clean();
            } else {
                category.UpdateCategory(CategoryId, name, 0, description);
                Tools.DialogAlert(stackPane, anchorePane,"Category has been update successfully");

            }
        }
    }

    @FXML
    void Cancel(ActionEvent event) {
        Stage stage = (Stage) Name.getScene().getWindow();
        stage.close();
    }

    public void Clean() {
        Name.setText(null);
        Description.setText(null);

    }

    public void EditData(int id, String name, int nbrBooks, String description) {
        CategoryId = id;
        Name.setText(name);
        Description.setText(description);
    }

    public Label getTitleLabel() {
        return TitleLabel;
    }

    public void setTitleLabel(String TitleLabel) {
        this.TitleLabel.setText(TitleLabel);
    }

    public void setButtonText(String ButtonText) {
        this.AddButton.setText(ButtonText);
    }

    public boolean isEdit() {
        return Edit;
    }

    public void setEdit(boolean Edit) {
        this.Edit = Edit;
    }
    public void setNameDisable(boolean Disable) {
        this.Name.setDisable(Disable);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        snackbar = new JFXSnackbar(anchorePane);
    }

}
