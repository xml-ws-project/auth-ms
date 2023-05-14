package com.vima.auth.dto;

import com.vima.auth.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class RegistrationResponseDTO {

        String firstName;
        String lastName;
        String email;
        String username;
        String password;
        Role role ;
        String phoneNumber;
}
