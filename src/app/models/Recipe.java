package app.models;

import app.enums.MealType;

import java.io.Serializable;
import java.time.Duration;
import java.util.Vector;

public class Recipe implements Serializable {
    private int id;
    private String name;
    private String description;
    private MealType type;
    private Duration prepTime;
    private Duration cookTime;
    private boolean isFavourite;
    private boolean isGuideEnabled;

    private Vector<Instruction> cookInstructions;
    private Vector<String> photos;

    public Recipe() {}

    public Recipe(String name, String description, MealType type, Duration prepTime, Duration cookTime) {
        this.id = -1;
        this.name = name;
        this.description = description;
        this.type = type;
        this.prepTime = prepTime;
        this.cookTime = cookTime;

        this.cookInstructions = new Vector<>();
        this.photos = new Vector<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MealType getType() {
        return type;
    }

    public void setType(MealType type) {
        this.type = type;
    }

    public Duration getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Duration prepTime) {
        this.prepTime = prepTime;
    }

    public Duration getCookTime() {
        return cookTime;
    }

    public void setCookTime(Duration cookTime) {
        this.cookTime = cookTime;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public boolean isGuideEnabled() {
        return isGuideEnabled;
    }

    public void setGuideEnabled(boolean guideEnabled) {
        isGuideEnabled = guideEnabled;
    }

    public Vector<Instruction> getCookInstructions() {
        return cookInstructions;
    }

    public void setCookInstructions(Vector<Instruction> cookInstructions) {
        this.cookInstructions = cookInstructions;
    }

    public Vector<String> getPhotos() {
        return photos;
    }

    public void setPhotos(Vector<String> photos) {
        this.photos = photos;
    }

    public void addPhoto(String filename) {
        this.photos.add(filename);
    }

    public void addInstruction(Instruction i) {
        this.cookInstructions.add(i);
    }

    @Override
    public String toString() {
        return "[" + id + ", " + name + "]";
    }
}
