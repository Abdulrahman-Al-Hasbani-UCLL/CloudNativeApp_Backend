package be.group16.forum.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Document(collection = "threads")
public class Thread {
    /*{
  "id": "string",
  "title": "string",
  "slug": "string",
  "body": "string",
  "locked": true,
  "pinned": true,
  "user": {
    "id": "string",
    "username": "string",
    "avatar": "string"
  },
  "tags": [
    {
      "id": "string",
      "name": "string",
      "description": "string",
      "color": "string",
      "threads": [
        {}
      ],
      "extendedData": {}
    }
  ],
  "createdAt": "string",
  "updatedAt": "string"
} */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Field("title")
    private String title;

    @Field("slug")
    private String slug;

    @Field("body")
    private String body;

    @Field("locked")
    private boolean locked;

    @Field("pinned")
    private boolean pinned;
    
    @Field("user")
    private User user;

    @Field("tags")
    private Tag[] tags;

    @Field("createdAt")
    private String createdAt;

    @Field("updatedAt")
    private String updatedAt;

    @Field("extendedData")
    private Object extendedData = new Object();
    
    public Thread() {
    }

    public Thread(String id, String title, String slug, String body, boolean locked, boolean pinned,
            User user, Tag[] tags, String createdAt, String updatedAt) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.body = body;
        this.locked = locked;
        this.pinned = pinned;
        this.user = user;
        this.tags = tags;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tag[] getTags() {
        return tags;
    }

    public void setTags(Tag[] tags) {
        this.tags = tags;
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

    public Object getExtendedData() {
        return extendedData;
    }

    public void setExtendedData(Object extendedData) {
        this.extendedData = extendedData;
    }

    
}
