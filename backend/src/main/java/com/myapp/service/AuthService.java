package com.myapp.service;

import com.myapp.entity.User;
import com.myapp.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityUtil securityUtil;

    private Map<String, Integer> activeSessions = new HashMap<>();

    public Map<String, Object> register(User user) {
        if (!securityUtil.isValidEmail(user.getEmail())) {
            throw new RuntimeException("Invalid email format");
        }

        if (!securityUtil.isValidPassword(user.getPassword())) {
            throw new RuntimeException("Password must be at least 6 characters");
        }

        if (userService.getUserByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        String originalPassword = user.getPassword();
        String hashedPassword = securityUtil.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        User createdUser = userService.createUser(user);
        String authToken = login(user.getEmail(), originalPassword);

        Map<String, Object> result = new HashMap<>();
        result.put("user", createdUser);
        result.put("authToken", authToken);

        return result;
    }

    public String login(String email, String password) {
        if (!securityUtil.isValidEmail(email)) {
            throw new RuntimeException("Invalid email format");
        }

        if (!securityUtil.isValidPassword(password)) {
            throw new RuntimeException("Invalid password");
        }

        Optional<User> userOpt = userService.getUserByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();

        if (!securityUtil.checkPassword(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String authToken = securityUtil.generateAuthToken();
        activeSessions.put(authToken, user.getId());
        return authToken;
    }
    public void logout(String authToken) {
        activeSessions.remove(authToken);
    }
    public boolean isValidToken(String authToken) {
        return authToken != null && activeSessions.containsKey(authToken);
    }
    public Optional<User> getUserByToken(String authToken) {
        if (!isValidToken(authToken)) {
            return Optional.empty();
        }
        Integer userId = activeSessions.get(authToken);
        return userService.getUserById(userId);
    }
}
