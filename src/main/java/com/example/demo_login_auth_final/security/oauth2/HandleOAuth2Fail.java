package com.example.demo_login_auth_final.security.oauth2;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo_login_auth_final.utils.CookieUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import static com.example.demo_login_auth_final.security.oauth2.OAuth2Cookie.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
@RequiredArgsConstructor
public class HandleOAuth2Fail extends SimpleUrlAuthenticationFailureHandler {
  private final OAuth2Cookie oAuth2Cookie;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    String targetUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
        .map(Cookie::getValue)
        .orElse(("/"));

    targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
        .queryParam("error", exception.getLocalizedMessage())
        .build().toUriString();

    oAuth2Cookie.removeAuthorizationRequestCookies(request, response);

    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }
}
