package com.example.demo_login_auth_final.service.jwt_service;

import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.demo_login_auth_final.config.AppProperties;
import com.example.demo_login_auth_final.security.UserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("deprecation")
public class JwtServiceImpl implements JwtService {
  private final AppProperties appProperties;

  @Override
  public String generateAccessToken(Authentication authentication) {
    Date expirDate = new Date(System.currentTimeMillis() + appProperties.getAuth().getAccess_token_expired());
    return createToken(authentication, expirDate);
  }

  @Override
  public String generateRefreshToken(Authentication authentication) {
    Date expirDate = new Date(System.currentTimeMillis() + appProperties.getAuth().getRefresh_token_expired());
    return createToken(authentication, expirDate);
  }

  @Override
  public Integer getUserIdByToken(String token) {
    var userId = parseToken(token, Claims::getSubject);
    if (userId == null) {
      return null;
    }
    return Integer.valueOf(userId);
  }

  private String createToken(Authentication authentication, Date expirDate) {
    UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
    return Jwts.builder()
        .subject(String.valueOf(user.getId()))
        .expiration(expirDate)
        .issuedAt(new Date())
        .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getToken_secret())
        .compact();
  }

  private <T> T parseToken(String token, Function<Claims, T> getClaim) {
    try {
      Claims claims = Jwts
          .parser()
          .setSigningKey(appProperties.getAuth().getToken_secret())
          .build()
          .parseClaimsJws(token)
          .getBody();
      return getClaim.apply(claims);

    } catch (SignatureException ex) {
      log.error("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      log.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      log.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty.");
    }
    return null;
  }

}
