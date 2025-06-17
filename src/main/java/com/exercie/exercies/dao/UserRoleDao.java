package com.exercie.exercies.dao;

import com.exercie.exercies.model.UserRole;

import java.util.List;

public interface UserRoleDao {
    void saveUserRoleCustomer(Long userId);
    void saveUserRolesBatch();
    List<UserRole> findByUserId(Long userId);
    int assignRolesToUser(Long userId, List<Long> roleIds);
}
