package app.controllers;

import app.enums.MealType;
import app.models.GlobalConstants;
import app.models.Recipe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;


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

    private int currentImage = 0;

    private Vector<String> editedImages;


    public EditRecipeController(Recipe paramRecipe) {
        this.inRecipe = paramRecipe;
    }

    @FXML
    private void initialize() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setInfoMessage("Welcome to edit! - " + inRecipe.getName());

        editedImages = (Vector<String>) inRecipe.getPhotos().clone();
        initBtnListener();
        initDataFields();
    }

    private void initBtnListener() {
        btnCancel.setOnAction(this::onActionBtnCancel);
        btnSave.setOnAction(this::onActionBtnSave);
        btnPrevImage.setOnAction(this::onActionBtnPrevImage);
        btnNextImage.setOnAction(this::onActionBtnNextImage);
        btnAddImage.setOnAction(this::onActionBtnAddImage);
        btnRemoveImage.setOnAction(this::onActionBtnRemoveImage);
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
        lblPicCount.setText("Photos added (" + editedImages.size() + ")");
        imageViewAdd.setImage(GlobalConstants.getPhotoFromPath(editedImages.size() != 0 ? editedImages.firstElement() : GlobalConstants.DUMMY_IMAGE_PATH));
        if(editedImages.size() < 1)
            btnRemoveImage.setDisable(true);


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
            inRecipe.setName(txtName.getText());
            inRecipe.setDescription(txtDescription.getText());
            inRecipe.setType((MealType) spinType.getValue());
            inRecipe.setPrepTime(Duration.ofMinutes(Long.valueOf((int) spinPrepTime.getValue())));
            inRecipe.setCookTime(Duration.ofMinutes(Long.valueOf((int) spinCookTime.getValue())));
            inRecipe.setFavourite(toggleFavourite.isSelected());
            inRecipe.setGuideEnabled(toggleGuideEnabled.isSelected());

            List<String> toDelete = new ArrayList<>(inRecipe.getPhotos());
            toDelete.removeAll(editedImages);
            for(String s : toDelete){
                File f = new File(s);
                f.delete();
            }
            inRecipe.setPhotos(editedImages);


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
        if(editedImages.size() > 1) {
            if(currentImage <= 0)
            {
                currentImage = editedImages.size()-1;
            }
            else
            {
                currentImage--;
            }
            loadImage();
        }
    }

    public void onActionBtnNextImage(ActionEvent actionEvent) {

        if(editedImages.size() > 1) {
            if(currentImage >= editedImages.size()-1)
            {
                currentImage = 0;
            }
            else
            {
                currentImage++;
            }
            loadImage();
        }
    }

    public void onActionBtnAddImage(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        File file = fileChooser.showOpenDialog(btnAddImage.getScene().getWindow());
        if (file != null) {
            try {
                BufferedImage img = ImageIO.read(file);
                imageViewAdd.setImage(SwingFXUtils.toFXImage(img, null));

                String imageName = GlobalConstants.RECIPEIMAGE_FOLDER_PATH + inRecipe.getId() + "_" + System.currentTimeMillis() + ".png";
                ImageIO.write(img, "png", new File(imageName));
                editedImages.add(imageName);

                lblPicCount.setText("Photos added (" + editedImages.size() + ")");

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        btnRemoveImage.setDisable(false);

    }

    public void onActionBtnRemoveImage(ActionEvent actionEvent) {
        if(editedImages.size() > 0)
            editedImages.remove(currentImage);

        if(currentImage <= 0)
        {
            currentImage = editedImages.size()-1;
        }
        else
        {
            currentImage--;
        }
        loadImage();
        lblPicCount.setText("Photos added (" + editedImages.size() + ")");
    }

    private void loadImage() {
        if (editedImages.size() > 0) {
            imageViewAdd.setImage(GlobalConstants.getPhotoFromPath(editedImages.elementAt(currentImage)));

        }
        else {
            imageViewAdd.setImage(GlobalConstants.getPhotoFromPath(GlobalConstants.DUMMY_IMAGE_PATH));
            btnRemoveImage.setDisable(true);
        }
    }
}


