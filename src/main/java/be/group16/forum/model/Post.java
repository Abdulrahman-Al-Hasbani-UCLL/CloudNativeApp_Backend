package be.group16.forum.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "posts")
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Field("body")
    private String body;

    @Field("userId")
    private String userId;

    @Field("threadId")
    private String threadId;

    @Field("parentId")
    private String parentId;

    @Field("bestAnswer")
    private boolean bestAnswer;

    @Field("likes")
    private Set<Like> likes = new HashSet<>();

    @Field("upvotes")
    private Set<Upvote> upvotes = new HashSet<>();

    @Field("extendedData")
    private Map<String, Object> extendedData = new HashMap<>();

    @Field("instanceId")
    private String instanceId;

    @Field("createdAt")
    private String createdAt;

    @Field("updatedAt")
    private String updatedAt;

    public Post() {
    }

    public Post(String id, String body, String userId, String threadId, String parentId,
                boolean bestAnswer, Set<Like> likes, Set<Upvote> upvotes,
                Map<String, Object> extendedData, String instanceId,
                String createdAt, String updatedAt) {
        this.id = id;
        this.body = body;
        this.userId = userId;
        this.threadId = threadId;
        this.parentId = parentId;
        this.bestAnswer = bestAnswer;
        this.likes = likes;
        this.upvotes = upvotes;
        this.extendedData = extendedData;
        this.instanceId = instanceId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isBestAnswer() {
        return bestAnswer;
    }

    public void setBestAnswer(boolean bestAnswer) {
        this.bestAnswer = bestAnswer;
    }

    public Set<Like> getLikes() {
        return likes;
    }

    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }

    public Set<Upvote> getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(Set<Upvote> upvotes) {
        this.upvotes = upvotes;
    }

    public Map<String, Object> getExtendedData() {
        return extendedData;
    }

    public void setExtendedData(Map<String, Object> extendedData) {
        this.extendedData = extendedData;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
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
