package com.employee.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter @Setter
@NoArgsConstructor
public class AuthResponse {
   
    private String token;
    //private String username;
    //private Set<String> roles;
}
