package com.employee.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.employee.util.JwtAuthFilter;
import com.employee.util.JwtUtil;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
    
    @SuppressWarnings("removal")
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthFilter jwtFilter = new JwtAuthFilter(jwtUtil);

        http.csrf().disable()
            .authorizeHttpRequests()
                .requestMatchers("/api/auth/**", "/actuator/**", "/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html","/actuator/**").permitAll()
                .anyRequest().authenticated()
            .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
}
