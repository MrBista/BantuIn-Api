package com.exercie.exercies.security;

import com.exercie.exercies.dao.UserDao;
import com.exercie.exercies.exception.ResourceNotFoundException;
import com.exercie.exercies.helper.RoleName;
import com.exercie.exercies.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDao userDao;

    @Autowired
    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User findUser = userDao
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new ResourceNotFoundException("username/email not found"));


        // cari role nya

        List<RoleName> roleUser = new ArrayList<>();



        return UserDetailsImpl.build(findUser, roleUser);
    }
}
