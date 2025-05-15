package com.exercie.exercies.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public class UsernameEmailPasswordAuthentication extends AbstractAuthenticationToken {
    private Object principal;
    private Object credentials;

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
