package be.group16.forum.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Document(collection = "upvote")
public class Upvote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Field("userId")
    private String userId;

    @Field("threadId")
    private String threadId;

    @Field("postId")
    private String postId;
    
    @Field("downvote")
    private boolean downvote;

    @Field("extendedData")
    private Object extendedData = new Object();

    @Field("createdAt")
    private String createdAt;

    @Field("updatedAt")
    private String updatedAt;

    public Upvote() {
    }

    public Upvote(String id, String userId, String threadId, String postId, boolean downvote, Object extendedData,
            String createdAt, String updatedAt) {
        this.id = id;
        this.userId = userId;
        this.threadId = threadId;
        this.postId = postId;
        this.downvote = downvote;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public boolean isDownvote() {
        return downvote;
    }

    public void setDownvote(boolean downvote) {
        this.downvote = downvote;
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
