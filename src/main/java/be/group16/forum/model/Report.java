package be.group16.forum.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Document(collection = "Report")    
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Field("reportedId")
    private String reportedId;

    @Field("reporterId")
    private String reporterId;

    @Field("threadId")
    private String threadId;

    @Field("postId")
    private String postId;

    @Field("privateMessageId")
    private String privateMessageId;

    @Field("read")
    private boolean read;

    @Field("type")
    private String type;

    @Field("description")
    private String description;

    @Field("instanceId")
    private String instanceId;

    @Field("extendedData")
    private Object extendedData = new Object();

    public Report() {
    }

    public Report(String id, String reportedId, String reporterId, String threadId, String postId,
            String privateMessageId, boolean read, String type, String description, String instanceId,
            Object extendedData) {
        this.id = id;
        this.reportedId = reportedId;
        this.reporterId = reporterId;
        this.threadId = threadId;
        this.postId = postId;
        this.privateMessageId = privateMessageId;
        this.read = read;
        this.type = type;
        this.description = description;
        this.instanceId = instanceId;
        this.extendedData = extendedData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportedId() {
        return reportedId;
    }

    public void setReportedId(String reportedId) {
        this.reportedId = reportedId;
    }

    public String getReporterId() {
        return reporterId;
    }

    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
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

    public String getPrivateMessageId() {
        return privateMessageId;
    }

    public void setPrivateMessageId(String privateMessageId) {
        this.privateMessageId = privateMessageId;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Object getExtendedData() {
        return extendedData;
    }

    public void setExtendedData(Object extendedData) {
        this.extendedData = extendedData;
    }
}
