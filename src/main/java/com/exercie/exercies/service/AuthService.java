package com.exercie.exercies.service;

import com.exercie.exercies.dao.UserDao;
import com.exercie.exercies.dao.UserDaoImpl;
import com.exercie.exercies.dto.request.LoginDtoReq;
import com.exercie.exercies.dto.request.UserDtoReq;
import com.exercie.exercies.dto.response.LoginDtoRes;
import com.exercie.exercies.dto.response.UserDtoRes;
import com.exercie.exercies.mapper.UserMapper;
import com.exercie.exercies.model.User;
import com.exercie.exercies.model.UserRole;
import com.exercie.exercies.repository.UserRepository;
import com.exercie.exercies.repository.UserRoleRepository;
import com.exercie.exercies.security.UsernameEmailPasswordAuthentication;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService {
    Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserDao userDaoImpl;
    private final UserMapper userMapper;

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    private final UserService userService;
    private final UserRoleService userRoleService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(@Qualifier("userDaoImpl") UserDaoImpl userDaoImpl, UserMapper userMapper, UserRepository userRepository, UserRoleRepository userRoleRepository, UserService userService, UserRoleService userRoleService, AuthenticationManager authenticationManager) {
        this.userDaoImpl = userDaoImpl;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.authenticationManager = authenticationManager;
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



    @Transactional
    public void registerUser(UserDtoReq userDtoReq) throws BadRequestException {
        // register user
        // cek dulu email atau username udah ada atau belum

       Optional<User> findUser =  userDaoImpl
                .findByUsernameOrEmail(userDtoReq.getUsername(), userDtoReq.getEmail());


       if (findUser.isPresent()){
           throw new BadRequestException("Username / email already exists !");
       }

        // 1. KALAU SUDAH DI REGISTER USERNYA MAKA INSERT JUGA BAGIAN USER ROLE

        userService.saveUser(userDtoReq);

        // save user role
        userRoleService.saveUserRole(userDtoReq.getId());

    }


    public LoginDtoRes loginUser(LoginDtoReq loginDtoReq) {
        //
        SecurityContext contextHolder = SecurityContextHolder.createEmptyContext();
        // ini materi besok buat sendiri authentication manager

        Authentication unauthenticatedToken = new UsernameEmailPasswordAuthentication(loginDtoReq.getIdentifier(), loginDtoReq.getPassword());

        // disini validasi authenticateion terjadi yang kedaftar adalah UsernameEmailPasswordAuthentication
        // nanti kedepannya kita akan nambah via otp
        Authentication authentication = authenticationManager.authenticate(unauthenticatedToken);

        String token = "ini nanti token";
        contextHolder.setAuthentication(authentication);

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        LoginDtoRes loginDtoRes = new LoginDtoRes();

        loginDtoRes.setIdentifier(authentication.getName());
        loginDtoRes.setRoles(roles);
        loginDtoRes.setToken(token);
        return loginDtoRes;
    }
}
