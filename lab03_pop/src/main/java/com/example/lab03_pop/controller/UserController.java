package com.example.lab03_pop.controller;

import com.example.lab03_pop.auth.JwtTokenProvider;
import com.example.lab03_pop.entity.Role;
import com.example.lab03_pop.entity.User;
import com.example.lab03_pop.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam Role role){
        User user = userService.registerUser(username, password, role);
        String token = jwtTokenProvider.generateToken(user);
        return ResponseEntity.ok(token);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(
            @RequestParam String username,
            @RequestParam String password){
        userService.deleteUser(username, password);
        return ResponseEntity.ok("User deleted");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String username,
            @RequestParam String password) throws IllegalAccessException {
        return ResponseEntity.ok(userService.loginUser(username, password));

    }

    @GetMapping("/manager/getControllers")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<Map<String, Object>>> getAllControllers() {
        return ResponseEntity.ok(userService.getAllControllers());
    }

    @GetMapping("/manager/getTenants")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<Map<String, Object>>> getAllTenants() {
        return ResponseEntity.ok(userService.getAllTenants());
    }

}

