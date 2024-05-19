package com.example.notemanegersystem.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDTO {
    @JsonProperty("email")
    private String email;
    private String password;
    @JsonProperty("retype_password")
    private String retypePassword;
    @JsonProperty("full_name")
    private String fullName;
}
