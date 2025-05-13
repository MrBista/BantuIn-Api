package com.exercie.exercies.dto.request;

import jakarta.validation.constraints.*;

public class UserDtoReq {
    private Long id;


    @NotNull(message = "email must filled")
    @NotBlank(message = "email must not empty")
    @Email(message = "format email not valid")
    private String email;


    @NotNull(message = "username must filled")
    @NotBlank(message = "username must not empty")
    private String username;


    @NotNull(message = "name must filled")
    @NotBlank(message = "name must not empty")
    private String name;

    @NotNull(message = "password must filled")
    @NotBlank(message = "password must not empty")
    private String password;

    public UserDtoReq() {
    }

    public UserDtoReq(Long id, String email, String username, String name, String password) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.name = name;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDtoReq{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
