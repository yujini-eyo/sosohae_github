package com.myspring.eum.admin.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class AdminVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;   // member.id
    private String name;
    private String password;
    private String email;
    private String role;     // ADMIN / USER ...
    private String status;   // ACTIVE / INACTIVE ...
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // getters/setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
