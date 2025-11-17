package com.myapp;

import com.myapp.entity.User;
import com.myapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== JAVA SPRING BOOT APPLICATION STARTED ===");

        long userCount = userService.getUserCount();
        System.out.println("Total users in database: " + userCount);

        System.out.println("Existing users:");
        userService.getAllUsers().forEach(user -> {
            System.out.println(" - " + user.getName() + " (" + user.getEmail() + ")");
        });

        System.out.println("=== APPLICATION READY ===");
    }
}