package com.sms.hrsam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
    private Long id;
    private String name;
    private String description;
    private int userCount;
    private List<UserDTO> users;  // User'dan UserDTO'ya değişti

    // Partial constructor for specific use cases
    public CompanyDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}