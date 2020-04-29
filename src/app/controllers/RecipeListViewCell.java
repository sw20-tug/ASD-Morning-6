package app.controllers;

import app.models.GlobalConstants;
import app.models.Recipe;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

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

        if (empty || recipe == null) {
            //Don't show anything
            setGraphic(null);
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

            imgRecipe.setImage(GlobalConstants.getPhotoFromPath(recipe.getPhotos().size() != 0 ? recipe.getPhotos().firstElement() : GlobalConstants.DUMMY_IMAGE_PATH));


            setText(null);
            setGraphic(recipeListCellHbox);
        }

    }
}
