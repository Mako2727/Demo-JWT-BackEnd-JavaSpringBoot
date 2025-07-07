package com.example.demo.SpringSecurity;

import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.CustomUserDetailsServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {



  private final CustomUserDetailsServiceImpl customUserDetailsService;

  public SecurityConfig(CustomUserDetailsServiceImpl customUserDetailsService) {
    this.customUserDetailsService = customUserDetailsService;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, jwtRequestFilter jwtRequestFilter)
      throws Exception {

    http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/api/auth/**", "/uploads/**", "/swagger-ui/**", "/v3/api-docs/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .formLogin(AbstractHttpConfigurer::disable)
        .csrf(csrf -> csrf.disable())
        .authenticationProvider(authenticationProvider())
        .exceptionHandling(
            exception ->
                exception.authenticationEntryPoint(
                    (request, response, authException) -> {
                      response.setContentType("application/json");
                      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                      String message = authException.getMessage();
                      String json =
                          String.format("{ \"error\": \"%s\" }", message.replace("\"", "\\\""));
                      response.getOutputStream().println(json);
                    }));

    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public UserDetailsService userDetailsService(UserRepository userRepository) {
    return new CustomUserDetailsServiceImpl(userRepository);
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(customUserDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public jwtRequestFilter jwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
    jwtRequestFilter filter = new jwtRequestFilter(jwtUtil);
    filter.setUserDetailsService(userDetailsService);
    return filter;
  }
}
