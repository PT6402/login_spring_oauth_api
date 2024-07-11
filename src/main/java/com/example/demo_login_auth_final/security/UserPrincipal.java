package com.example.demo_login_auth_final.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.demo_login_auth_final.model.Role;
import com.example.demo_login_auth_final.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal implements OAuth2User, UserDetails {
  private Integer id;
  private String email;
  private String password;
  private Collection<? extends GrantedAuthority> authorities;
  private Map<String, Object> attributes;

  public static UserPrincipal createUser(User user) {
    List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(Role.user.toString()));

    return UserPrincipal.builder()
        .id(user.getId())
        .password(user.getPassword())
        .email(user.getEmail())
        .authorities(authorities)
        .build();
  }

  public static UserPrincipal createAdmin(User user) {
    List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(Role.admin.toString()));

    return UserPrincipal.builder()
        .id(user.getId())
        .password(user.getPassword())
        .email(user.getPassword())
        .authorities(authorities)
        .build();
  }

  public static UserPrincipal createUser(User user, Map<String, Object> attributes) {
    UserPrincipal userPrincipal = createUser(user);
    userPrincipal.setAttributes(attributes);
    return userPrincipal;
  }

  public static UserPrincipal createAdmin(User user, Map<String, Object> attributes) {
    UserPrincipal userPrincipal = createAdmin(user);
    userPrincipal.setAttributes(attributes);
    return userPrincipal;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public String getName() {
    return String.valueOf(id);
  }
}
