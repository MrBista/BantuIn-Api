package com.exercie.exercies.mapper;

import com.exercie.exercies.dto.request.UserDtoReq;
import com.exercie.exercies.dto.response.UserDtoRes;
import com.exercie.exercies.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class UserMapper {
    public UserDtoRes toDto(User user){
        UserDtoRes userDtoRes = new UserDtoRes();
        userDtoRes.setId(user.getId());
        userDtoRes.setName(user.getName());
        userDtoRes.setEmail(user.getEmail());
        userDtoRes.setUsername(user.getUsername());
        return userDtoRes;
    }

    public User toEntity(UserDtoReq userDtoReq){
        User user = new User();
        user.setId(userDtoReq.getId());
        user.setPassword(userDtoReq.getPassword());
        user.setName(userDtoReq.getName());
        user.setEmail(userDtoReq.getEmail());
        user.setUsername(userDtoReq.getUsername());
        return user;
    }

    public List<UserDtoRes> toDTOList(Iterable<User> users){
        return StreamSupport.stream(users.spliterator(), false)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
