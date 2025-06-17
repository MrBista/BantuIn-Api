package com.exercie.exercies.service;

import com.exercie.exercies.dao.UserDao;
import com.exercie.exercies.dao.UserDaoImpl;
import com.exercie.exercies.dao.UserRoleDao;
import com.exercie.exercies.dto.request.LoginDtoReq;
import com.exercie.exercies.dto.request.UserDtoReq;
import com.exercie.exercies.dto.response.LoginDtoRes;
import com.exercie.exercies.dto.response.UserDtoRes;
import com.exercie.exercies.exception.ResourceNotFoundException;
import com.exercie.exercies.helper.RoleName;
import com.exercie.exercies.mapper.UserMapper;
import com.exercie.exercies.model.Role;
import com.exercie.exercies.model.User;
import com.exercie.exercies.model.UserRole;
import com.exercie.exercies.repository.UserRepository;
import com.exercie.exercies.repository.UserRoleRepository;
import com.exercie.exercies.security.JwtTokenService;
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

    private final JwtTokenService jwtTokenService;

    private final UserService userService;
    private final UserRoleService userRoleService;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final UserRoleDao userRoleDao;

    @Autowired
    public AuthService(@Qualifier("userDaoImpl") UserDaoImpl userDaoImpl, UserMapper userMapper, UserRepository userRepository, UserRoleRepository userRoleRepository, JwtTokenService jwtTokenService, UserService userService, UserRoleService userRoleService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserRoleDao userRoleDao) {
        this.userDaoImpl = userDaoImpl;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.jwtTokenService = jwtTokenService;
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRoleDao = userRoleDao;
    }

    public List<UserDtoRes> getAllUser(){
//        return userDaoImpl.getAllUser();
        Map<String, Object> params = new HashMap<>();
        List<User> user =  userDaoImpl.getAllUser(params);
        logger.info("users: {}", user);
        return userMapper.toDTOList(user);
    }

    @Transactional
    public void createUser(UserDtoReq userDtoReq) throws BadRequestException {
        // cari dulu email / usernamenya udah dipakai belum
        Optional<User> existingUser = userDaoImpl.findByUsernameOrEmail(userDtoReq.getUsername(), userDtoReq.getEmail());

        if (existingUser.isPresent()) {
            // Jika ada pengguna dengan username atau email yang sama, lempar exception
            throw new BadRequestException("Username atau email sudah terdaftar.");
        }


        User user = userMapper.toEntity(userDtoReq);
        user.setPassword(passwordEncoder.encode(userDtoReq.getPassword()));
        userDaoImpl.saveUser(user);
        userDtoReq.setId(user.getId());

        if (userDtoReq.getRole().isEmpty() || userDtoReq.getRole().isBlank()){
            throw new BadRequestException("roles must filled");
        }




        // !TODO tambahkan validasi apakah userrole id ada di db, kalau ga ada maka nanti throw dan buat di service yang beda
//        userRoleDao.assignRolesToUser(user.getId(), userDtoReq.getRoles());

    }

    public UserDtoRes getUserById(Long id){
        User userDtoRes = userDaoImpl.getUserById(id);
        logger.info("user res {}", userDtoRes);
        if (userDtoRes == null){
            throw new ResourceNotFoundException("User not found");
        }
        return userMapper.toDto(userDtoRes);
    }



    @Transactional
    public UserDtoRes registerUser(UserDtoReq userDtoReq) throws BadRequestException {
        // register user
        // cek dulu email atau username udah ada atau belum

       Optional<User> findUser =  userDaoImpl
                .findByUsernameOrEmail(userDtoReq.getUsername(), userDtoReq.getEmail());


       if (findUser.isPresent()){
           throw new BadRequestException("Username / email already exists !");
       }

       if (!userDtoReq.getPassword().equals(userDtoReq.getPasswordConfirmation())) {
           throw new BadRequestException("Password not match");
       }

        userService.saveUser(userDtoReq);

        // save user role
        userRoleService.saveUserRole(userDtoReq.getId());

        UserDtoRes userDtoRes = new UserDtoRes();

        userDtoRes.setId(userDtoReq.getId());
        userDtoRes.setFirstName(userDtoReq.getFirstName());
        userDtoRes.setLastName(userDtoReq.getFirstName());
        userDtoRes.setEmail(userDtoReq.getEmail());
        userDtoRes.setUsername(userDtoReq.getUsername());
        userDtoRes.setRole(RoleName.CUSTOMER.name());
        userDtoRes.setIsActive(true);
//        userDtoRes.setCreatedAt(userDtoRes.getCreatedAt());


        return userDtoRes;
    }


    public LoginDtoRes loginUser(LoginDtoReq loginDtoReq) {
        //
        SecurityContext contextHolder = SecurityContextHolder.createEmptyContext();
        // ini materi besok buat sendiri authentication manager

        Authentication unauthenticatedToken = new UsernameEmailPasswordAuthentication(loginDtoReq.getIdentifier(), loginDtoReq.getPassword());


        Authentication authentication = authenticationManager.authenticate(unauthenticatedToken);


        contextHolder.setAuthentication(authentication);

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        String tokenGenerated = jwtTokenService.generateToken(authentication);

        User userDetail = (User) authentication.getPrincipal();


        LoginDtoRes loginDtoRes = new LoginDtoRes();

        loginDtoRes.setIdentifier(authentication.getName());
        loginDtoRes.setRoles(roles);
        loginDtoRes.setToken(tokenGenerated);
        loginDtoRes.setName(userDetail.getName());
        loginDtoRes.setId(userDetail.getId());
        loginDtoRes.setEmail(userDetail.getEmail());
        loginDtoRes.setAvatar("");
        return loginDtoRes;
    }

    @Transactional
    public void deleteUserById(Long userId) {
            // ! TODO tambahkan validasi usernya ada apa tidak
            userDaoImpl
                    .findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            userDaoImpl.deleteUserById(userId);
    }


}
