package com.snipper.snippets;

import com.snipper.snippets.encryption_util.EncryptionUtil;
import com.snipper.snippets.service.SnippetService;
import com.snipper.snippets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class SnippetsApplication implements CommandLineRunner {
    @Autowired
    @Lazy
    UserService userService;
    @Autowired
    @Lazy
    SnippetService snippetService;


    public static void main(String[] args) {
        SpringApplication.run(SnippetsApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public EncryptionUtil encryptionUtil() {
        return new EncryptionUtil();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll() // Allow all requests
                );
        return http.build();
    }

    @Override
    public void run(String... args) {
        try {
            // Clean tables before injecting data
            snippetService.cleanUpSnippetTable();
            userService.cleanUpUserTable();

            // Load JSON files from classpath (assets folder in resources)
            ClassPathResource userResource = new ClassPathResource("userData.json");
            ClassPathResource snippetResource = new ClassPathResource("snippetData.json");

            // Check if the resources exist
            if (!snippetResource.exists()) {
                System.out.println("Snippet JSON file not found.");
                return;
            }
            if (!userResource.exists()) {
                System.out.println("User JSON file not found.");
                return;
            }

            // Read the JSON data
            try (InputStream userStream = userResource.getInputStream()) {
                userService.importJsonData(userStream);
            } catch (IOException e) {
                System.out.println("Error reading user JSON: " + e.getMessage());
            }

            try (InputStream snippetStream = snippetResource.getInputStream()) {
                snippetService.importJsonData(snippetStream);
            } catch (IOException e) {
                System.out.println("Error reading device JSON: " + e.getMessage());
            }
            System.out.println("Data imported from JSON files successfully.");
        } catch (Exception e) {
            System.out.println("An error occurred during the data import process: " + e.getMessage());
        }
    }

}
