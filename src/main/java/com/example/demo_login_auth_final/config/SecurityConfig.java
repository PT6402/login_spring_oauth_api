package com.example.demo_login_auth_final.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo_login_auth_final.security.AuthFilter;
import com.example.demo_login_auth_final.security.CustomUserDetailsService;
import com.example.demo_login_auth_final.security.oauth2.CustomOauth2Service;
import com.example.demo_login_auth_final.security.oauth2.HandleOAuth2Fail;
import com.example.demo_login_auth_final.security.oauth2.HandleOAuth2Success;
import com.example.demo_login_auth_final.security.oauth2.OAuth2Cookie;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@RequiredArgsConstructor
@SuppressWarnings("removal")
public class SecurityConfig {
  private final AppProperties appProperties;
  private final CustomUserDetailsService customUserDetailsService;
  private final CustomOauth2Service customOAuth2UserService;
  private final HandleOAuth2Success handleOAuth2Success;
  private final HandleOAuth2Fail handleOAuth2Fail;
  private final AuthenticationProvider authenticationProvider;
  private final AuthFilter authFilter;
  private final PasswordEncoder passwordEncoder;
  private final OAuth2Cookie oAuth2Cookie;

  private static final String[] LIST_NO_AUTH = {
      "/api/v1/authentication/**",
      "/auth/**",
      "/oauth2/**",
      "/login/**"
  };

  @Bean
  public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
        .userDetailsService(customUserDetailsService)
        .passwordEncoder(passwordEncoder)
        .and()
        .build();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(o -> o.configurationSource(corsConfiguration()))
        .authorizeHttpRequests(req -> req
            .requestMatchers(LIST_NO_AUTH)
            .permitAll()
            .anyRequest()
            .authenticated())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .formLogin(c -> c.disable())
        .oauth2Login(oauth2 -> oauth2
            .authorizationEndpoint()
            .baseUri("/oauth2/authorize")
            .authorizationRequestRepository(oAuth2Cookie)
            .and()
            .redirectionEndpoint()
            .baseUri("/oauth2/callback/*")
            .and()
            .userInfoEndpoint()
            .userService(customOAuth2UserService)
            .and()
            .successHandler(handleOAuth2Success)
            .failureHandler(
                handleOAuth2Fail)
            .loginProcessingUrl("/login"))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(logout -> logout
            // .addLogoutHandler(logoutHandler)
            .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfiguration() {

    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(appProperties.getCors().getAllowed_origins());
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(Long.valueOf(appProperties.getCors().getMax_age_secs()));
    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",
        configuration);
    return urlBasedCorsConfigurationSource;
  }
}
