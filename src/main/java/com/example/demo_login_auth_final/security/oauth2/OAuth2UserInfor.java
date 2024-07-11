package com.example.demo_login_auth_final.security.oauth2;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class OAuth2UserInfor {
  protected Map<String, Object> attributes;

  public abstract String getId();

  public abstract String getEmail();

  public abstract String getName();

}
