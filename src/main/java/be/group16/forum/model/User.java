package be.group16.forum.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Document(collection = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Field("username")
    @NotNull
    @Size(max = 50)
    private String username;

    @Field("email")
    @NotNull
    @Email
    private String email;

    private String displayName;

    @JsonIgnore
    @NotNull
    @Size(min = 8)
    private String password;

    private Boolean emailVerified = false;

    private String image;

    // Pas aan zodat roles geen relatie meer heeft
    @Field(name = "roles")
    private Set<String> roles = new HashSet<>();

    @Size(max = 1000)
    private String bio;

    private String signature;

    private String url;

    @Field("reputation")
    private int reputation = 0;

    // @JdbcTypeCode(SqlTypes.JSON)
    // @Column(columnDefinition = "VARCHAR")
    private Map<String, Object> extendedData = new HashMap<>();

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.displayName = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        }

        public String[] getRoles() {
        return roles.toArray(new String[0]);
        }

        public void setRoles(String[] roleArray) {
        this.roles.clear();
        if (roleArray != null) {
            for (String role : roleArray) {
                this.roles.add(role);
            }
        }
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getExtendedData() {
        return extendedData;
    }

    public void setExtendedData(Map<String, Object> extendedData) {
        this.extendedData = extendedData;
    }

    public void addRole(String role) {
        this.roles.add(role);
    }

    public void removeRole(String role) {
        this.roles.remove(role);
    }

    public void addExtendedData(String key, Object value) {
        this.extendedData.put(key, value);
    }

    public Object getExtendedDataValue(String key) {
        return this.extendedData.get(key);
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }
}
