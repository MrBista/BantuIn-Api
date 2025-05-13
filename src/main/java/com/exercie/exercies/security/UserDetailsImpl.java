package com.exercie.exercies.security;

import com.exercie.exercies.helper.RoleName;
import com.exercie.exercies.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String username;
    private String email;
    private String password;

    private Collection<? extends GrantedAuthority> authorities;


    public UserDetailsImpl(Long id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public UserDetailsImpl() {
    }

    public static UserDetailsImpl build(User user, List<RoleName> roleList){
        List<GrantedAuthority> authorityList =  roleList
                .stream()
                .map((role) -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        UserDetailsImpl userDetails = new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), authorityList);

        return userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId(){
        return this.id;
    }

    public String getEmail(){
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
