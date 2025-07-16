package com.bict.auth_api.dtos.auth.requeset;

import lombok.Data;

@Data
public class RequestSignupDto {
    private String username;
    private String password;
    private String role;
}
