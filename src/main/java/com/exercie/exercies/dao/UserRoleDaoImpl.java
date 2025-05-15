package com.exercie.exercies.dao;

import com.exercie.exercies.helper.RoleName;
import com.exercie.exercies.model.Role;
import com.exercie.exercies.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRoleDaoImpl implements UserRoleDao{
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final String INSERT_CUSTOMER_ROLE_QUERY = """
                insert into user_roles (user_id, role_id)
                value (:userId, :roleId)
            """;

    private final String FIND_BY_USER_ID_QUERY = """
                select ur.user_id as userId, ur.role_id as roleId, r.name as roleName
                from user_roles as ur
                left join roles as r on ur.role_id = r.id
                where ur.user_id = :userId
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

    @Override
    public List<UserRole> findByUserId(Long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        return namedParameterJdbcTemplate.query(FIND_BY_USER_ID_QUERY, params,userRoleRowMapper);
    }

    private final RowMapper<UserRole> userRoleRowMapper = (rs, rowNum) -> {
        UserRole userRole = new UserRole();

        userRole.setUserId(rs.getLong("userId"));
        userRole.setRoleId(rs.getLong("roleId"));

        if (rs.getString("roleName") != null){
            Role role = new Role();
            role.setName(RoleName.valueOf(rs.getString("roleName")));
            userRole.setRole(role);
        }

        return userRole;

    };
}
