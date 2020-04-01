package app.controllers;
import app.enums.MealType;
import app.models.RecipeManager;
import javafx.event.ActionEvent;
import app.models.Recipe;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

//import java.awt.event.ActionEvent;
import javax.swing.text.StyledEditorKit;
import java.net.URL;
import java.time.Duration;
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

    @FXML
    private  Button btn_cancel;

    public AddRecipeController(int inparam1) {
        this.param1 = inparam1;
    }

    @FXML
    private void initialize() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setInfoMessage("Add a new recipe!");
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

        // check if all text field have values
        if (TF_dishname.getText().isEmpty() || TF_description.getText().isEmpty() || TF_dishtype.getText().isEmpty() ||
            TF_preptime.getText().isEmpty() || TF_cooktime.getText().isEmpty()){
            setErrorMessage("Please enter valid Values in the proper fields!");
        }
        else{
            Recipe new_recipe = new Recipe();

            new_recipe.setName(TF_dishname.getText());
            new_recipe.setDescription(TF_description.getText());

            MealType type = MealType.valueOf(TF_dishtype.getText().toUpperCase());
            new_recipe.setType(type);

            try {
                new_recipe.setPrepTime(Duration.ofMinutes(Integer.parseInt(TF_preptime.getText())));
                new_recipe.setCookTime(Duration.ofMinutes(Integer.parseInt(TF_cooktime.getText())));

            }catch (Exception e){
                setErrorMessage("Please enter a valid number for cooking/preparation time (in minutes)");
                e.printStackTrace();
            }

            // check if every attribute has a value
            if (!(new_recipe.getName().isEmpty() || new_recipe.getDescription().isEmpty() || (new_recipe.getType()) == null
                || (new_recipe.getPrepTime() == null) || (new_recipe.getCookTime() == null))){

                RecipeManager.getInstance().addRecipe(new_recipe);

                Stage stage = (Stage) TF_dishtype.getScene().getWindow();
                stage.close();
            }
        }
    }

    public void onActionbtnCancel(ActionEvent actionEvent){
        Stage stage = (Stage) btn_cancel.getScene().getWindow();
        stage.close();
    }

}

