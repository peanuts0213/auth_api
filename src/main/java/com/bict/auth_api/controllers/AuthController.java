package com.bict.auth_api.controllers;

import com.bict.auth_api.dtos.auth.requeset.RequestUpdateUserDto;
import com.bict.auth_api.dtos.auth.requeset.RequestSignupDto;
import com.bict.auth_api.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bict.auth_api.dtos.auth.requeset.RequestLoginDto;
import com.bict.auth_api.dtos.auth.response.ResponseLoginDto;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

  private final AuthService authService;

  @GetMapping("/create/authdata")
  public ResponseEntity<?> createAuthData() {
    try {
      authService.createAuth();
      return ResponseEntity.ok("계정 생성 성공");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PostMapping("/login")
  public ResponseEntity<ResponseLoginDto> login(@RequestBody RequestLoginDto dto) {
    System.out.println("start");
    String token = authService.login(dto);
    System.out.println("end");

    return ResponseEntity.ok(new ResponseLoginDto(token));
  }

  @PostMapping("/auth/signup")
  public ResponseEntity<String> signup(@RequestBody RequestSignupDto dto) {
    try {
      String message = authService.signup(dto);
      return ResponseEntity.ok(message);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러 발생");
    }
  }

  @DeleteMapping("/auth/deleteuser/{username}")
  public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String authHeader, @PathVariable String username) {
    try {
      String token = authHeader.substring(7);
      authService.deleteUser(token, username);
      return ResponseEntity.ok("삭제 완료 되었습니다.");
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @PatchMapping("/auth/update")
  public ResponseEntity<String> updateUser(@RequestHeader("Authorization") String authHeader, @RequestBody RequestUpdateUserDto dto) {
    try {
      String token = authHeader.substring(7);
      String message = authService.updateUser(token, dto);
      return ResponseEntity.ok(message);
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping("/auth/getuser")
  public ResponseEntity<?> getUser(@RequestHeader("Authorization") String authHeader) {
    try {
      String token = authHeader.substring(7);
      return ResponseEntity.ok(authService.getUser(token));
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping("/auth/getusers")
  public ResponseEntity<?> getUsers(@RequestHeader("Authorization") String authHeader) {
    try {
      String token = authHeader.substring(7);
      return ResponseEntity.ok(authService.getUsers(token));
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
  }

//  @PostMapping("/verify")
//  public ResponseEntity<?> verify(@RequestHeader("Authorization") String authHeader) {
//    try {
//      String token = authHeader.substring(7);
//      return ResponseEntity.ok(authService.verify(token));
//    } catch (SecurityException e) {
//      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//    }
//  }
}