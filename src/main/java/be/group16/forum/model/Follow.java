package be.group16.forum.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Document(collection = "follow")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Field("followerId")
    private String followerId;

    @Field("followingId")
    private String followingId;

    @Field("extendedData")
    private Object extendedData = new Object();

    @Field("createdAt")
    private String createdAt;

    @Field("updatedAt")
    private String updatedAt;

    public Follow() {
    }

    public Follow(String id, String followerId, String followingId, Object extendedData,
            String createdAt, String updatedAt) {
        this.id = id;
        this.followerId = followerId;
        this.followingId = followingId;
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

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public String getFollowingId() {
        return followingId;
    }

    public void setFollowingId(String followingId) {
        this.followingId = followingId;
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
