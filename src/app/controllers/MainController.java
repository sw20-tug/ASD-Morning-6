package app.controllers;

import app.models.Recipe;
import app.models.RecipeManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Label lblMessage;

    @FXML
    private Button btnAddRecipe;

    @FXML
    private ListView listAllRecipes;

    @FXML
    private ListView listFavRecipes;

    private ObservableList<Recipe> recipeAllObservableList;
    private ObservableList<Recipe> recipeFavObservableList;

    private FXMLLoader mLLoader;

    public MainController() {

    }

    @FXML
    private void initialize() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initViewElements();
        fillListViews();
    }

    private void initViewElements() {
        setInfoMessage("Welcome to COOK! - " + RecipeManager.getInstance().getRecipes().size() + " Recipes loaded");
    }

    private void fillListViews() {
        recipeAllObservableList = FXCollections.<Recipe>observableArrayList(RecipeManager.getInstance().getRecipes());
        listAllRecipes.setItems(recipeAllObservableList);
        listAllRecipes.setCellFactory(recipeListView -> new RecipeListViewCell());

        recipeFavObservableList = FXCollections.<Recipe>observableArrayList(RecipeManager.getInstance().getFavRecipes());
        listFavRecipes.setItems(recipeFavObservableList);
        listFavRecipes.setCellFactory(recipeListView -> new RecipeListViewCell());

        setSuccessMessage("Recipes loaded!");
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

    public void onActionbtnAddRecipe(ActionEvent actionEvent) {
        setInfoMessage("ADD window is open!");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../resources/views/addRecipe.fxml"));
        Parent root1 = null;
        try {
            AddRecipeController addRecipeController = new AddRecipeController(1800);

            fxmlLoader.setController(addRecipeController);
            root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("ADD RECIPE");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
