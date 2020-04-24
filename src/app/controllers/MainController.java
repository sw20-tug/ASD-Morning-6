package app.controllers;

import app.models.Instruction;
import app.models.Recipe;
import app.models.RecipeManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;

public class MainController implements Initializable {
    @FXML
    private Label lblMessage;

    @FXML
    private Button btnAddRecipe;

    @FXML
    private ListView listAllRecipes;

    @FXML
    private ListView listFavRecipes;

    @FXML
    private TextField searchAll;

    @FXML
    private TextField searchFav;



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
        setListViewContextMenu(listAllRecipes);
        setListViewContextMenu(listFavRecipes);
        searchListener();
    }

    private void initViewElements() {
        setInfoMessage("Welcome to COOK! - " + RecipeManager.getInstance().getRecipes().size() + " Recipes loaded");
    }

    private void searchListener() {

        FilteredList<Recipe> allFiltered;

        allFiltered = new FilteredList<Recipe>(FXCollections.observableArrayList(RecipeManager.getInstance().getRecipes()));

        searchAll.textProperty().addListener((observable, oldValue, newValue) -> {
            allFiltered.setPredicate(recipe -> {

                if (newValue.isEmpty())
                    return true;

                // search every attribute
                if (recipe.getName().toLowerCase().contains(newValue.toLowerCase()))
                    return true;
                else if (recipe.getCookTime().toString().contains(newValue.toLowerCase()))
                    return true;
                else if (recipe.getPrepTime().toString().contains(newValue.toLowerCase()))
                    return true;
                else if (recipe.getDescription().toLowerCase().contains(newValue.toLowerCase()))
                    return true;
                else if (recipe.getType().toString().toLowerCase().contains(newValue.toLowerCase()))
                    return true;

                /* // search for instruction
                Vector<Instruction> instructions = recipe.getCookInstructions();

                // check every instruction for search term
                for (int i = 0; i < recipe.getCookInstructions().size(); i++)
                {
                    if (instructions.get(i).getDescription().contains(newValue.toLowerCase()))
                        return true;
                    else if (instructions.get(i).getTask().contains(newValue.toLowerCase()))
                        return true;
                }
                */

                return false;
            });
        });

        listAllRecipes.setItems(new SortedList<Recipe>(allFiltered));

        FilteredList<Recipe> favFiltered;

        favFiltered = new FilteredList<Recipe>(FXCollections.observableArrayList(RecipeManager.getInstance().getFavRecipes()));

        searchFav.textProperty().addListener((observable, oldValue, newValue) -> {
            favFiltered.setPredicate(recipe -> {

                // search every attribute
                if (recipe.getName().toLowerCase().contains(newValue.toLowerCase()))
                    return true;
                else if (recipe.getCookTime().toString().toLowerCase().contains(newValue.toLowerCase()))
                    return true;
                else if (recipe.getPrepTime().toString().toLowerCase().contains(newValue.toLowerCase()))
                    return true;
                else if (recipe.getDescription().toLowerCase().contains(newValue.toLowerCase()))
                    return true;
                else if (recipe.getType().toString().toLowerCase().contains(newValue.toLowerCase()))
                    return true;

                /* // search for instruction
                Vector<Instruction> instructions = recipe.getCookInstructions();

                // check every instruction for search term
                for (int i = 0; i < recipe.getCookInstructions().size(); i++)
                {
                    if (instructions.get(i).getDescription().contains(newValue.toLowerCase()))
                        return true;
                    else if (instructions.get(i).getTask().contains(newValue.toLowerCase()))
                        return true;
                }
                */
                return false;
            });
        });

        listFavRecipes.setItems(new SortedList<Recipe>(favFiltered));

    }



    private void fillListViews() {
        //recipeAllObservableList = FXCollections.<Recipe>observableArrayList(RecipeManager.getInstance().getRecipes());
        //listAllRecipes.setItems(recipeAllObservableList);
        listAllRecipes.setCellFactory(recipeListView -> new RecipeListViewCell());

        //recipeFavObservableList = FXCollections.<Recipe>observableArrayList(RecipeManager.getInstance().getFavRecipes());
        //listFavRecipes.setItems(recipeFavObservableList);
        listFavRecipes.setCellFactory(recipeListView -> new RecipeListViewCell());

        searchListener();
    }

    private void setListViewContextMenu(ListView lv){

        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit");
        MenuItem deleteItem = new MenuItem("Delete");

        editItem.setOnAction((event) -> {
            openEditView((Recipe) lv.getSelectionModel().getSelectedItem());

        });

        deleteItem.setOnAction((event) -> {
            Recipe r = (Recipe) lv.getSelectionModel().getSelectedItem();
            for(String s : r.getPhotos())
            {
                File f = new File(s);
                f.deleteOnExit();
            }
            RecipeManager.getInstance().deleteRecipe(r);
            fillListViews();
        });
        contextMenu.getItems().addAll(editItem,deleteItem);

        lv.setContextMenu(contextMenu);

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

    private void updateView() {
        fillListViews();
        setInfoMessage("Data updated");
    }

    public void onActionbtnAddRecipe(ActionEvent actionEvent) {
        setInfoMessage("ADD window is open!");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../resources/views/editRecipe.fxml"));
        Parent rootAddView = null;
        try {
            AddRecipeController addRecipeController = new AddRecipeController();

            fxmlLoader.setController(addRecipeController);
            rootAddView = (Parent) fxmlLoader.load();
            Stage stageAddView = new Stage();
            stageAddView.initModality(Modality.APPLICATION_MODAL);
            stageAddView.setTitle("ADD RECIPE");
            stageAddView.setResizable(false);
            stageAddView.setScene(new Scene(rootAddView));

            stageAddView.setOnHidden((WindowEvent event1) -> {
                updateView();
            });

            stageAddView.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void openEditView(Recipe r) {
        setInfoMessage("EDIT window is open!");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../resources/views/editRecipe.fxml"));
        Parent rootEditView = null;
        try {
            EditRecipeController editRecipeController = new EditRecipeController(r);

            fxmlLoader.setController(editRecipeController);
            rootEditView = (Parent) fxmlLoader.load();
            Stage stageEditView = new Stage();
            stageEditView.initModality(Modality.APPLICATION_MODAL);
            stageEditView.setTitle("EDIT RECIPE");
            stageEditView.setResizable(false);
            stageEditView.setScene(new Scene(rootEditView));

            stageEditView.setOnHidden((WindowEvent event1) -> {
                updateView();
            });

            stageEditView.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
