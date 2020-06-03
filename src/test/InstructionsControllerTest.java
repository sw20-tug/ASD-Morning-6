package test;

import app.controllers.EditRecipeController;
import app.controllers.InstructionsController;
import app.controllers.StepGuideController;
import app.enums.MealType;
import app.models.GlobalConstants;
import app.models.Instruction;
import app.models.Recipe;
import app.models.RecipeManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.Vector;

import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class InstructionsControllerTest {

    StackPane root = new StackPane();
    Scene scene = new Scene(root, 100, 100);

    private Label lblMessage;
    private ListView listInstructions;
    private TextField txtTitle;
    private TextArea txtAreaDescription;
    private Button btnDelete;
    private Button btnAdd;
    private Button btnCancel;
    private Button btnSetImage;
    private Button btnRemoveImage;
    private Button btnInstructionUp;
    private Button btnInstructionDown;
    private ImageView imgInstruction;
    private Recipe inRecipe;
    private ObservableList<Instruction> tmpInstructions;
    private Instruction currentInstruction;

    private InstructionsController controllerOne;
    private InstructionsController controllerTwo;
    private RecipeManager recipeManager;

    private Vector<Instruction> tempInstructions;

    @Start
    public void start(Stage stage){
        btnDelete = new Button("Delete");
        btnDelete.setId("btnDelete");
        btnDelete.setOnAction(actionEvent -> controllerOne.onActionBtnDelete(actionEvent));

        btnAdd = new Button("Add");
        btnAdd.setId("btnAdd");
        btnAdd.setOnAction(actionEvent -> controllerOne.onActionBtnAdd(actionEvent));

        btnCancel = new Button("Cancel");
        btnCancel.setId("btnCancel");
        btnCancel.setOnAction(actionEvent -> controllerOne.onActionBtnCancel(actionEvent));

        btnSetImage = new Button("Set");
        btnSetImage.setId("btnSetImage");
        btnSetImage.setOnAction(actionEvent -> controllerOne.onActionBtnSetImage(actionEvent));

        btnRemoveImage = new Button("Remove");
        btnRemoveImage.setId("btnRemoveImage");
        btnRemoveImage.setOnAction(actionEvent -> controllerOne.onActionBtnRemoveImage(actionEvent));

        btnInstructionUp = new Button("UP");
        btnInstructionUp.setId("btnInstructionUp");
        btnInstructionUp.setOnAction(actionEvent -> controllerOne.onActionBtnInstructionUp(actionEvent));

        btnInstructionDown = new Button("DOWN");
        btnInstructionDown.setId("btnInstructionDown");
        btnInstructionDown.setOnAction(actionEvent -> controllerOne.onActionBtnInstructionDown(actionEvent));

        VBox vbox = new VBox(1);
        vbox.getChildren().addAll(btnDelete, btnAdd, btnCancel, btnSetImage, btnRemoveImage, btnInstructionUp, btnInstructionDown);

        stage.setScene(scene);
        root.getChildren().add(vbox);

        stage.show();
    }

    @BeforeEach
    public void setup() {

        lblMessage = spy(new Label());
        txtTitle = spy(new TextField());
        txtAreaDescription = spy(new TextArea());
        listInstructions = spy(new ListView());

        Recipe recipe1 = new Recipe("test1", "test1 desc", MealType.FISH, Duration.ofMinutes(5), Duration.ofMinutes(30));
        Recipe recipe2 = new Recipe("test2", "test2 desc", MealType.PORK, Duration.ofMinutes(7), Duration.ofMinutes(5));

        recipe1.setCookInstructions(new Vector<>());
        recipe2.addInstruction(new Instruction("testTask-2-1", "testDescription-2-1", GlobalConstants.DUMMY_IMAGE_PATH));
        recipe2.addInstruction(new Instruction("testTask-2-2", "testDescription-2-2", GlobalConstants.DUMMY_IMAGE_PATH));

        recipeManager = new RecipeManager();

        recipeManager.addRecipe(recipe1);
        recipeManager.addRecipe(recipe2);

        controllerOne = spy(new InstructionsController(lblMessage, listInstructions, txtTitle, txtAreaDescription, btnDelete, btnAdd, btnCancel, btnSetImage, btnRemoveImage, btnInstructionUp, btnInstructionDown, imgInstruction, recipe2, tmpInstructions, currentInstruction));
        controllerTwo = spy(new InstructionsController(lblMessage, listInstructions, txtTitle, txtAreaDescription, btnDelete, btnAdd, btnCancel, btnSetImage, btnRemoveImage, btnInstructionUp, btnInstructionDown, imgInstruction, recipe1, tmpInstructions, currentInstruction));

        RecipeManager.setInstance(recipeManager);
    }

    @Test
    void testRecipeWithNoInstructions() {
        controllerTwo.initViewFields();

        tempInstructions = recipeManager.getRecipes().elementAt(0).getCookInstructions();

        assert tempInstructions.size() == 0;
        assert btnRemoveImage.isVisible() == false;
        assert btnSetImage.isVisible() == false;
        assert lblMessage.getText().contains("Welcome to modify instructions!");
    }

    @Test
    void testRecipeWithMultipleInstructions() {
        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerOne.initViewFields();
        } catch (Exception ex) {}

        tempInstructions = recipeManager.getRecipes().elementAt(1).getCookInstructions();

        assert tempInstructions.size() == 2;
        assert btnRemoveImage.isVisible() == true;
        assert btnSetImage.isVisible() == true;
        assert listInstructions.getItems().size() == tempInstructions.size();
    }

    @Test
    void testAddNewInstruction() {
        controllerTwo.initViewFields();

        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerTwo.onActionBtnAdd(null);
        } catch (Exception ex) {}

        assert txtTitle.getText().equalsIgnoreCase("newTitle");
        assert txtAreaDescription.getText().equalsIgnoreCase("newDescription");
        assert listInstructions.getItems().size() == 1;
    }

    @Test
    void testDeleteInstruction() {
        controllerTwo.initViewFields();

        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerTwo.onActionBtnAdd(null);
        } catch (Exception ex) {}

        assert txtTitle.getText().equalsIgnoreCase("newTitle");
        assert txtAreaDescription.getText().equalsIgnoreCase("newDescription");
        assert listInstructions.getItems().size() == 1;

        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerTwo.onActionBtnDelete(null);
        } catch (Exception ex) {}

        assert listInstructions.getItems().size() == 0;
    }

    @Test
    void testEditInstruction() {
        controllerTwo.initViewFields();

        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerTwo.onActionBtnAdd(null);
        } catch (Exception ex) {}

        assert txtTitle.getText().equalsIgnoreCase("newTitle");
        assert txtAreaDescription.getText().equalsIgnoreCase("newDescription");
        assert listInstructions.getItems().size() == 1;

        String editedTitle = "newNewTitle";
        String editedDescription = "newNewDescription";

        txtTitle.setText(editedTitle);
        txtAreaDescription.setText(editedDescription);

        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerTwo.lvItemClicked(null);
        } catch (Exception ex) {}

        assert ((Instruction)listInstructions.getItems().get(0)).getTask().equalsIgnoreCase(editedTitle);
        assert ((Instruction)listInstructions.getItems().get(0)).getDescription().equalsIgnoreCase(editedDescription);
    }

    @Test
    void testSaveDataOnViewClosing() {
        controllerTwo.initViewFields();

        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerTwo.onActionBtnAdd(null);
        } catch (Exception ex) {}

        assert txtTitle.getText().equalsIgnoreCase("newTitle");
        assert txtAreaDescription.getText().equalsIgnoreCase("newDescription");
        assert listInstructions.getItems().size() == 1;

        String editedTitle = "newNewTitle";
        String editedDescription = "newNewDescription";

        txtTitle.setText(editedTitle);
        txtAreaDescription.setText(editedDescription);

        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerTwo.lvItemClicked(null);
        } catch (Exception ex) {}

        assert ((Instruction)listInstructions.getItems().get(0)).getTask().equalsIgnoreCase(editedTitle);
        assert ((Instruction)listInstructions.getItems().get(0)).getDescription().equalsIgnoreCase(editedDescription);

        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerTwo.onActionBtnCancel(null);
        } catch (Exception ex) {}

        tempInstructions = recipeManager.getRecipes().elementAt(0).getCookInstructions();

        assert tempInstructions.get(0).getTask().equalsIgnoreCase(editedTitle);
        assert tempInstructions.get(0).getDescription().equalsIgnoreCase(editedDescription);
    }

    @Test
    void testInstructionUpWithoutSelection() {
        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerTwo.initViewFields();
        } catch (Exception ex) {}

        try {
            controllerTwo.onActionBtnInstructionUp(null);
        } catch (Exception ex) {}

        assert lblMessage.getText().contains("Select item to change the order");
    }

    @Test
    void testInstructionDownWithoutSelection() {
        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerTwo.initViewFields();
        } catch (Exception ex) {}

        try {
            controllerTwo.onActionBtnInstructionDown(null);
        } catch (Exception ex) {}

        assert lblMessage.getText().contains("Select item to change the order");
    }

    @Test
    void testInstructionOrderChange() {
        controllerTwo.initViewFields();

        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerTwo.onActionBtnAdd(null);
        } catch (Exception ex) {}

        assert txtTitle.getText().equalsIgnoreCase("newTitle");
        assert txtAreaDescription.getText().equalsIgnoreCase("newDescription");
        assert listInstructions.getItems().size() == 1;

        String editedTitle = "newNewTitle";
        String editedDescription = "newNewDescription";

        txtTitle.setText(editedTitle);
        txtAreaDescription.setText(editedDescription);

        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerTwo.lvItemClicked(null);
        } catch (Exception ex) {}

        assert ((Instruction)listInstructions.getItems().get(0)).getTask().equalsIgnoreCase(editedTitle);
        assert ((Instruction)listInstructions.getItems().get(0)).getDescription().equalsIgnoreCase(editedDescription);

        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerTwo.onActionBtnAdd(null);
        } catch (Exception ex) {}

        assert ((Instruction)listInstructions.getItems().get(1)).getTask().equalsIgnoreCase("newTitle");
        assert ((Instruction)listInstructions.getItems().get(1)).getDescription().equalsIgnoreCase("newDescription");

        listInstructions.getSelectionModel().select(0);

        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerTwo.onActionBtnInstructionDown(null);
        } catch (Exception ex) {}

        assert ((Instruction)listInstructions.getItems().get(1)).getTask().equalsIgnoreCase(editedTitle);
        assert ((Instruction)listInstructions.getItems().get(1)).getDescription().equalsIgnoreCase(editedDescription);

        listInstructions.getSelectionModel().select(1);

        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerTwo.onActionBtnInstructionUp(null);
        } catch (Exception ex) {}

        assert ((Instruction)listInstructions.getItems().get(0)).getTask().equalsIgnoreCase(editedTitle);
        assert ((Instruction)listInstructions.getItems().get(0)).getDescription().equalsIgnoreCase(editedDescription);
    }

}
