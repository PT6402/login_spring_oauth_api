package com.example.demo_login_auth_final.security.oauth2;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo_login_auth_final.model.AuthProvider;
import com.example.demo_login_auth_final.model.Role;
import com.example.demo_login_auth_final.model.User;
import com.example.demo_login_auth_final.repository.UserRepo;
import com.example.demo_login_auth_final.security.UserPrincipal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOauth2Service extends DefaultOAuth2UserService {
  private final UserRepo userRepo;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    var oauth2_user = super.loadUser(userRequest);
    try {
      return handleGetInforUserByOAuth2(userRequest, oauth2_user);
    } catch (AuthenticationException authEx) {
      throw authEx;
    } catch (Exception e) {
      log.error("erro prcess oauth get user {}", e.getMessage());
      throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
    }
  }

  private OAuth2User handleGetInforUserByOAuth2(OAuth2UserRequest userquest, OAuth2User oauth2_user) {

    OAuth2UserInfor oAuth2UserInfo = new OAuth2GoogleUserInfor(oauth2_user.getAttributes());
    if (!StringUtils.hasLength(oAuth2UserInfo.getEmail())) {
      new Exception("can not load infor user by OAuth2 GOOGLE");
    }

    var userOptional = userRepo.findByEmail(oAuth2UserInfo.getEmail());
    User user;
    if (userOptional.isEmpty()) {
      user = userRepo.save(User.builder()
          .name(oAuth2UserInfo.getName())
          .email(oAuth2UserInfo.getEmail())
          .role(Role.user)
          .provider(AuthProvider.google)
          .build());
    } else {
      user = userOptional.get();
    }

    return UserPrincipal.createUser(user, oauth2_user.getAttributes());
  }

}
