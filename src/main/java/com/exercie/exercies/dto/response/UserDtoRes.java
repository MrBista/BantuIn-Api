package com.exercie.exercies.dto.response;

public class UserDtoRes {
    private Long id;
    private String email;
    private String username;
    private String name;

    public UserDtoRes() {
    }

    public UserDtoRes(Long id, String email, String username, String name) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.name = name;
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

    @Override
    public String toString() {
        return "UserDtoRes{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
