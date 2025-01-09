package Shared_classes;

import java.io.Serializable;

public class Book implements Serializable {
    private String name;
    private String topic;
    private String description;
    private int copies;
    private int availableCopies;

    public Book(String name, String topic, String description, int copies, int availableCopies) {
        this.name = name;
        this.topic = topic;
        this.description = description;
        this.copies = copies;
        this.availableCopies = availableCopies;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    @Override
    public String toString() {
        return "Book [name=" + name + ", topic=" + topic + ", description=" + description + 
               ", copies=" + copies + ", availableCopies=" + availableCopies + "]";
    }
}
