package app.models;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class GlobalConstants
{
    public static final String APPDATA_FOLDER_PATH = "./appdata/";
    public static final String RECIPEIMAGE_FOLDER_PATH = APPDATA_FOLDER_PATH + "images/recipeimages/";
    public static final String APPIMAGE_FOLDER_PATH = APPDATA_FOLDER_PATH + "images/appimages/";
    public static final String DUMMY_IMAGE_PATH = APPIMAGE_FOLDER_PATH + "dummy.png";
    public static final String LOGO_IMAGE_PATH = APPIMAGE_FOLDER_PATH + "Logo.png";
    public static final String FAV_IMAGE_PATH = APPIMAGE_FOLDER_PATH + "star.png";
    public static final String DATAFILE_PATH = APPDATA_FOLDER_PATH + "cookdata.bin";

    private static Map<String, Image> IMAGES = new HashMap<>();

    public static Image getPhotoFromPath(String path){
        if(IMAGES.containsKey(path)){
            return IMAGES.get(path);
        }else
        {
            FileInputStream input;
            Image img = null;
            try {
                input = new FileInputStream(path);

                img = new Image(input,400, 300,false, false);
                IMAGES.put(path , img);
                input.close();
            } catch (FileNotFoundException e) {

                if(IMAGES.containsKey(DUMMY_IMAGE_PATH))
                {
                    img = IMAGES.get(DUMMY_IMAGE_PATH);
                }
                else
                {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return img;
        }

    }

    public static void removeImageFromMap(String key) {
        File f = new File(key);
        f.delete();

        IMAGES.remove(key);
    }

}
