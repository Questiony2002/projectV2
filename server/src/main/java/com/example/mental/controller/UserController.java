package com.example.mental.controller;

import com.example.mental.dto.UserDTO;
import com.example.mental.entity.User;
import com.example.mental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody User user) {
        return ResponseEntity.ok(userService.login(user.getUsername(), user.getPassword()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }
} 