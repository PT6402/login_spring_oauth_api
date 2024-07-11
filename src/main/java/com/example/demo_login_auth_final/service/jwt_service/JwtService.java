package com.example.demo_login_auth_final.service.jwt_service;

import org.springframework.security.core.Authentication;

public interface JwtService {
  String generateAccessToken(Authentication authentication);

  String generateRefreshToken(Authentication authentication);

  Integer getUserIdByToken(String token);
}
