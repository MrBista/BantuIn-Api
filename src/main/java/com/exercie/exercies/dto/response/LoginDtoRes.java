package com.exercie.exercies.dto.response;

import com.exercie.exercies.model.Role;

import java.util.Set;

public class LoginDtoRes {
    private Long id;
    private String name;
    private String email;
    private String avatar;
    private String identifier;
    private String token;
    private Set<String> roles;


    public LoginDtoRes(String identifier, String token, Set<String> roles) {
        this.identifier = identifier;
        this.token = token;
        this.roles = roles;
    }

    public LoginDtoRes(Long id, String name, String email, String avatar, String identifier, String token, Set<String> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.identifier = identifier;
        this.token = token;
        this.roles = roles;
    }

    public LoginDtoRes() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "LoginDtoRes{" +
                "identifier='" + identifier + '\'' +
                ", token='" + token + '\'' +
                ", roles=" + roles +
                '}';
    }
}
