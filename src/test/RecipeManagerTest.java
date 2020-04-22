package test;

import app.models.Recipe;
import app.models.RecipeManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Vector;

import static app.enums.MealType.*;

public class RecipeManagerTest {

    static final RecipeManager recipeManager = new RecipeManager();

    @BeforeAll
    static void setUpTestEnvironment(){

        Recipe recipe1 = new Recipe("test1", "test-desc1", CHICKEN, Duration.ofMinutes(15), Duration.ofMinutes(60));
        recipe1.setFavourite(true);
        Recipe recipe2 = new Recipe("test2", "test-desc2", FISH, Duration.ofMinutes(20), Duration.ofMinutes(45));
        recipe2.setFavourite(false);
        Recipe recipe3 = new Recipe("test3", "test-desc3", PORK, Duration.ofMinutes(35), Duration.ofMinutes(70));
        recipe3.setFavourite(false);
        Recipe recipe4 = new Recipe("test4", "test-desc4", VEGAN, Duration.ofMinutes(22), Duration.ofMinutes(10));
        recipe4.setFavourite(true);
        Recipe recipe5 = new Recipe("test5", "test-desc5", VEGETARIAN, Duration.ofMinutes(17), Duration.ofMinutes(30));
        recipe5.setFavourite(true);

        Vector<Recipe> testRecipes = new Vector<>();
        testRecipes.add(recipe1);
        testRecipes.add(recipe2);
        testRecipes.add(recipe3);
        testRecipes.add(recipe4);
        testRecipes.add(recipe5);

        recipeManager.setRecipes(testRecipes);
    }

    @Test
    void getFavRecipesTest(){
        Vector<Recipe> testResultRecipes = recipeManager.getFavRecipes();

        assert(testResultRecipes.size() == 3);
        for(Recipe recipe : testResultRecipes){
            assert (recipe.isFavourite());
        }
    }

    @Test
    void addRecipeTest(){
        int recipeCount = recipeManager.getRecipes().size();

        Recipe addTestRecipe = new Recipe("addTest", "addTest-desc5", VEGETARIAN, Duration.ofMinutes(19), Duration.ofMinutes(31));

        recipeManager.addRecipe(addTestRecipe);

        recipeCount++;
        assert (recipeManager.getRecipes().size() == recipeCount);
    }

    @Test
    void addRecipeTestInputNull(){
        int recipeCount = recipeManager.getRecipes().size();

        Assertions.assertThrows(IllegalArgumentException.class, () -> recipeManager.addRecipe(null));

        assert (recipeManager.getRecipes().size() == recipeCount);
    }
}
