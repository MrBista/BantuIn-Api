package com.exercie.exercies.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LoginDtoReq {

    @NotBlank(message = "identifier is must filled")
    @NotNull(message = "identifier is must")
    private String identifier;



    @NotBlank(message = "identifier is must filled")
    @NotNull(message = "identifier is must")
    private String password;


    public LoginDtoReq() {
    }

    public LoginDtoReq(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginDto{" +
                "identifier='" + identifier + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
