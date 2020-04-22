package test;

import app.controllers.AddRecipeController;
import app.enums.MealType;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;

import java.time.Duration;

import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
class AddRecipeControllerTest {

    StackPane root = new StackPane();
    Scene scene = new Scene(root, 100, 100);

    private Button saveBtn;
    private Button cancelBtn;
    private RecipeManager recipeManager;

    private Label lblMessage;
    private TextField txtName;
    private TextField txtDescription;
    private Spinner<MealType> spinType;
    private Spinner<Integer> spinPrepTime;
    private Spinner<Integer> spinCookTime;
    private ToggleButton toggleFavourite;
    private ToggleButton toggleGuideEnabled;

    private AddRecipeController controller;

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

        //I know this also happens initMealTypeSpinner ...
        ObservableList<MealType> mealTypes = FXCollections.observableArrayList(MealType.FISH, MealType.PORK, MealType.VEGAN, MealType.BEEF, MealType.CHICKEN, MealType.VEGETARIAN);
        SpinnerValueFactory<MealType> mealTypeValueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<MealType>(mealTypes);

        spinType = spy(new Spinner<MealType>());
        spinType.setValueFactory(mealTypeValueFactory);

        SpinnerValueFactory<Integer> spinnerIntegerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,300, 1);

        spinPrepTime = spy(new Spinner<Integer>());
        spinPrepTime.setValueFactory(spinnerIntegerValueFactory);
        spinCookTime = spy(new Spinner<Integer>());
        spinCookTime.setValueFactory(spinnerIntegerValueFactory);

        toggleFavourite = spy(new ToggleButton());
        toggleGuideEnabled = spy(new ToggleButton());

        controller = spy(new AddRecipeController(lblMessage, txtName, txtDescription, spinType, spinPrepTime, spinCookTime, toggleFavourite, toggleGuideEnabled, saveBtn, cancelBtn, null, null, null, null, null));

        Mockito.doAnswer((i)-> null).when(controller).closeAddWindow();

        recipeManager = new RecipeManager();
        RecipeManager.setInstance(recipeManager);
    }

    @Test
    void testOnActionBtnSaveHappyPath(FxRobot robot) {
        txtName.setText("Pasta");
        txtDescription.setText("Italian Dish");
        spinPrepTime.getValueFactory().setValue(10);
        spinCookTime.getValueFactory().setValue(15);
        spinType.getValueFactory().setValue(MealType.PORK);

        robot.clickOn("#saveBtn");

        assert recipeManager.getRecipes().size() == 1;
        assert lblMessage.getText().contains("SUCCESS");

    }

    @Test
    void testNoDescription(FxRobot robot) {
        txtName.setText("Pasta");
        txtDescription.setText("");
        spinPrepTime.getValueFactory().setValue(10);
        spinCookTime.getValueFactory().setValue(15);
        spinType.getValueFactory().setValue(MealType.PORK);

        robot.clickOn("#saveBtn");

        assert recipeManager.getRecipes().size() == 0;
        assert lblMessage.getText().contains("FAILURE");
    }

    @Test
    void testNoRecipeName(FxRobot robot) {
        txtName.setText("");
        txtDescription.setText("Italian Dish");
        spinPrepTime.getValueFactory().setValue(10);
        spinCookTime.getValueFactory().setValue(15);
        spinType.getValueFactory().setValue(MealType.PORK);

        robot.clickOn("#saveBtn");

        assert recipeManager.getRecipes().size() == 0;
        assert lblMessage.getText().contains("FAILURE");
    }

    @Test
    void onActionbtnCancel(FxRobot robot) {
        robot.clickOn("#cancelBtn");

        FxAssert.verifyThat(cancelBtn, LabeledMatchers.hasText("Cancel"));
    }
}