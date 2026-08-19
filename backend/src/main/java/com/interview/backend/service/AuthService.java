package com.interview.backend.service;


import com.interview.backend.dto.LoginRequestDTO;
import com.interview.backend.dto.LoginResponseDTO;
import com.interview.backend.dto.RegisterRequestDTO;
import com.interview.backend.entity.User;
import com.interview.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public LoginResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered.");
        }

        User user = new User(request.getUsername(), request.getEmail(), request.getPassword());
        User savedUser = userRepository.save(user);

        return new LoginResponseDTO(savedUser.getId(), savedUser.getUsername(), "Registration successful.");
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password."));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid username or password.");
        }

        return new LoginResponseDTO(user.getId(), user.getUsername(), "Login successful.");
    }
}