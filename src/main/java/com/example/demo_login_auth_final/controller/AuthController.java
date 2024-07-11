package com.example.demo_login_auth_final.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo_login_auth_final.service.auth_service.AuthService;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/authentication")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping
  public ResponseEntity<?> getTokeID(@PathParam("token_id") String tokenID) {
    return authService.getTokeID(tokenID);
  }
}
