package com.exercie.exercies.service;

import com.exercie.exercies.dao.UserDao;
import com.exercie.exercies.dto.request.UserDtoReq;
import com.exercie.exercies.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserDao userDaoImpl;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserDao userDaoImpl) {
        this.passwordEncoder = passwordEncoder;
        this.userDaoImpl = userDaoImpl;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUser(UserDtoReq userDtoReq){
        User user = new User();
        user.setName(userDtoReq.getName());
        user.setPassword(passwordEncoder.encode(userDtoReq.getPassword()));
        user.setUsername(userDtoReq.getUsername());
        user.setEmail(userDtoReq.getEmail());
        user.setCreatedAt(LocalDateTime.now());

        userDaoImpl.saveUser(user);

        userDtoReq.setId(user.getId());
    }
}
