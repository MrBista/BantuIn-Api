package com.exercie.exercies.security;

import com.exercie.exercies.service.UserRoleService;
import com.exercie.exercies.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthenticationProviderConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final UserRoleService userRoleService;

    @Autowired
    public AuthenticationProviderConfig(
            PasswordEncoder passwordEncoder,
            UserService userService,
            UserRoleService userRoleService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @Bean
    public UsernameEmailPasswordProvider usernameEmailPasswordProvider() {
        return new UsernameEmailPasswordProvider(passwordEncoder, userService, userRoleService);
    }
}
