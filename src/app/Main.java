package app;

import app.models.GlobalConstants;
import app.models.Instruction;
import app.models.Recipe;
import app.models.RecipeManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../resources/views/main.fxml"));
        primaryStage.setTitle("COOK");
        primaryStage.setScene(new Scene(root, 1440, 810));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(GlobalConstants.getPhotoFromPath(GlobalConstants.LOGO_IMAGE_PATH));
        primaryStage.show();
    }


    public static void main(String[] args) {

        try {
            System.out.println("Loading data...");
            FileInputStream fis = new FileInputStream(new File(GlobalConstants.DATAFILE_PATH));
            ObjectInputStream ois = new ObjectInputStream(fis);

            RecipeManager.setInstance(((RecipeManager) ois.readObject()));
            fis.close();
            ois.close();

        } catch (FileNotFoundException e) {
            System.out.println("No binary datafile found.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        launch(args);

        try {
            System.out.println("Saving data...");
            FileOutputStream fos = new FileOutputStream(new File(GlobalConstants.DATAFILE_PATH));
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(RecipeManager.getInstance());

            fos.close();
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printData() {
        for (Recipe r : RecipeManager.getInstance().getRecipes()) {
            System.out.println("====== [" + r.getName() + "] ======");
            System.out.println(r);

            for (Instruction i : r.getCookInstructions()) {
                System.out.println(i);
            }

            System.out.println("=======================");
        }
    }
}
