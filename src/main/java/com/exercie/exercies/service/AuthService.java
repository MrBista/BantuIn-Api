package com.exercie.exercies.service;

import com.exercie.exercies.dao.UserDaoImpl;
import com.exercie.exercies.dto.request.UserDtoReq;
import com.exercie.exercies.dto.response.UserDtoRes;
import com.exercie.exercies.mapper.UserMapper;
import com.exercie.exercies.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {
    Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserDaoImpl userDaoImpl;
    private final UserMapper userMapper;

    @Autowired
    public AuthService(@Qualifier("userDaoImpl") UserDaoImpl userDaoImpl, UserMapper userMapper) {
        this.userDaoImpl = userDaoImpl;
        this.userMapper = userMapper;
    }

    public List<UserDtoRes> getAllUser(){
//        return userDaoImpl.getAllUser();
        Map<String, Object> params = new HashMap<>();
        List<User> user =  userDaoImpl.getAllUser(params);
        logger.info("users: {}", user);
        return userMapper.toDTOList(user);
    }

    @Transactional
    public void createUser(UserDtoReq userDtoReq){
        User user = userMapper.toEntity(userDtoReq);
        userDaoImpl.saveUser(user);
        userDtoReq.setId(user.getId());
    }

    public UserDtoRes getUserById(Long id){
        User userDtoRes = userDaoImpl.getUserById(id);
        logger.info("user res {}", userDtoRes);
        if (userDtoRes == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }
        return userMapper.toDto(userDtoRes);
    }


}
