package com.employee.controller;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.dto.AuthResponse;
import com.employee.dto.LoginRequest;
import com.employee.dto.RegisterRequest;
import com.employee.entity.Role;
import com.employee.entity.User;
import com.employee.repository.UserRepository;
import com.employee.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        // Convert Strings → Enum Roles
        Set<Role> assignedRoles = request.getRoles()
                .stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(assignedRoles)
                .build();

        userRepo.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var opt = userRepo.findByUsername(request.getUsername());
        if (opt.isEmpty() || !passwordEncoder.matches(request.getPassword(), opt.get().getPassword())) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
        var roles = opt.get().getRoles().stream().map(Enum::name).collect(Collectors.toSet());
        String token = jwtUtil.generateToken(opt.get().getUsername(), roles);
        return ResponseEntity.ok(new AuthResponse(token));
    }
    
}
