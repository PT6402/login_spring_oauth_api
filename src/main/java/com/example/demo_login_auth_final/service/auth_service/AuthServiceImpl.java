package com.example.demo_login_auth_final.service.auth_service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthServiceImpl implements AuthService {

  @Override
  public ResponseEntity<?> getTokeID(String token) {
    String urlGGCheck = "https://www.googleapis.com/oauth2/v3/userinfo";

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();

    headers.add("Authorization", "Bearer " + token);
    HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

    ResponseEntity<String> response = restTemplate.exchange(urlGGCheck, HttpMethod.GET, entity, String.class);

    return response;
  }

}
