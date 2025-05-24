package com.exercie.exercies.security;

import com.exercie.exercies.exception.ForbiddenHandler;
import com.exercie.exercies.exception.NotAuthenticationEntryPoint;
import com.exercie.exercies.service.UserRoleService;
import com.exercie.exercies.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    private final UsernameEmailPasswordProvider usernameEmailPasswordProvider;
    private final ForbiddenHandler forbiddenHandler;
    private final NotAuthenticationEntryPoint notAuthenticationEntryPoint;

    @Autowired
    public WebSecurityConfig(UsernameEmailPasswordProvider usernameEmailPasswordProvider, ForbiddenHandler forbiddenHandler, NotAuthenticationEntryPoint notAuthenticationEntryPoint) {
        this.usernameEmailPasswordProvider = usernameEmailPasswordProvider;
        this.forbiddenHandler = forbiddenHandler;
        this.notAuthenticationEntryPoint = notAuthenticationEntryPoint;
    }


    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(usernameEmailPasswordProvider);
    }

//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authManagerBuilder =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//        authManagerBuilder.authenticationProvider(usernameEmailPasswordProvider);
//        return authManagerBuilder.build();
//    }

    @Bean
    public AuthJwtTokenFilter authJwtTokenFilter(){
        return new AuthJwtTokenFilter();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedHandler(forbiddenHandler)
                        .authenticationEntryPoint(notAuthenticationEntryPoint)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->  auth
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/auth/register").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );


        httpSecurity.addFilterBefore(authJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.authenticationProvider(usernameEmailPasswordProvider);
        return httpSecurity.build();
    }
}
