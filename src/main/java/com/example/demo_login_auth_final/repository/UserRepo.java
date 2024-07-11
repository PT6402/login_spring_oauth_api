package com.example.demo_login_auth_final.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo_login_auth_final.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
  Optional<User> findByEmail(String email);
}
