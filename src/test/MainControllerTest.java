package test;

import app.controllers.MainController;
import app.enums.MealType;
import app.models.Recipe;
import app.models.RecipeManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationExtension;

import java.time.Duration;

import static org.mockito.Mockito.spy;

@ExtendWith(ApplicationExtension.class)
public class MainControllerTest {

    private ComboBox<MealType> cmbMealType;
    //max_time
    private Slider sliderPrepTime;
    //max_time
    private Slider sliderCookTime;
    private ListView listAllRecipes;
    private ListView listFavRecipes;
    private Button btnUndoFilter;
    private Label lblMessage;

    private Label lblName;
    private Label lblDescription;
    private Label lblMealType;
    private Label lblPrepTime;
    private Label lblCookingTime;

    private AnchorPane apDetailView;

    private ImageView imgFav;

    private MainController controller;

    static RecipeManager recipeManager;

    @BeforeEach
    public void setup() {
        ObservableList<MealType> mealTypes = FXCollections.observableArrayList(MealType.FISH, MealType.PORK, MealType.VEGAN, MealType.BEEF, MealType.CHICKEN, MealType.VEGETARIAN);

        cmbMealType = spy(new ComboBox<>());
        cmbMealType.setItems(mealTypes);

        sliderPrepTime = spy(new Slider(0, 200, 0));
        sliderCookTime = spy(new Slider(0, 200, 0));
        listAllRecipes = spy(new ListView());
        listFavRecipes = spy(new ListView());
        btnUndoFilter = spy(new Button());
        lblMessage = spy(new Label());
        lblName = spy(new Label());
        lblDescription = spy(new Label());
        lblMealType = spy(new Label());
        lblPrepTime = spy(new Label());
        lblCookingTime = spy(new Label());
        apDetailView = spy(new AnchorPane());

        lblName = spy(new Label());
        lblDescription = spy(new Label());
        lblMealType = spy(new Label());
        lblPrepTime = spy(new Label());
        lblCookingTime = spy(new Label());

        imgFav = spy(new ImageView());

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

        RecipeManager.setInstance(recipeManager);

        controller = spy(new MainController(lblMessage, cmbMealType, sliderPrepTime, sliderCookTime, listAllRecipes,listFavRecipes, btnUndoFilter, apDetailView, lblName, lblDescription, lblMealType, lblPrepTime, lblCookingTime, imgFav));

        Mockito.doAnswer((i)-> null).when(controller).loadImage();
    }

    @Test
    void testFilterNoneFinding() {

        cmbMealType.getSelectionModel().select(MealType.FISH);
        sliderPrepTime.setValue(100);
        sliderCookTime.setValue(100);

        controller.filterRecipes();

        assert listAllRecipes.getItems().size() == 0;

    }

    @Test
    void testFilterAllFishFinding() {

        cmbMealType.getSelectionModel().select(MealType.FISH);
        sliderPrepTime.setValue(0);
        sliderCookTime.setValue(0);

        controller.filterRecipes();

        assert listAllRecipes.getItems().size() == 6;

    }

    @Test
    void testFilterFindBasic() {

        cmbMealType.getSelectionModel().select(MealType.FISH);
        sliderPrepTime.setValue(20);
        sliderCookTime.setValue(5);

        controller.filterRecipes();

        assert listAllRecipes.getItems().size() == 3;

    }

    @Test
    void testFilterFindBasic2() {

        cmbMealType.getSelectionModel().select(MealType.FISH);
        sliderPrepTime.setValue(20);
        sliderCookTime.setValue(11);

        controller.filterRecipes();

        assert listAllRecipes.getItems().size() == 1;

    }

    @Test
    void testDisplayCurrentRecipe () {
        MainController.currentRecipe = new Recipe("test11", "test11 desc", MealType.FISH, Duration.ofMinutes(20), Duration.ofMinutes(11));
        controller.displayCurrentRecipe();

        assert lblName.getText().equals("test11");
        assert lblMealType.getText().equals(MealType.FISH.toString());
        assert lblPrepTime.getText().equals(20 + " min.") ;
        assert lblCookingTime.getText().equals(11 + " min.");
        assert lblDescription.getText().equals("test11 desc");
    }

    @Test
    void testDisplayCurrentRecipeWithNoCurrentRecipe () {
        MainController.currentRecipe = null;
        controller.displayCurrentRecipe();
    }
}