package app;

import app.enums.MealType;
import app.models.Instruction;
import app.models.Recipe;
import app.models.RecipeManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.mockito.internal.matchers.Null;

import java.io.*;
import java.time.Duration;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../resources/views/main.fxml"));
        primaryStage.setTitle("COOK");
        primaryStage.setScene(new Scene(root, 1440, 810));
        primaryStage.show();
    }


    public static void main(String[] args) {
        //Recipe
        Recipe r1 = new Recipe("Toast", "two toast slices, ham and cheese", MealType.PORK, Duration.ofMinutes(2), Duration.ofMinutes(3));
        r1.addPhoto("toast.png");
        r1.addPhoto("mealpic_1_2.png");

        Instruction i1 = new Instruction("Schinken auflegen", "Lege den Schinken auf! LOS!", "cookinstruction_1_1");
        Instruction i2 = new Instruction("Toast toasten", "Toaster vorheizen, und rein damit.", "cookinstruction_1_2");
        r1.addInstruction(i1);
        r1.addInstruction(i2);


        Recipe r2 = new Recipe("Fischstäbchen", "4 Iglo Pkg. Fischstäbchen", MealType.FISH, Duration.ofMinutes(2), Duration.ofMinutes(15));
        r2.setFavourite(true);
        r2.addPhoto("toast.png");
        r2.addPhoto("mealpic_2_2.jpg");


        Recipe r3 = new Recipe("Humusaufstrich", "500g Kompost, 2 TL Öl", MealType.VEGAN, Duration.ofMinutes(20000), Duration.ofMinutes(20));
        r3.addPhoto("food.png");

        Instruction i3 = new Instruction("Komposter entleeren", "Mistgabel holen und loslegen!", "cookinstruction_3_1");
        r3.addInstruction(i3);

        RecipeManager.getInstance().addRecipe(r1);
        RecipeManager.getInstance().addRecipe(r2);
        RecipeManager.getInstance().addRecipe(r3);

        try {

            printData();

            FileOutputStream fos = new FileOutputStream(new File("appdata/cookdata.bin"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(RecipeManager.getInstance());

            fos.close();
            oos.close();

            FileInputStream fis = new FileInputStream(new File("appdata/cookdata.bin"));
            ObjectInputStream ois = new ObjectInputStream(fis);

            RecipeManager.setInstance(((RecipeManager) ois.readObject()));

            printData();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        launch(args);
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
