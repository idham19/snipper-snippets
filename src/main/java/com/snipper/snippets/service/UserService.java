package com.snipper.snippets.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snipper.snippets.encryption_util.EncryptionUtil;
import com.snipper.snippets.model.User;
import com.snipper.snippets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    private EncryptionUtil encryptionUtil;
    @Autowired
    JdbcTemplate jdbcTemplate;
    private PasswordEncoder passwordEncoder;

    //    @Autowired
//    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,EncryptionUtil encryptionUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.encryptionUtil=encryptionUtil;
    }

    public void cleanUpUserTable() {
        jdbcTemplate.execute("SET SQL_SAFE_UPDATES = 0;");
        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("ALTER TABLE users AUTO_INCREMENT = 1;");
        jdbcTemplate.execute("SET SQL_SAFE_UPDATES = 1;");
    }


    public void importJsonData(InputStream inputStream) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<User> users = objectMapper.readValue(inputStream, new TypeReference<List<User>>() {
        });
        for (User user : users) {
            if (!user.getEmail().isEmpty()) {
                // for email, we will use cipher to encrypt which can be reversible
                String encryptedEmailUser = encryptionUtil.encrypt(user.getEmail());
                user.setEmail(encryptedEmailUser);
            }
            if (!user.getPassword().isEmpty()) {
                //for password, we will hash it using BcryptPasswordEncoder irreversible
                String hashedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(hashedPassword);
            }
            userRepository.save(user);
        }
    }

    public List<User> findAllUsers() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        List<User> users = userRepository.findAll();
        for(User user :users){
            user.setEmail(EncryptionUtil.decrypt(user.getEmail()));
        }
        return users;
    }

    public Optional<User> findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
         user.ifPresent(u->{
             if(u.getEmail() !=null){
                 try {
                     u.setEmail(EncryptionUtil.decrypt(u.getEmail()));
                 } catch (NoSuchPaddingException e) {
                     throw new RuntimeException(e);
                 } catch (NoSuchAlgorithmException e) {
                     throw new RuntimeException(e);
                 } catch (InvalidKeyException e) {
                     throw new RuntimeException(e);
                 } catch (IllegalBlockSizeException e) {
                     throw new RuntimeException(e);
                 } catch (BadPaddingException e) {
                     throw new RuntimeException(e);
                 }
             }
         });
        return user;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }


}
