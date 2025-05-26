package com.exercie.exercies.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AuthJwtTokenFilter extends OncePerRequestFilter {
    Logger logger = LoggerFactory.getLogger(AuthJwtTokenFilter.class);

    @Autowired
    private JwtTokenService jwtService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }


        String token = header.substring(7);

        if (jwtService.validateToken(token)) {
            // Get username from token
            Claims claim = jwtService.getClaim(token);

            String username = claim.get("username", String.class);

            // Get roles from token
            String[] roles = jwtService.getRolesFromToken(token);

            // Convert roles to authorities
            List<SimpleGrantedAuthority> authorities = Arrays.stream(roles)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            logger.info("authorities {}",authorities);
            logger.info("roles {}", roles);
            // Create authenticated token
            UsernameEmailPasswordAuthentication authentication =
                    new UsernameEmailPasswordAuthentication(username, null, authorities);

            // Set authentication to context
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);

    }

}
