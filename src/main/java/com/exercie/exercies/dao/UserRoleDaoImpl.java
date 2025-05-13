package com.exercie.exercies.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRoleDaoImpl implements UserRoleDao{
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final String INSERT_CUSTOMER_ROLE_QUERY = """
                insert into user_roles (user_id, role_id)
                value (:userId, :roleId)
            """;


    @Autowired
    public UserRoleDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void saveUserRoleCustomer(Long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("roleId", 3);
        namedParameterJdbcTemplate.update(INSERT_CUSTOMER_ROLE_QUERY, params);
    }
}
