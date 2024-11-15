package com.snipper.snippets.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snipper.snippets.model.User;
import com.snipper.snippets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void cleanUpUserTable() {
        jdbcTemplate.execute("SET SQL_SAFE_UPDATES = 0;");
        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("ALTER TABLE users AUTO_INCREMENT = 1;");
        jdbcTemplate.execute("SET SQL_SAFE_UPDATES = 1;");
    }


    public void importJsonData(InputStream inputStream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<User> users = objectMapper.readValue(inputStream, new TypeReference<List<User>>() {
        });
        for (User user : users
        ) {
            userRepository.save(user);
        }
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }


}
