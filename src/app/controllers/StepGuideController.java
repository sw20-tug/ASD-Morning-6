package app.controllers;

import app.enums.MealType;
import app.models.GlobalConstants;
import app.models.Instruction;
import app.models.Recipe;
import app.models.RecipeManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.Vector;


public class StepGuideController implements Initializable {
    @FXML
    private Label lblMessage;

    @FXML
    private Label lblTask;

    @FXML
    private Label lblDescription;

    @FXML
    private Button btnContinue;

    @FXML
    private Button btnBack;

    @FXML
    private ImageView imgInstruction;

    private Recipe inRecipe;

    private int instructionIndexCounter;

    public StepGuideController(Recipe r) {
        this.inRecipe = r;
    }

    @FXML
    private void initialize() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initListener();
        initViewFields();
        displayInstruction();
    }

    private void initViewFields() {
        instructionIndexCounter = 0;

        if (inRecipe.getCookInstructions().size() == 0) {
            btnBack.setVisible(false);
            btnContinue.setVisible(false);
            setErrorMessage("No instructions found! - Go back and add one.");
        }
    }

    private void initListener() {
        btnContinue.setOnAction(event -> { onActionBtnContinue(event); });
        btnBack.setOnAction(event -> { onActionBtnBack(event); });
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

    private void displayInstruction() {
        if (inRecipe.getCookInstructions().size() > 0) {
            lblTask.setText(inRecipe.getCookInstructions().get(instructionIndexCounter).getTask());
            lblDescription.setText(inRecipe.getCookInstructions().get(instructionIndexCounter).getDescription());
            imgInstruction.setImage(GlobalConstants.getPhotoFromPath(inRecipe.getCookInstructions().get(instructionIndexCounter).getPhoto()));
            setInfoMessage("Instruction " + (instructionIndexCounter + 1) + " of " + inRecipe.getCookInstructions().size());
        }
    }

    public void onActionBtnContinue(ActionEvent actionEvent) {
        if ((instructionIndexCounter + 1) < inRecipe.getCookInstructions().size()) {
            instructionIndexCounter++;
            displayInstruction();
        } else {
            setSuccessMessage("Reach last. Enjoy your meal!");
        }
    }

    public void onActionBtnBack(ActionEvent actionEvent) {
        if (instructionIndexCounter > 0) {
            instructionIndexCounter--;
            displayInstruction();
        } else {
            setErrorMessage("Wrong direction ;)");
        }
    }
}
