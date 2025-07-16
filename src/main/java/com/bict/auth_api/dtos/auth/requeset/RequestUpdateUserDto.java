package com.bict.auth_api.dtos.auth.requeset;

import lombok.Data;

@Data
public class RequestUpdateUserDto {
    private String password;
    private String role;
}