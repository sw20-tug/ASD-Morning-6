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


public class InstructionsController implements Initializable {
    @FXML
    private Label lblMessage;

    @FXML
    private ListView listInstructions;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextArea txtAreaDescription;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSetImage;

    @FXML
    private Button btnRemoveImage;

    @FXML
    private Button btnInstructionUp;

    @FXML
    private Button btnInstructionDown;

    @FXML
    private ImageView imgInstruction;

    private Recipe inRecipe;

    private ObservableList<Instruction> tmpInstructions;

    private Instruction currentInstruction;

    public InstructionsController(Recipe r) {
        this.inRecipe = r;
    }

    @FXML
    private void initialize() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initListener();
        initViewFields();
    }

    private void initViewFields() {
        tmpInstructions = FXCollections.observableList(inRecipe.getCookInstructions());
        listInstructions.setItems(tmpInstructions);

        if (tmpInstructions.size() != 0) {
            currentInstruction = tmpInstructions.get(0);

            imgInstruction.setImage(GlobalConstants.getPhotoFromPath(currentInstruction.getPhoto()));

            if (currentInstruction.getPhoto().isEmpty())
                btnRemoveImage.setVisible(false);

            displayCurrentInstruction();
        } else {
            btnSetImage.setVisible(false);
            btnRemoveImage.setVisible(false);
        }

        setInfoMessage("Welcome to modify instructions!");
    }

    private void initListener() {
        btnCancel.setOnAction(event -> {
            onActionBtnCancel(event);
        });
        btnDelete.setOnAction(event -> {
            onActionBtnDelete(event);
        });
        btnAdd.setOnAction(event -> {
            onActionBtnAdd(event);
        });
        listInstructions.setOnMouseClicked(event -> {
            lvItemClicked(event);
        });
        btnSetImage.setOnAction(event -> {
            onActionBtnSetImage(event);
        });
        btnRemoveImage.setOnAction(event -> {
            onActionBtnRemoveImage(event);
        });
        btnInstructionUp.setOnAction(event -> {
            onActionBtnInstructionUp(event);
        });
        btnInstructionDown.setOnAction(event -> {
            onActionBtnInstructionDown(event);
        });
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

    private void onActionBtnAdd(ActionEvent actionEvent) {
        updateCurrentInstruction();

        tmpInstructions.add(new Instruction("newTitle", "newDescription", ""));
        currentInstruction = tmpInstructions.get(tmpInstructions.size() - 1);
        listInstructions.getSelectionModel().selectLast();

        btnSetImage.setVisible(true);
        btnRemoveImage.setVisible(true);
        displayCurrentInstruction();

        setSuccessMessage("New instruction added, please edit on the right side");
    }

    private void onActionBtnDelete(ActionEvent actionEvent) {
        if (currentInstruction != null) {
            GlobalConstants.removeImageFromMap(currentInstruction.getPhoto());
            tmpInstructions.remove(currentInstruction);
            currentInstruction = null;

            if (tmpInstructions.size() == 0) {
                btnSetImage.setVisible(false);
                btnRemoveImage.setVisible(false);
            }

            displayCurrentInstruction();

            setErrorMessage("What has been done, cannot be undone. muahahahaha");
        }
    }

    private void onActionBtnCancel(ActionEvent actionEvent) {
        updateCurrentInstruction();
        closeInstructionWindow();
    }

    private void closeInstructionWindow() {
        Stage stage = (Stage) lblMessage.getScene().getWindow();
        stage.close();
    }

    private void displayCurrentInstruction() {
        if (currentInstruction != null) {
            txtTitle.setText(currentInstruction.getTask());
            txtAreaDescription.setText(currentInstruction.getDescription());
            imgInstruction.setImage(GlobalConstants.getPhotoFromPath(currentInstruction.getPhoto()));

            if (currentInstruction.getPhoto().isEmpty())
                btnRemoveImage.setVisible(false);
            else
                btnRemoveImage.setVisible(true);
        } else {
            txtTitle.setText("");
            txtAreaDescription.setText("");
            imgInstruction.setImage(null);
        }
    }

    private void lvItemClicked(MouseEvent mouseEvent) {
        updateCurrentInstruction();
        currentInstruction = (Instruction) listInstructions.getSelectionModel().getSelectedItem();
        if (currentInstruction != null) {
            displayCurrentInstruction();
        }

        listInstructions.refresh();
        setSuccessMessage("Instruction updated!");
    }

    private void updateCurrentInstruction() {
        if (currentInstruction != null) {
            currentInstruction.setTask(txtTitle.getText());
            currentInstruction.setDescription(txtAreaDescription.getText());
        }
    }

    private void onActionBtnSetImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        File file = fileChooser.showOpenDialog(btnSetImage.getScene().getWindow());
        if (file != null) {
            try {
                BufferedImage img = ImageIO.read(file);
                String imageName = GlobalConstants.RECIPEIMAGE_FOLDER_PATH + inRecipe.getId() + "_" + System.currentTimeMillis() + ".png";
                ImageIO.write(img, "png", new File(imageName));

                if (!currentInstruction.getPhoto().isEmpty())
                    GlobalConstants.removeImageFromMap(currentInstruction.getPhoto());

                currentInstruction.setPhoto(imageName);

                imgInstruction.setImage(GlobalConstants.getPhotoFromPath(currentInstruction.getPhoto()));
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        btnRemoveImage.setVisible(true);
    }

    private void onActionBtnRemoveImage(ActionEvent actionEvent) {
        GlobalConstants.removeImageFromMap(currentInstruction.getPhoto());
        currentInstruction.setPhoto("");
        imgInstruction.setImage(GlobalConstants.getPhotoFromPath(currentInstruction.getPhoto()));
        btnRemoveImage.setVisible(false);
    }

    private void onActionBtnInstructionDown(ActionEvent event) {
        int selectedIndex = listInstructions.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1) {
            selectedIndex++;

            if (selectedIndex <= tmpInstructions.size() - 1) {
                Instruction tmp = tmpInstructions.get(selectedIndex);
                tmpInstructions.remove(selectedIndex);
                tmpInstructions.add(selectedIndex - 1, tmp);

                setSuccessMessage("Order successfully changed");

                listInstructions.refresh();
            } else {
                setErrorMessage("Cannot push over the limits");
            }
        } else
        {
            setErrorMessage("Select item to change the order");
        }
    }

    private void onActionBtnInstructionUp(ActionEvent event) {
        int selectedIndex = listInstructions.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1) {
            Instruction tmp = tmpInstructions.get(selectedIndex);
            selectedIndex--;

            if (selectedIndex >= 0) {
                tmpInstructions.remove(selectedIndex + 1);
                tmpInstructions.add(selectedIndex, tmp);

                listInstructions.getSelectionModel().select(selectedIndex);

                setSuccessMessage("Order successfully changed");

                listInstructions.refresh();
            } else {
                setErrorMessage("Cannot push over the limits");
            }
        } else {
            setErrorMessage("Select item to change the order");
        }
    }
}