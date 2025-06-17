package com.exercie.exercies.dto.response;

import com.exercie.exercies.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoRes {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String role;
    private Boolean isActive;
    private Boolean emailVerified;
    private Date createdAt;
}
