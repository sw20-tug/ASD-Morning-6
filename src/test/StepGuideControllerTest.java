package test;

import app.controllers.EditRecipeController;
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

import static org.mockito.Mockito.spy;

@ExtendWith(ApplicationExtension.class)
public class StepGuideControllerTest {

    StackPane root = new StackPane();
    Scene scene = new Scene(root, 100, 100);

    private Label lblMessage;
    private Label lblTask;
    private Label lblDescription;
    private Button btnContinue;
    private Button btnBack;
    private ImageView imgInstruction;
    private int instructionIndexCounter;

    private StepGuideController controllerOne;
    private StepGuideController controllerTwo;
    private RecipeManager recipeManager;

    private Vector<Instruction> tempInstructions;

    @Start
    public void start(Stage stage){
        btnContinue = new Button("Continue");
        btnContinue.setId("btnContinue");
        btnContinue.setOnAction(actionEvent -> controllerOne.onActionBtnContinue(actionEvent));

        btnBack = new Button("Step back");
        btnBack.setId("btnBack");
        btnBack.setOnAction(actionEvent -> controllerOne.onActionBtnBack(actionEvent));

        VBox vbox = new VBox(5);
        vbox.getChildren().addAll(btnContinue, btnBack);

        stage.setScene(scene);
        root.getChildren().add(vbox);

        stage.show();
    }

    @BeforeEach
    public void setup() {

        lblMessage = spy(new Label());
        lblTask = spy(new Label());
        lblDescription = spy(new Label());

        Recipe recipe1 = new Recipe("test1", "test1 desc", MealType.FISH, Duration.ofMinutes(5), Duration.ofMinutes(30));
        Recipe recipe2 = new Recipe("test2", "test2 desc", MealType.PORK, Duration.ofMinutes(7), Duration.ofMinutes(5));

        recipe1.setCookInstructions(new Vector<>());
        recipe2.addInstruction(new Instruction("testTask-2-1", "testDescription-2-1", "testPhoto-2-2"));
        recipe2.addInstruction(new Instruction("testTask-2-2", "testDescription-2-2", "testPhoto-2-2"));

        recipeManager = new RecipeManager();

        recipeManager.addRecipe(recipe1);
        recipeManager.addRecipe(recipe2);

        controllerOne = spy(new StepGuideController(lblMessage, lblTask, lblDescription, imgInstruction, btnContinue, btnBack, recipe2));
        controllerTwo = spy(new StepGuideController(lblMessage, lblTask, lblDescription, imgInstruction, btnContinue, btnBack, recipe1));

        RecipeManager.setInstance(recipeManager);

    }

    @Test
    void testRecipeWithNoInstructions() {
        controllerTwo.initViewFields();

        tempInstructions = recipeManager.getRecipes().elementAt(0).getCookInstructions();

        assert tempInstructions.size() == 0;
        assert btnBack.isVisible() == false;
        assert btnContinue.isVisible() == false;
        assert lblMessage.getText().contains("No instructions found! - Go back and add one.");
    }

    @Test
    void testRecipeWithInstructions() {
        controllerOne.initViewFields();


        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerOne.displayInstruction();
        } catch (Exception ex) {}

        tempInstructions = recipeManager.getRecipes().elementAt(1).getCookInstructions();

        assert tempInstructions.size() > 0;
        assert btnBack.isVisible() == true;
        assert btnContinue.isVisible() == true;
        assert lblTask.getText().equals(tempInstructions.elementAt(0).getTask());
        assert lblDescription.getText().equals(tempInstructions.elementAt(0).getDescription());
    }

    @Test
    void testLowerLimit(FxRobot robot) {
        robot.clickOn("#btnBack");

        assert lblMessage.getText().contains("Wrong direction ;)");
    }

    @Test
    void testContinueToNextInstruction(FxRobot robot) {
        controllerOne.initViewFields();

        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerOne.displayInstruction();
        } catch (Exception ex) {}

        tempInstructions = recipeManager.getRecipes().elementAt(1).getCookInstructions();

        assert tempInstructions.size() > 0;
        assert btnBack.isVisible() == true;
        assert btnContinue.isVisible() == true;
        assert lblTask.getText().equals(tempInstructions.elementAt(0).getTask());
        assert lblDescription.getText().equals(tempInstructions.elementAt(0).getDescription());

        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerOne.onActionBtnContinue(null);
        } catch (Exception ex) {}

        assert lblTask.getText().equals(tempInstructions.elementAt(1).getTask());
        assert lblDescription.getText().equals(tempInstructions.elementAt(1).getDescription());
    }

    @Test
    void testStepBackToPrevInstruction(FxRobot robot) {
        controllerOne.initViewFields();

        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerOne.displayInstruction();
        } catch (Exception ex) {}

        tempInstructions = recipeManager.getRecipes().elementAt(1).getCookInstructions();

        assert tempInstructions.size() > 0;
        assert btnBack.isVisible() == true;
        assert btnContinue.isVisible() == true;
        assert lblTask.getText().equals(tempInstructions.elementAt(0).getTask());
        assert lblDescription.getText().equals(tempInstructions.elementAt(0).getDescription());

        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerOne.onActionBtnContinue(null);
        } catch (Exception ex) {}

        assert lblTask.getText().equals(tempInstructions.elementAt(1).getTask());
        assert lblDescription.getText().equals(tempInstructions.elementAt(1).getDescription());

        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerOne.onActionBtnBack(null);
        } catch (Exception ex) {}

        assert lblTask.getText().equals(tempInstructions.elementAt(0).getTask());
        assert lblDescription.getText().equals(tempInstructions.elementAt(0).getDescription());
    }

    @Test
    void testUpperLimit() {
        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerOne.onActionBtnContinue(null);
        } catch (Exception ex) {}

        try { // catching the fileNotFoundException for the pictures, not occur in the real application
            controllerOne.onActionBtnContinue(null);
        } catch (Exception ex) {}

        assert lblMessage.getText().contains("Reach last. Enjoy your meal!");
    }

}
