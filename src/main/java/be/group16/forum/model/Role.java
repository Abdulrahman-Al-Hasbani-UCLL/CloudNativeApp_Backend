package be.group16.forum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Field("name")
    @NotNull
    @Size(max = 100)    
    private String name;

    @Field("description")
    @Size(max = 500)
    private String description;

    @Field("color")
    @Size(max = 7) //Voor Hex kleur
    private String color;

    // @ElementCollection
    // @CollectionTable(name = "role_extended_data", joinColumns = @JoinColumn(name = "role_id"))
    // @Column(name = "data")
    @Field("extendedData")
    private Map<String, Object> extendedData = new HashMap<>();

    @Field("createdAt")
    @NotNull
    private String createdAt;

    @Field("updatedAt")
    @NotNull
    private String updatedAt;

    public Role() {
    }

    public Role(String name, String description, String color, String createdAt, String updatedAt) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
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

    public Map<String, Object> getExtendedData() {
        return extendedData;
    }

    public void setExtendedData(Map<String, Object> extendedData) {
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