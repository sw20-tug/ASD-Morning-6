package app.controllers;

import app.models.Recipe;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RecipeListViewCell extends ListCell<Recipe> {
    @FXML
    private HBox recipeListCellHbox;

    @FXML
    private ImageView imgRecipe;

    @FXML
    private Label lblName;

    @FXML
    private Label lblType;

    @FXML
    private Label lblPrepTime;

    @FXML
    private Label lblCookTime;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Recipe recipe, boolean empty) {
        super.updateItem(recipe, empty);

        if(empty || recipe == null) {

            //do nothing

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("../../resources/views/listCellRecipe.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            lblName.setText(recipe.getName());
            lblType.setText(recipe.getType().toString());
            lblPrepTime.setText(String.valueOf(recipe.getPrepTime().toMinutes()));
            lblCookTime.setText(String.valueOf(recipe.getCookTime().toMinutes()));

            String tmp = "appdata/images/" + recipe.getPhotos().firstElement();
            System.out.println(tmp);

            try {
                FileInputStream input = new FileInputStream(tmp);
                Image tmpRecipeImage = new Image(input);
                imgRecipe.setImage(tmpRecipeImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            setText(null);
            setGraphic(recipeListCellHbox);
        }

    }
}
