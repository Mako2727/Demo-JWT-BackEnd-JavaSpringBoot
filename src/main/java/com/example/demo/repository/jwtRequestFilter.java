package com.example.demo.repository;

import com.example.demo.security.JwtUtil;
import org.springframework.security.core.userdetails.UserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class jwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
       private UserDetailsService userDetailsService;

    public void JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

public jwtRequestFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
}



    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        
        if (path.equals("/api/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("authorization");

        String jwt = null;
        String username = null;

        System.out.println("Liste des headers reçus :");
        request.getHeaderNames().asIterator()
       .forEachRemaining(name -> System.out.println(name + " : " + request.getHeader(name)));

          System.out.println("avant controle token : ");
          System.out.println("mon authHeader :"+authHeader);
        if (authHeader != null ) {
              
            jwt = authHeader;
            jwt=jwt.replace("Bearer ","");
             System.out.println("Token reçu : '" + jwt + "'");
            username = jwtUtil.extractUsername(jwt);
              System.out.println("mon username : "+username);
        }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authToken);
    System.out.println(" Authentifié avec succès : " + username);
}
 System.out.println("request... : " + request);
        System.out.println("response...: " + response);
        filterChain.doFilter(request, response);
       
    }
}
