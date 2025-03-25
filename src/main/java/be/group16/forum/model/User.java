package be.group16.forum.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Schema(description = "User entity")
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // This is a safe long aah id
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    private String displayName;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private Boolean emailVerified = false;

    private String image;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    @Column(length = 1000)
    private String bio;

    private String signature;

    private String url;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "VARCHAR")
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
}
