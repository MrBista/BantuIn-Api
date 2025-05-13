package com.exercie.exercies.repository;

import com.exercie.exercies.model.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
}
