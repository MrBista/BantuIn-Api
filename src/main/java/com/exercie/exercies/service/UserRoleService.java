package com.exercie.exercies.service;

import com.exercie.exercies.dao.UserRoleDao;
import com.exercie.exercies.model.User;
import com.exercie.exercies.model.UserRole;
import com.exercie.exercies.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
}
