package test;

import app.controllers.EditRecipeController;
import app.enums.MealType;
import app.models.Recipe;
import app.models.RecipeManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

import java.time.Duration;
import java.util.Vector;

import static org.mockito.Mockito.spy;

@ExtendWith(ApplicationExtension.class)
public class EditRecipeControllerTest {


    StackPane root = new StackPane();
    Scene scene = new Scene(root, 100, 100);

    private Button saveBtn;
    private Button cancelBtn;

    private Label lblMessage;
    private TextField txtName;
    private TextField txtDescription;
    private Spinner<MealType> spinType;
    private Spinner<Integer> spinPrepTime;
    private Spinner<Integer> spinCookTime;

    private ToggleButton toggleFavourite;
    private ToggleButton toggleGuideEnabled;

    private Recipe inRecipe;
    private EditRecipeController controller;

    private RecipeManager recipeManager;


    @Start
    public void start(Stage stage){
        saveBtn = new Button("Save");
        saveBtn.setId("saveBtn");
        saveBtn.setOnAction(actionEvent -> controller.onActionBtnSave(actionEvent));

        cancelBtn = new Button("Cancel");
        cancelBtn.setId("cancelBtn");

        VBox vbox = new VBox(5);
        vbox.getChildren().addAll(saveBtn, cancelBtn);

        stage.setScene(scene);
        root.getChildren().add(vbox);

        stage.show();
    }

    @BeforeEach
    public void setup() {

        lblMessage = spy(new Label());
        txtName = spy(new TextField());
        txtDescription = spy(new TextField());

        ObservableList<MealType> mealTypes = FXCollections.observableArrayList(MealType.FISH, MealType.PORK, MealType.VEGAN, MealType.BEEF, MealType.CHICKEN, MealType.VEGETARIAN);
        SpinnerValueFactory<MealType> mealTypeValueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<MealType>(mealTypes);

        spinType = spy(new Spinner<MealType>());
        spinType.setValueFactory(mealTypeValueFactory);

        SpinnerValueFactory<Integer> spinnerIntegerValueFactoryPrep = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,300, 1);
        SpinnerValueFactory<Integer> spinnerIntegerValueFactoryCook = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,300, 1);

        spinPrepTime = spy(new Spinner<Integer>());
        spinPrepTime.setValueFactory(spinnerIntegerValueFactoryPrep);

        spinCookTime = spy(new Spinner<Integer>());
        spinCookTime.setValueFactory(spinnerIntegerValueFactoryCook);

        toggleFavourite = spy(new ToggleButton());
        toggleGuideEnabled = spy(new ToggleButton());

        Recipe recipe1 = new Recipe("test1", "test1 desc", MealType.FISH, Duration.ofMinutes(5), Duration.ofMinutes(30));
        Recipe recipe2 = new Recipe("test2", "test2 desc", MealType.PORK, Duration.ofMinutes(7), Duration.ofMinutes(5));
        Recipe recipe3 = new Recipe("test3", "test3 desc", MealType.BEEF, Duration.ofMinutes(15), Duration.ofMinutes(15));
        Recipe recipe4 = new Recipe("test4", "test4 desc", MealType.VEGAN, Duration.ofMinutes(4), Duration.ofMinutes(14));
        Recipe recipe5 = new Recipe("test5", "test5 desc", MealType.VEGETARIAN, Duration.ofMinutes(30), Duration.ofMinutes(6));
        Recipe recipe6 = new Recipe("test6", "test6 desc", MealType.FISH, Duration.ofMinutes(20), Duration.ofMinutes(10));
        Recipe recipe7 = new Recipe("test7", "test7 desc", MealType.PORK, Duration.ofMinutes(7), Duration.ofMinutes(18));
        Recipe recipe8 = new Recipe("test8", "test8 desc", MealType.FISH, Duration.ofMinutes(6), Duration.ofMinutes(20));
        Recipe recipe9 = new Recipe("test9", "test9 desc", MealType.FISH, Duration.ofMinutes(70), Duration.ofMinutes(5));
        Recipe recipe10 = new Recipe("test10", "test10 desc", MealType.FISH, Duration.ofMinutes(5), Duration.ofMinutes(70));
        Recipe recipe11 = new Recipe("test11", "test11 desc", MealType.FISH, Duration.ofMinutes(20), Duration.ofMinutes(11));

        recipeManager = new RecipeManager();

        recipeManager.addRecipe(recipe1);
        recipeManager.addRecipe(recipe2);
        recipeManager.addRecipe(recipe3);
        recipeManager.addRecipe(recipe4);
        recipeManager.addRecipe(recipe5);
        recipeManager.addRecipe(recipe6);
        recipeManager.addRecipe(recipe7);
        recipeManager.addRecipe(recipe8);
        recipeManager.addRecipe(recipe9);
        recipeManager.addRecipe(recipe10);
        recipeManager.addRecipe(recipe11);

        inRecipe = recipe9;
        Vector<String> editedImages = new Vector<>();
        controller = spy(new EditRecipeController(lblMessage, txtName, txtDescription, spinType, spinPrepTime, spinCookTime, toggleFavourite, toggleGuideEnabled, saveBtn, cancelBtn, inRecipe, editedImages));

        Mockito.doAnswer((i)-> null).when(controller).closeEditWindow();

        RecipeManager.setInstance(recipeManager);

    }

    @Test
    void testOnActionBtnSaveHappyPath(FxRobot robot) {

        txtName.setText("Pasta");
        txtDescription.setText("Pasta-desc");
        spinPrepTime.getValueFactory().setValue(10);
        spinCookTime.getValueFactory().setValue(15);
        spinType.getValueFactory().setValue(MealType.PORK);

        robot.clickOn("#saveBtn");

        Recipe new_recipe = new Recipe("Pasta", "Pasta-desc", MealType.PORK, Duration.ofMinutes(10), Duration.ofMinutes(15));
        assert inRecipe.equals(new_recipe);
        assert recipeManager.getRecipes().size() == 11;
        assert lblMessage.getText().contains("SUCCESS");
    }

    @Test
    void testOnActionBtnSaveEmptyName(FxRobot robot) {

        txtName.setText("");
        txtDescription.setText("Pasta");
        spinPrepTime.getValueFactory().setValue(10);
        spinCookTime.getValueFactory().setValue(15);
        spinType.getValueFactory().setValue(MealType.PORK);

        robot.clickOn("#saveBtn");

        assert recipeManager.getRecipes().size() == 11;
        assert lblMessage.getText().contains("FAILURE");

    }

    @Test
    void testOnActionBtnSaveEmptyDescription(FxRobot robot) {
        txtName.setText("Pasta");
        txtDescription.setText("");
        spinPrepTime.getValueFactory().setValue(10);
        spinCookTime.getValueFactory().setValue(15);
        spinType.getValueFactory().setValue(MealType.PORK);

        robot.clickOn("#saveBtn");

        assert recipeManager.getRecipes().size() == 11;
        assert lblMessage.getText().contains("FAILURE");
    }
}