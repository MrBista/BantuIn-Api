package com.exercie.exercies.controller;


import com.exercie.exercies.dto.request.UserDtoReq;
import com.exercie.exercies.dto.response.CommonResponse;
import com.exercie.exercies.dto.response.UserDtoRes;
import com.exercie.exercies.service.AuthService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final AuthService userService;

    @Autowired
    public UserController(AuthService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN') or hasAuthority('ADMIN')")
    public ResponseEntity<?> findAllUser(){
        List<UserDtoRes> data = userService.getAllUser();

        return CommonResponse.generateResponse(data, "Sucessfully get data", HttpStatus.OK);
    }




    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN') or hasAuthority('ADMIN')")
    public ResponseEntity<?> findById(@PathVariable("id")Long  userId){
        UserDtoRes userDtoRes = userService.getUserById(userId);
        return CommonResponse.generateResponse(userDtoRes, "success", HttpStatus.OK);
    }



    @PostMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN') or hasAuthority('ADMIN')")
    public ResponseEntity<?> inserUser(@RequestBody UserDtoReq userDtoReq) throws BadRequestException {
        userService.createUser(userDtoReq);
        return CommonResponse.generateResponse(userDtoReq, "success", HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN') or hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUserById(@PathVariable("id")Long userId){
        userService.deleteUserById(userId);
        return CommonResponse.generateResponse(true, "success", HttpStatus.OK);
    }


}
