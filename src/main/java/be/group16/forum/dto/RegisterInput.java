package be.group16.forum.dto;

import java.util.Map;

public class RegisterInput {
    private String username;
    private String email;
    private String displayName;
    private String password;
    private Boolean emailVerified;
    private String[] roles;
    private Map<String, Object> extendedData;

    public void addRole(String newRole) {
        if (roles == null) {
            roles = new String[] { newRole };
        } else {
            String[] newRoles = new String[roles.length + 1];
            System.arraycopy(roles, 0, newRoles, 0, roles.length);
            newRoles[roles.length] = newRole;
            roles = newRoles;
        }
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Boolean getEmailVerified() { return emailVerified; }
    public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }
    public String[] getRoles() { return roles; }
    public void setRoles(String[] roles) { this.roles = roles; }
    public Map<String, Object> getExtendedData() { return extendedData; }
    public void setExtendedData(Map<String, Object> extendedData) { this.extendedData = extendedData; }
    
}