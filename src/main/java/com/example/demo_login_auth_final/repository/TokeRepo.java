package com.example.demo_login_auth_final.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo_login_auth_final.model.Token;

@Repository
public interface TokeRepo extends JpaRepository<Token, Integer> {

}
