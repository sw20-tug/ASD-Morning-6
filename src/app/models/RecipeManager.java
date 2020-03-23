package app.models;

import java.io.Serializable;
import java.util.Vector;

public class RecipeManager implements Serializable {
    private static RecipeManager instance;

    private Vector<Recipe> recipes;

    private static int id = 1;

    public RecipeManager() {
        this.recipes = new Vector<>();
    }

    public static RecipeManager getInstance() {
        if (instance == null)
            instance = new RecipeManager();

        return instance;
    }

    public static void setInstance(RecipeManager rm) {
        instance = rm;
    }

    public Vector<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Vector<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void addRecipe(Recipe r) {
        r.setId(id);
        this.recipes.add(r);
        id++;
    }
}
