package com.bict.auth_api.dtos.auth.requeset;

import lombok.Data;

@Data
public class RequestLoginDto {
  private String username;
  private String password;
}
