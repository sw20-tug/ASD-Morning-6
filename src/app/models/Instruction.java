package app.models;

import java.io.Serializable;

public class Instruction implements Serializable {
    private String task;
    private String description;
    private String photo;

    public Instruction() {}

    public Instruction(String task, String description, String photo) {
        this.task = task;
        this.description = description;
        this.photo = photo;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "[" + task + "]";
    }
}
