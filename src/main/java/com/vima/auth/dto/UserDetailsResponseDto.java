package com.vima.auth.dto;

import com.vima.auth.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class UserDetailsResponseDto {

    private Long id;

    private String email;

    private String password;

    private Role role;

    private int penalties;
}
