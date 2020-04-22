package app.controllers;

import app.enums.MealType;
import app.models.Instruction;
import app.models.Recipe;
import app.models.RecipeManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;


public class EditRecipeController implements Initializable {

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

    private Recipe inRecipe;

    public EditRecipeController(Recipe paramRecipe) {
        this.inRecipe = paramRecipe;
    }

    @FXML
    private void initialize() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setInfoMessage("Welcome to edit! - " + inRecipe.getName());

        initBtnListener();
        initDataFields();
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
        valueFactory.setValue(inRecipe.getType());
        spinType.setValueFactory(valueFactory);
        setInfoMessage("Fill in the shown fields to add a new recipe!");
    }

    private void initDataFields() {
        txtName.setText(inRecipe.getName());
        txtDescription.setText(inRecipe.getDescription());

        initMealTypeSpinner();

        spinPrepTime.getValueFactory().setValue((int)inRecipe.getPrepTime().toMinutes());
        spinCookTime.getValueFactory().setValue((int)inRecipe.getCookTime().toMinutes());

        toggleFavourite.setSelected(inRecipe.isFavourite());
        toggleGuideEnabled.setSelected(inRecipe.isGuideEnabled());

        //TODO set photos

        setSuccessMessage("Inserted current values!");
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
            RecipeManager.getInstance().updateRecipe(inRecipe.getId(), r);

            setSuccessMessage("Updated Recipe!");

            closeEditWindow();
        }
    }

    public void onActionBtnCancel(ActionEvent actionEvent) {
        closeEditWindow();
    }

    private void closeEditWindow() {
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


