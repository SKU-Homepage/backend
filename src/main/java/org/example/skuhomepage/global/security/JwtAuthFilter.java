package org.example.skuhomepage.global.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
  private final CustomUserDetailService customUserDetailsService;
  private final JwtUtil jwtUtil;

  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // String authorizationHeader = request.getHeader("Authorization");
    String token = null;
    Cookie[] cookies = request.getCookies();
    log.info("Cookies are {}", cookies);

    if (cookies != null && cookies.length > 0) {
      for (Cookie cookie : cookies) {
        if ("token".equals(cookie.getName())) {
          token = cookie.getValue();
          break;
        }
      }
    }

    // JWT가 헤더에 있는 경우
    if (token != null && jwtUtil.validateToken(token)) {

      String userEmail = jwtUtil.getUserEmail(token);

      // 유저와 토큰 일치 시 userDetails 생성
      UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

      if (userDetails != null) {
        // UserDetsils, Password, Role -> 접근권한 인증 Token 생성
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        // 현재 Request의 Security Context에 접근권한 설정
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }
    }

    filterChain.doFilter(request, response); // 다음 필터로 넘기기
  }
}
