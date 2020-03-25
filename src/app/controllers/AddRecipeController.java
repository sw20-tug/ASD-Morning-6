package app.controllers;
import javafx.event.ActionEvent;
import app.models.Recipe;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

//import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;


public class AddRecipeController implements Initializable {

    @FXML
    private Label lblMessage;

    private int param1;

    @FXML
    private TextField TF_dishname;

    @FXML
    private TextField TF_description;

    @FXML
    private TextField TF_dishtype;

    @FXML
    private TextField TF_preptime;

    @FXML
    private TextField TF_cooktime;


    @FXML
    private Button btn_save;

    public AddRecipeController(int inparam1) {
        this.param1 = inparam1;
    }

    @FXML
    private void initialize() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setInfoMessage("Add a new recipe! - " + this.param1);
    }

    private void setInfoMessage(String msg) {
        lblMessage.setStyle("-fx-background-color:yellow; -fx-text-fill:black;");
        lblMessage.setText("INFO --> " + msg);
    }

    private void setErrorMessage(String msg) {
        lblMessage.setStyle("-fx-background-color:red; -fx-text-fill:white;");
        lblMessage.setText("FAILURE --> " + msg);
    }

    private void setSuccessMessage(String msg) {
        lblMessage.setStyle("-fx-background-color:green; -fx-text-fill:white;");
        lblMessage.setText("SUCCESS --> " + msg);
    }



    @FXML
    public void onActionBtnSave(ActionEvent actionEvent){
        setInfoMessage(TF_dishname.getText());


    }
}

