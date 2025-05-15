package com.exercie.exercies.dto.response;

import com.exercie.exercies.model.Role;

import java.util.Set;

public class LoginDtoRes {
    private String identifier;
    private String token;
    private Set<String> roles;


    public LoginDtoRes(String identifier, String token, Set<String> roles) {
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

    @Override
    public String toString() {
        return "LoginDtoRes{" +
                "identifier='" + identifier + '\'' +
                ", token='" + token + '\'' +
                ", roles=" + roles +
                '}';
    }
}
