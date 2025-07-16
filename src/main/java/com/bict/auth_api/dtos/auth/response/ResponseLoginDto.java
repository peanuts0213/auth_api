package com.bict.auth_api.dtos.auth.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseLoginDto {
  private String token;
}
