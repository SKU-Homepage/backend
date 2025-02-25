package org.example.skuhomepage.global.config;

import org.example.skuhomepage.global.security.CustomUserDetailService;
import org.example.skuhomepage.global.security.JwtAuthFilter;
import org.example.skuhomepage.global.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

  private final CustomUserDetailService customUserDetailService;
  private final JwtUtil jwtUtil;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable());
    http.cors(Customizer.withDefaults());

    http.formLogin(form -> form.disable());
    http.httpBasic(AbstractHttpConfigurer::disable);

    // JwtAuthFilter 추가
    http.addFilterBefore(
        new JwtAuthFilter(customUserDetailService, jwtUtil),
        UsernamePasswordAuthenticationFilter.class);

    http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/api/**",
                        "/login/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated());

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {

    return new BCryptPasswordEncoder();
  }
}
