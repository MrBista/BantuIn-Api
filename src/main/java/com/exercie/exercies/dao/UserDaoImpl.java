package com.exercie.exercies.dao;

import com.exercie.exercies.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class UserDaoImpl implements UserDao{
    Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate, DataSource dataSource, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<User> getAllUser(Map<String, Object> params) {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();


        try(Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
        ){
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    users.add(mapRowToUser(rs));
                }
            }
        }catch (SQLException e){
            throw new RuntimeException("Error finding all product with error message " + e.getMessage());
        }

        return users;
    }

    @Override
    public User getUserById(Long id) {
        String sql = "SELECT * FROM users where id=?";
        User user = null;
        try(Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setLong(1, id);
            try(ResultSet rs = ps.executeQuery()){
                // karena pasti satu jadi ga perlu pakai while
                if (rs.next()){
                    user = mapRowToUser(rs);
                }
            }
        }catch (SQLException e){
            logger.error("error sql: {}", e.getMessage());
            throw new RuntimeException("something went wrong in getUserById");
        }
        return user;
    }

    @Override
    public void saveUser(User user) {
        String sql = """
                    INSERT INTO users(email, username, name, password) values(?, ?, ?, ?)
                """ ;
        try(Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ){
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getName());
            ps.setString(4, user.getPassword());
            int arrowAffacted = ps.executeUpdate();
            if (arrowAffacted == 0){
                throw new SQLException("now row affected");
            }
            try (ResultSet rs = ps.getGeneratedKeys()){
                if (rs.next()){
                    user.setId(rs.getLong(1));
                }else {
                    throw new SQLException("Creating product failed, no id obtained");
                }
            }
        }catch (SQLException e){
            logger.error("error sql {} ", e.getMessage());
            throw new RuntimeException("Something went wrong in save user");
        }
    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public Optional<User> findByUsername(String username) {
        String query = """
                    SELECT * FROM user
                    where username = :username
                """;

        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        User user = namedParameterJdbcTemplate.queryForObject(query, params, userRowMapper);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String username, String email) {
        String query = """
                    SELECT * FROM user
                    where username = :username or email = :email
                """;

        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("email", email);
        User user = namedParameterJdbcTemplate.queryForObject(query, params, userRowMapper);
        return Optional.ofNullable(user);
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setEmail(rs.getString("email"));
        user.setUsername(rs.getString("username"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        return user;
    }


    private final RowMapper<User> userRowMapper = (rs, i) ->{
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setName(rs.getString("name"));
        return user;
    };
}
