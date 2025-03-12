package be.group16.forum.model;

public class ErrorResponse {
    private String name;

    private String message;

    public ErrorResponse() {
        this.name = "General Error";
        this.message = "This is a default message";
    };

    public ErrorResponse(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getErrorName() {
        return name;
    };

    public String getErrorMessage() {
        return message;
    };

    public void setErrorName(String newName) {
        this.name = newName;
    };

    public void setErrorMessage(String newMessage) {
        this.message = newMessage;
    };

}
