package com.exercie.exercies.service;

import com.exercie.exercies.dao.UserRoleDao;
import com.exercie.exercies.exception.ResourceNotFoundException;
import com.exercie.exercies.model.User;
import com.exercie.exercies.model.UserRole;
import com.exercie.exercies.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    private final UserRoleDao userRoleDao;

    @Autowired
    public UserRoleService(UserRoleRepository userRoleRepository, UserRoleDao userRoleDao) {
        this.userRoleRepository = userRoleRepository;
        this.userRoleDao = userRoleDao;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUserRole(Long userId){
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }


        userRoleDao.saveUserRoleCustomer(userId);
    }

    public List<UserRole> findByUserId(Long userId){
        if (userId == null){
            throw new IllegalArgumentException("User ID cannot be null");

        }
        List<UserRole> userRoleList = userRoleDao.findByUserId(userId);
        if (userRoleList.isEmpty()){
            throw new ResourceNotFoundException("user have no roles");
        }

        return userRoleList;
    }

    public Set<GrantedAuthority> findRoleUserGrantedAuthority(Long userId){
        List<UserRole> userRoleList = findByUserId(userId);

       return userRoleList
                .stream()
                .map((userRole) -> new SimpleGrantedAuthority(userRole.getRole().getName().toString()))
                .collect(Collectors.toSet());

    }
}
