package be.group16.forum.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Document(collection = "threads")
public class Tag {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("color")
    private String color;

    @Field("threads")
    private Thread[] threads;

    @Field("extendedData")
    private Object extendedData = new Object();

    @Field("createdAt")
    private String createdAt;

    @Field("updatedAt")
    private String updatedAt;
    
    public Tag() {
    }

    public Tag(String id, String name, String description, String color, Thread[] threads, Object extendedData,
            String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.threads = threads;
        this.extendedData = extendedData;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Thread[] getThreads() {
        return threads;
    }

    public void setThreads(Thread[] threads) {
        this.threads = threads;
    }

    public Object getExtendedData() {
        return extendedData;
    }

    public void setExtendedData(Object extendedData) {
        this.extendedData = extendedData;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
