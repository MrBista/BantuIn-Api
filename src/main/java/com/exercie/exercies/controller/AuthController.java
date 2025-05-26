package com.exercie.exercies.controller;

import com.exercie.exercies.dto.request.LoginDtoReq;
import com.exercie.exercies.dto.request.UserDtoReq;
import com.exercie.exercies.dto.response.CommonResponse;
import com.exercie.exercies.dto.response.LoginDtoRes;
import com.exercie.exercies.dto.response.UserDtoRes;
import com.exercie.exercies.service.AuthService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }




    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDtoReq userDtoReq) throws BadRequestException {
        authService.registerUser(userDtoReq);
        return CommonResponse.generateResponse(userDtoReq, "success", HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginDtoReq loginDtoReq){
        LoginDtoRes loginDtoRes = authService.loginUser(loginDtoReq);
        return CommonResponse.generateResponse(loginDtoRes, "success", HttpStatus.OK);
    }
}
