package com.exercie.exercies.dao;

import com.exercie.exercies.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserDao {
    public List<User> getAllUser(Map<String, Object> params);
    public User getUserById(Long id);
    public void saveUser(User user);
    public void deleteUser(User user);
    public void deleteUserById(Long id);
    public Optional<User> findByUsername(String username);

    public Optional<User> findById(Long id);

    public Optional<User> findByUsernameOrEmail(String username, String email);
}

