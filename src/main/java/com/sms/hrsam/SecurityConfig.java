package com.sms.hrsam;

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
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(auth -> auth
                        .requestMatchers("/api/surveys/**").permitAll()  // Bu endpoint'i herkese açık hale getirir
                        .requestMatchers("/api/users").permitAll()  // Kullanıcı oluşturma endpoint'ini de açık bırakır
                        .anyRequest().permitAll()  // Diğer tüm istekler için kimlik doğrulama gerektirmeden açık
                );

        return http.build();
    }
}
