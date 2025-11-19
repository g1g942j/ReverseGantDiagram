package com.myapp.controller;

import com.myapp.entity.User;
import com.myapp.service.UserService;
import com.myapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.myapp.DTOs.ApiResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        users.forEach(user -> user.setPassword(null));
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getUserById(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            user.get().setPassword(null);
            return ResponseEntity.ok(ApiResponse.success(user.get()));
        }
        return ResponseEntity.status(404).body(ApiResponse.error("User not found"));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<?>> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isPresent()) {
            user.get().setPassword(null);
            return ResponseEntity.ok(ApiResponse.success(user.get()));
        }
        return ResponseEntity.status(404).body(ApiResponse.error("User not found"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createUser(@RequestBody User user,
                                                     @CookieValue(value = "authToken", required = false) String authToken) {
        if (!authService.isValidToken(authToken)) {
            return ResponseEntity.status(401).body(ApiResponse.error("Authentication required"));
        }
        try {
            User createdUser = userService.createUser(user);
            createdUser.setPassword(null);
            return ResponseEntity.ok(ApiResponse.success(createdUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateUser(@PathVariable Integer id,
                                                     @RequestBody User userDetails,
                                                     @CookieValue(value = "authToken", required = false) String authToken) {
        if (!authService.isValidToken(authToken)) {
            return ResponseEntity.status(401).body(ApiResponse.error("Authentication required"));
        }
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            updatedUser.setPassword(null);
            return ResponseEntity.ok(ApiResponse.success(updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable Integer id,
                                                     @CookieValue(value = "authToken", required = false) String authToken) {
        if (!authService.isValidToken(authToken)) {
            return ResponseEntity.status(401).body(ApiResponse.error("Authentication required"));
        }
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }
}