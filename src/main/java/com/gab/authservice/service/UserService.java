package com.gab.authservice.service;

import com.gab.authservice.dto.LoginRequest;
import com.gab.authservice.entity.User;
import com.gab.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByUserName(String userName) {
        return userRepository.findByEmail(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
