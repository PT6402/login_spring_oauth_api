package com.example.demo_login_auth_final.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo_login_auth_final.repository.UserRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepo userRepo;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    try {

      var user = userRepo.findByEmail(email)
          .orElseThrow(() -> new UsernameNotFoundException("user not found by email: " + email));
      return UserPrincipal.createUser(user);
    } catch (Exception e) {
      log.error(e.getMessage());
      return null;
    }
  }

  @Transactional
  public UserDetails loadUserById(int userId) {
    try {
      var user = userRepo.findById(userId)
          .orElseThrow(() -> new Exception("user not found by id: " + userId));
      return UserPrincipal.createUser(user);
    } catch (Exception e) {
      log.error(e.getMessage());
      return null;
    }
  }

}
