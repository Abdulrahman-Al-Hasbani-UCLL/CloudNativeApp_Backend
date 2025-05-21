package be.group16.forum.dto;

public class PostInput {
    private String body;
    private String threadId;
    private String userId;
    private String parentId;

    public PostInput() {}

    public PostInput(String body, String threadId, String userId, String parentId) {
        this.body = body;
        this.threadId = threadId;
        this.userId = userId;
        this.parentId = parentId;
    }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getThreadId() { return threadId; }
    public void setThreadId(String threadId) { this.threadId = threadId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }
}