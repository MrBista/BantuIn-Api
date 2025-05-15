package com.exercie.exercies.security;

import com.exercie.exercies.model.User;
import com.exercie.exercies.service.UserRoleService;
import com.exercie.exercies.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

public class UsernameEmailPasswordProvider implements AuthenticationProvider {
    private final Logger log = LoggerFactory.getLogger(UsernameEmailPasswordProvider.class);

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final UserRoleService userRoleService;

    @Autowired
    public UsernameEmailPasswordProvider(PasswordEncoder passwordEncoder, UserService userService, UserRoleService userRoleService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 1. cari dulu username
        // 2. cek match ga passwordnya
        //

        if (authentication instanceof UsernameEmailPasswordAuthentication){
            UsernameEmailPasswordAuthentication authenticationDetail = (UsernameEmailPasswordAuthentication) authentication;
            String identifier = authenticationDetail.getName();
            String password = authenticationDetail.getCredentials().toString();


            User user = userService.
                    findUserByIdentifier(identifier)
                    .orElseThrow(() -> new BadCredentialsException("username/password is wrong"));


            // nanti kalau ada tambahan cek user active

            // kalau passwordnya ga match maka throw exception
            if (!passwordEncoder.matches(password, user.getPassword())){
                log.info("password not match");
                log.info("password input {}", password);
                throw  new BadCredentialsException("username/password is wrong");
            }

            Set<GrantedAuthority> authorities =  userRoleService
                                                    .findRoleUserGrantedAuthority(user.getId());


            return new UsernameEmailPasswordAuthentication(user, user.getPassword(), authorities);



        }



        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernameEmailPasswordAuthentication.class.isAssignableFrom(authentication);
    }
}
