package com.exercie.exercies.security;

import com.exercie.exercies.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collection;

public class UsernameEmailPasswordAuthentication extends AbstractAuthenticationToken {
    private Object principal;
    private Object credentials;

    Logger log = LoggerFactory.getLogger(UsernameEmailPasswordProvider.class);

    public UsernameEmailPasswordAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        // untuk authrorize / ketika user sudah login
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public UsernameEmailPasswordAuthentication(Object principal, Object credentials){
        // untuk login / authenticate user
        super(AuthorityUtils.NO_AUTHORITIES);
        log.info("masuk sini dengan principle dan credential {}{}", principal, credentials);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }




    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public String getName() {
        Object var2 = this.getPrincipal();
        if (var2 instanceof User userDetails) {
            return userDetails.getUsername();
        }
        return var2.toString();
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException{
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }

}
