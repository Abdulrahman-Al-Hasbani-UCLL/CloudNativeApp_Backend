package be.group16.forum.dto;

import java.util.List;

public class ThreadInput {
    private String title;
    private String body;
    private String userId;
    private List<String> tagIds;

    public ThreadInput() {}

    public ThreadInput(String title, String body, String userId, List<String> tagIds) {
        this.title = title;
        this.body = body;
        this.userId = userId;
        this.tagIds = tagIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public List<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<String> tagIds) {
        this.tagIds = tagIds;
    }
}