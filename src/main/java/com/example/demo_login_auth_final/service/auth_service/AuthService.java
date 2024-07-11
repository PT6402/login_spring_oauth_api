package com.example.demo_login_auth_final.service.auth_service;

import org.springframework.http.ResponseEntity;

public interface AuthService {
  ResponseEntity<?> getTokeID(String token);
}
