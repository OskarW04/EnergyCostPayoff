package com.example.lab03_pop.service;

import com.example.lab03_pop.auth.AuthResponse;
import com.example.lab03_pop.auth.CustomUserDetails;
import com.example.lab03_pop.auth.JwtTokenProvider;
import com.example.lab03_pop.entity.*;
import com.example.lab03_pop.repository.ControllerRepository;
import com.example.lab03_pop.repository.TenantRepository;
import com.example.lab03_pop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ControllerRepository controllerRepository;
    private final TenantRepository tenantRepository;
    @Transactional
    public User registerUser(String username, String password, Role role){
        if(userRepository.existsByUsername(username)){
            throw new IllegalArgumentException("Username already exists");
        }
        User user;
        if(role == Role.TENANT){
            user = new Tenant();
        } else if (role == Role.MANAGER) {
            user = new Manager();
        } else if (role == Role.CONTROLLER) {
            user = new Controller();
        } else throw new IllegalArgumentException("Wrong role");

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }


    @Transactional
    public void deleteUser(String username, String password){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if(passwordEncoder.matches(password, user.getPassword())){
            userRepository.delete(user);
        }else{
            throw new IllegalArgumentException("Wrong password");
        }
    }

    @Transactional
    public AuthResponse loginUser(String username, String password) throws IllegalAccessException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new IllegalAccessException("Wrong Password");
        }

        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponse(token);
    }

    public List<Map<String, Object>> getAllControllers() {
        CustomUserDetails currentUserDetails = getCurrentUser();
        User currentUser = currentUserDetails.user();

        if (!currentUser.getRole().equals(Role.MANAGER)) {
            throw new IllegalArgumentException("Only manager can do this");
        }

        List<Map<String, Object>> controllerList = new ArrayList<>();
        for (Controller controller : controllerRepository.findAll()) {
            Map<String, Object> controllerData = new HashMap<>();
            controllerData.put("id", controller.getId());
            controllerData.put("username", controller.getUsername());
            controllerList.add(controllerData);
        }
        return controllerList;
    }

    public List<Map<String, Object>> getAllTenants() {
        CustomUserDetails currentUserDetails = getCurrentUser();
        User currentUser = currentUserDetails.user();

        if (!currentUser.getRole().equals(Role.MANAGER)) {
            throw new IllegalArgumentException("Only manager can view this");
        }
        List<Map<String, Object>> tenantList = new ArrayList<>();
        for (Tenant tenant : tenantRepository.findAll()) {
            Map<String, Object> tenantData = new HashMap<>();
            tenantData.put("id", tenant.getId());
            tenantData.put("username", tenant.getUsername());
            tenantList.add(tenantData);
        }
        return tenantList;
    }

    private CustomUserDetails getCurrentUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
