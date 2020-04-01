package app.controllers;

import app.enums.MealType;
import app.models.Instruction;
import app.models.Recipe;
import app.models.RecipeManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.Vector;


public class AddRecipeController implements Initializable {

    @FXML
    private Label lblMessage;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtDescription;

    @FXML
    private Spinner spinType;

    @FXML
    private Spinner spinPrepTime;

    @FXML
    private Spinner spinCookTime;

    @FXML
    private ToggleButton toggleFavourite;

    @FXML
    private ToggleButton toggleGuideEnabled;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    private ImageView imageViewAdd;

    @FXML
    private Button btnPrevImage;

    @FXML
    private Button btnNextImage;

    @FXML
    private Button btnAddImage;

    @FXML
    private Button btnRemoveImage;

    @FXML
    private Label lblPicCount;

    private Vector<String> imgVec;

    public AddRecipeController() {}

    @FXML
    private void initialize() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setInfoMessage("Fill in the shown fields to add a new recipe!");

        initBtnListener();
        initMealTypeSpinner();

        FileInputStream input = null;
        try {
            input = new FileInputStream("appdata/images/dummy.png");
            Image tmpRecipeImage = new Image(input, imageViewAdd.getFitWidth(), imageViewAdd.getFitHeight(), false, true);
            imageViewAdd.setImage(tmpRecipeImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        imgVec = new Vector<>();
    }

    private void initBtnListener() {
        btnCancel.setOnAction(event -> { onActionBtnCancel(event); });
        btnSave.setOnAction(event -> { onActionBtnSave(event); });
        btnPrevImage.setOnAction(event -> { onActionBtnPrevImage(event); });
        btnNextImage.setOnAction(event -> { onActionBtnNextImage(event); });
        btnAddImage.setOnAction(event -> { onActionBtnAddImage(event); });
        btnRemoveImage.setOnAction(event -> { onActionBtnRemoveImage(event); });
    }

    private void initMealTypeSpinner() {
        ObservableList<MealType> mealTypes = FXCollections.observableArrayList(MealType.FISH, MealType.PORK, MealType.VEGAN, MealType.BEEF, MealType.CHICKEN, MealType.VEGETARIAN);
        SpinnerValueFactory<MealType> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<MealType>(mealTypes);
        valueFactory.setValue(MealType.BEEF);
        spinType.setValueFactory(valueFactory);
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

    public void onActionBtnSave(ActionEvent actionEvent) {
        if (txtName.getText().isEmpty() || txtDescription.getText().isEmpty()) {
            setErrorMessage("Please enter some values in the text fields!");
        } else {
            Recipe r = new Recipe(txtName.getText(), txtDescription.getText(), (MealType) spinType.getValue(), Duration.ofMinutes(Long.valueOf((int) spinPrepTime.getValue())), Duration.ofMinutes(Long.valueOf((int) spinCookTime.getValue())));
            r.setFavourite(toggleFavourite.isSelected());
            r.setGuideEnabled(toggleGuideEnabled.isSelected());

            //TODO Picture adding - file chooser etc.

            RecipeManager.getInstance().addRecipe(r);

            setSuccessMessage("Added an new Recipe!");

            closeAddWindow();
        }
    }

    public void onActionBtnCancel(ActionEvent actionEvent) {
        closeAddWindow();
    }

    private void closeAddWindow() {
        Stage stage = (Stage) lblMessage.getScene().getWindow();
        stage.close();
    }

    public void onActionBtnPrevImage(ActionEvent actionEvent) {
        //TODO
    }

    public void onActionBtnNextImage(ActionEvent actionEvent) {
        //TODO
    }

    public void onActionBtnAddImage(ActionEvent actionEvent) {
        //TODO
    }

    public void onActionBtnRemoveImage(ActionEvent actionEvent) {
        //TODO
    }
}