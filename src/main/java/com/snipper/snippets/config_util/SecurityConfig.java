package com.snipper.snippets.config_util;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/home", "/login").permitAll() // Allow public access to these paths
                        .anyRequest().authenticated()  // Secure all other endpoints
                )
                .oauth2Login()  // Enable OAuth2 login
                .and()
                .logout().permitAll();  // Allow logout

        return http.build(); // Build the security filter chain
    }
}
