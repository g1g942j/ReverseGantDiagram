package com.myapp.controller;

import com.myapp.DTOs.ApiResponse;
import com.myapp.entity.User;
import com.myapp.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody User user, HttpServletResponse response) {
        try {
            Map<String, Object> result = authService.register(user);
            User createdUser = (User) result.get("user");
            String authToken = (String) result.get("authToken");
            setAuthCookie(response, authToken);

            return ResponseEntity.ok(ApiResponse.success(createdUser));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody Map<String, String> credentials,
                                   HttpServletResponse response) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");

            String authToken = authService.login(email, password);
            setAuthCookie(response, authToken);

            return ResponseEntity.ok(ApiResponse.success("Login successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(@CookieValue(value = "authToken", required = false) String authToken,
                                    HttpServletResponse response) {
        if (authToken != null) {
            authService.logout(authToken);
            removeAuthCookie(response);
        }

        return ResponseEntity.ok(ApiResponse.success("Logout successful"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getCurrentUser(@CookieValue(value = "authToken", required = false) String authToken) {
        if (authToken == null || !authService.isValidToken(authToken)) {
            return ResponseEntity.status(401).body(ApiResponse.error("Not authenticated"));
        }

        return authService.getUserByToken(authToken)
                .map(user -> ResponseEntity.ok(ApiResponse.success(user)))
                .orElse(ResponseEntity.status(401).body(ApiResponse.error("Invalid session")));
    }

    private void setAuthCookie(HttpServletResponse response, String authToken) {
        Cookie cookie = new Cookie("authToken", authToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 дней
        response.addCookie(cookie);
    }

    private void removeAuthCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("authToken", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}