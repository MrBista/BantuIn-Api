package com.exercie.exercies.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoReq {
    private Long id;


    @NotNull(message = "email must filled")
    @NotBlank(message = "email must not empty")
    @Email(message = "format email not valid")
    private String email;


    @NotNull(message = "username must filled")
    @NotBlank(message = "username must not empty")
    private String username;


    @Size(min = 2, max = 50, message = "maximal first name is 50")
    private String firstName;

    @Size(min = 2, max = 50, message = "maximal first name is 50")
    private String lastEmail;


    @NotNull(message = "password must filled")
    @NotBlank(message = "password must not empty")
    private String password;

    private String passwordConfirmation;

    private String role;

    @NotNull(message = "phoneNumber must filled")
    @NotBlank(message = "phoneNumber must not empty")
    private String phoneNumber;

    private Date dateOfBirth;

    private LocalDateTime createdAt;
}
