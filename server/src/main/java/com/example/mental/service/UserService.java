package com.example.mental.service;

import com.example.mental.dto.UserDTO;
import com.example.mental.entity.User;

public interface UserService {
    UserDTO register(User user);
    UserDTO login(String username, String password);
} 