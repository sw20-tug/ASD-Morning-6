package app.controllers;

import app.enums.MealType;
import app.models.GlobalConstants;
import app.models.Recipe;
import app.models.RecipeManager;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    private Vector<BufferedImage> bufferVec;

    private int currentImage = 0;

    private Recipe tmpRecipe;

    private Image defaultRecipeImage;

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
            defaultRecipeImage = new Image(input, imageViewAdd.getFitWidth(), imageViewAdd.getFitHeight(), false, true);
            imageViewAdd.setImage(defaultRecipeImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        tmpRecipe = new Recipe();
        bufferVec = new Vector<>();
    }

    private void initBtnListener(){
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
            tmpRecipe.setName(txtName.getText());
            tmpRecipe.setDescription(txtDescription.getText());
            tmpRecipe.setType((MealType) spinType.getValue());
            tmpRecipe.setCookTime(Duration.ofMinutes(Long.valueOf((int) spinPrepTime.getValue())));
            tmpRecipe.setPrepTime(Duration.ofMinutes(Long.valueOf((int) spinCookTime.getValue())));
            tmpRecipe.setFavourite(toggleFavourite.isSelected());
            tmpRecipe.setGuideEnabled(toggleGuideEnabled.isSelected());

            for(int i = 0; i < bufferVec.size(); i++) {
                try {
                    String imageName = GlobalConstants.IMAGE_FOLDER_PATH + tmpRecipe.getId() + "_" + i;
                    ImageIO.write(bufferVec.get(i), "png", new File(imageName));
                    tmpRecipe.addPhoto(imageName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            RecipeManager.getInstance().addRecipe(tmpRecipe);

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
        if(bufferVec.size() > 1) {
            if(currentImage <= 0)
            {
                currentImage = bufferVec.size()-1;
            }
            else
            {
                currentImage--;
            }
            loadImage();
        }
    }

    public void onActionBtnNextImage(ActionEvent actionEvent) {

        if(bufferVec.size() > 1) {
            if(currentImage >= bufferVec.size()-1)
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
                bufferVec.add(img);
                currentImage = bufferVec.size()-1;
                lblPicCount.setText("Photos added (" + bufferVec.size() + ")");

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        btnRemoveImage.setDisable(false);
    }

    public void onActionBtnRemoveImage(ActionEvent actionEvent) {
        bufferVec.remove(currentImage);
        if(currentImage <= 0)
        {
            currentImage = bufferVec.size()-1;
        }
        else
        {
            currentImage--;
        }
        loadImage();
        lblPicCount.setText("Photos added (" + bufferVec.size() + ")");

    }

    private void loadImage() {
        if (bufferVec.size() > 0) {
            imageViewAdd.setImage(SwingFXUtils.toFXImage(bufferVec.get(currentImage), null));
        }
        else {
            imageViewAdd.setImage(defaultRecipeImage);
            btnRemoveImage.setDisable(true);
        }
    }
}