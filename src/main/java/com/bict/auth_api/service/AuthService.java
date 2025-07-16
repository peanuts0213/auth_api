package com.bict.auth_api.service;

import com.bict.auth_api.components.JwtProvider;
import com.bict.auth_api.dtos.auth.requeset.RequestLoginDto;
import com.bict.auth_api.dtos.auth.requeset.RequestUpdateUserDto;
import com.bict.auth_api.dtos.auth.requeset.RequestSignupDto;
import com.bict.auth_api.entities.UserEntity;
import com.bict.auth_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void createAuth() {
        UserEntity user = new UserEntity();
        UserEntity user2 = new UserEntity();
        user.setUsername("pomit");
        user.setPassword(passwordEncoder.encode("pomit!9400"));
        user.setRole("ADMIN");
        user2.setUsername("admin");
        user2.setPassword(passwordEncoder.encode("bict4486"));
        user2.setRole("ADMIN");
        userRepository.save(user);
        userRepository.save(user2);
    }

    public String login(RequestLoginDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        System.out.println("힝");
        UserEntity user = userRepository.findByUsername(dto.getUsername());
        return jwtProvider.generateToken(dto.getUsername(), user.getRole());
    }

    public String signup(RequestSignupDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());

        userRepository.save(user);
        return "회원가입 성공";
    }

    public void deleteUser(String token, String username) {
        if (!jwtProvider.validateToken(token)) {
            throw new SecurityException("Invalid token");
        }

        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        userRepository.delete(user);
    }

    public String updateUser(String token, RequestUpdateUserDto dto) {
        if (!jwtProvider.validateToken(token)) {
            throw new SecurityException("Invalid token");
        }

        String username = jwtProvider.getUsername(token);
        UserEntity user = userRepository.getUserIdByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getRole() != null && !dto.getRole().isEmpty()) {
            user.setRole(dto.getRole());
        }

        userRepository.save(user);
        return "사용자 정보 수정 완료";
    }

    public Map<String, Object> getUser(String token) {
        if (!jwtProvider.validateToken(token)) {
            throw new SecurityException("Invalid token");
        }

        String username = jwtProvider.getUsername(token);
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        return Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "role", user.getRole()
        );
    }

    public Object getUsers(String token) {
        if (!jwtProvider.validateToken(token)) {
            throw new SecurityException("Invalid token");
        }

        return userRepository.findAll();
    }

    public Map<String, String> verify(String token) {
        if (!jwtProvider.validateToken(token)) {
            throw new SecurityException("Invalid token");
        }

        String username = jwtProvider.getUsername(token);
        return Map.of("username", username);
    }
}
