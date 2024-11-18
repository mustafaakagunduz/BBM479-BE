// src/main/java/com/sms/hrsam/dto/RoleUpdateRequest.java
package com.sms.hrsam.dto;

public class RoleUpdateRequest {
    private String roleName;

    // Constructor
    public RoleUpdateRequest() {}

    public RoleUpdateRequest(String roleName) {
        this.roleName = roleName;
    }

    // Getter ve Setter
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}