package app.controllers;

import app.models.Recipe;
import app.models.RecipeManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    @FXML
    private ImageView imageViewDetails;

    @FXML
    private ImageView imgFav;

    @FXML
    private Button btnNextImage;

    @FXML
    private Button btnPrevImage;

    @FXML
    private Label lblName;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblMealType;

    @FXML
    private Label lblPrepTime;

    @FXML
    private Label lblCookingTime;

    @FXML
    private AnchorPane apDetailView;




    private ObservableList<Recipe> recipeAllObservableList;
    private ObservableList<Recipe> recipeFavObservableList;
    private int currentImage = 0;
    public static Recipe currentRecipe;
    private FXMLLoader mLLoader;
    private Image defaultRecipeImage;

    public MainController() {

    }

    @FXML
    private void initialize() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initViewElements();
            fillListViews();
            updateDetailView();
            setListViewContextMenu(listAllRecipes);
            setListViewContextMenu(listFavRecipes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initViewElements() throws IOException {
        FileInputStream input = new FileInputStream("appdata/images/dummy.png");
        defaultRecipeImage = new Image(input, imageViewDetails.getFitWidth(), imageViewDetails.getFitHeight(), false, true);

        input = new FileInputStream("appdata/images/star.png");
        Image imageFav = new Image(input, imageViewDetails.getFitWidth(), imageViewDetails.getFitHeight(), false, true);
        imgFav.setImage(imageFav);

        setInfoMessage("Welcome to COOK! - " + RecipeManager.getInstance().getRecipes().size() + " Recipes loaded");

        input.close();
    }

    private void fillListViews() {
        recipeAllObservableList = FXCollections.<Recipe>observableArrayList(RecipeManager.getInstance().getRecipes());
        listAllRecipes.setItems(recipeAllObservableList);
        listAllRecipes.setCellFactory(recipeListView -> new RecipeListViewCell());

        recipeFavObservableList = FXCollections.<Recipe>observableArrayList(RecipeManager.getInstance().getFavRecipes());
        listFavRecipes.setItems(recipeFavObservableList);
        listFavRecipes.setCellFactory(recipeListView -> new RecipeListViewCell());
    }

    private void updateDetailView() {
        if(RecipeManager.getInstance().getRecipes().size() > 0) {
            if(listAllRecipes.getSelectionModel().getSelectedItem() != null)
            {
                currentRecipe = (Recipe) listAllRecipes.getSelectionModel().getSelectedItem();
            }else {
                currentRecipe = RecipeManager.getInstance().getRecipes().firstElement();
            }
            displayCurrentRecipe();
        }
        else
        {
            apDetailView.setVisible(false);
        }
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
            updateDetailView();
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

            stageAddView.showAndWait();
            displayCurrentRecipe();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onActionBtnPrevImage(ActionEvent actionEvent) {
        if(currentRecipe.getPhotos().size() > 1) {
            if(currentImage <= 0)
            {
                currentImage = currentRecipe.getPhotos().size()-1;
            }
            else
            {
                currentImage--;
            }
            loadImage();
        }
    }

    public void onActionBtnNextImage(ActionEvent actionEvent) {

        if(currentRecipe.getPhotos().size() > 1) {
            if(currentImage >= currentRecipe.getPhotos().size()-1)
            {
                currentImage = 0;
            }
            else
            {
                currentImage++;
            }
            loadImage();
        }
    }

    private void loadImage() {
        if (currentRecipe.getPhotos().size() > 0) {
            try {
                System.out.println("loading Image: " + currentRecipe.getPhotos().elementAt(currentImage));
                FileInputStream input = new FileInputStream(currentRecipe.getPhotos().elementAt(currentImage));
                Image img = new Image(input, imageViewDetails.getFitWidth(), imageViewDetails.getFitHeight(), false, true);
                imageViewDetails.setImage(img);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            imageViewDetails.setImage(defaultRecipeImage);
        }
    }

    private void displayCurrentRecipe(){
        apDetailView.setVisible(true);
        lblName.setText(currentRecipe.getName());
        lblMealType.setText(currentRecipe.getType().toString());
        lblPrepTime.setText(currentRecipe.getPrepTime().toMinutes() + " min.");
        lblCookingTime.setText(currentRecipe.getCookTime().toMinutes() + " min.");
        lblDescription.setText(currentRecipe.getDescription());
        currentImage = 0;

        if(currentRecipe.isFavourite()){
            imgFav.setVisible(true);
        }else{
            imgFav.setVisible(false);
        }

        loadImage();
        //TODO display instructions??

    }

    public void lvItemClicked(MouseEvent mouseEvent) {
        currentRecipe = (Recipe) listAllRecipes.getSelectionModel().getSelectedItem();
        if(currentRecipe != null){
            displayCurrentRecipe();
        }
    }

    public void lvItemFavClicked(MouseEvent mouseEvent) {
        currentRecipe = (Recipe) listFavRecipes.getSelectionModel().getSelectedItem();
        if(currentRecipe != null){
            displayCurrentRecipe();
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

            stageEditView.showAndWait();
            displayCurrentRecipe();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
